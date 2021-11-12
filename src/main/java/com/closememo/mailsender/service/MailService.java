package com.closememo.mailsender.service;

import com.closememo.mailsender.dto.MailDTO;
import com.closememo.mailsender.dto.PostDTO;
import com.closememo.mailsender.infra.file.FileHandler;
import com.closememo.mailsender.infra.mail.MailClient;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {

  private static final String DEFAULT_TITLE = "(제목없음)";

  private final FileHandler fileHandler;
  private final MailClient mailClient;

  public MailService(FileHandler fileHandler, MailClient mailClient) {
    this.fileHandler = fileHandler;
    this.mailClient = mailClient;
  }

  public void sendMail(String email, List<PostDTO> posts) {
    List<PostDTO> replacedPosts = posts.stream()
        .map(MailService::checkAndAdjustPost)
        .collect(Collectors.toList());

    File compressed;
    try {
      compressed = fileHandler.getZipFile(email, replacedPosts);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      log.error("making zip file failed! email={}", email);
      return;
    }

    MailDTO mail = new MailDTO(email, "[테스트] 제목", "[데스트] 본문", compressed);

    try {
      mailClient.send(mail);
    } catch (MessagingException e) {
      log.error(e.getMessage(), e);
      log.error("sending mail failed! email={}", email);
    }

    if (compressed != null) {
      fileHandler.clear(compressed);
    }
  }

  private static PostDTO checkAndAdjustPost(PostDTO post) {
    String title = StringUtils.isBlank(post.getTitle())
        ? DEFAULT_TITLE
        : post.getTitle().replaceAll("[\\\\/:*?\"<>|]", "□");

    return new PostDTO(
        title,
        post.getContent(),
        post.getTags(),
        post.getCreatedAt()
    );
  }
}
