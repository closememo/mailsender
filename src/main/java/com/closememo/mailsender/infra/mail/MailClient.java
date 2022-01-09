package com.closememo.mailsender.infra.mail;

import com.closememo.mailsender.dto.MailDTO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailClient {

  private final JavaMailSender mailSender;

  private static final String SENDER = "클로즈메모<closememo@gmail.com>";

  public MailClient(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void send(MailDTO mail) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

    messageHelper.setFrom(SENDER);
    messageHelper.setTo(mail.getEmail());
    messageHelper.setSubject(mail.getTitle());
    messageHelper.setText(mail.getText());
    if (mail.getFile() != null) {
      messageHelper.addAttachment(mail.getFile().getName(), mail.getFile());
    }

    mailSender.send(message);
  }
}
