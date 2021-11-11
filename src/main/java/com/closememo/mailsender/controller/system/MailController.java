package com.closememo.mailsender.controller.system;

import com.closememo.mailsender.controller.system.requests.SendMailRequest;
import com.closememo.mailsender.dto.PostDTO;
import com.closememo.mailsender.service.MailService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SystemMailSenderInterface
public class MailController {

  private final MailService mailService;

  public MailController(MailService mailService) {
    this.mailService = mailService;
  }

  @PostMapping("/send-mail")
  public void sendMail(@RequestBody SendMailRequest request) {

    List<PostDTO> posts = Collections.emptyList();
    if (CollectionUtils.isNotEmpty(request.getPosts())) {
      posts = request.getPosts().stream()
          .map(post -> new PostDTO(post.getTitle(), post.getContent(), post.getTags(), post.getCreatedAt()))
          .collect(Collectors.toList());
    }

    mailService.sendMail(request.getEmail(), posts);
  }
}
