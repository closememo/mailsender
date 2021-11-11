package com.closememo.mailsender.dto;

import java.io.File;
import lombok.Getter;

@Getter
public class MailDTO {

  private final String email;
  private final String title;
  private final String text;
  private final File file;

  public MailDTO(String email, String title, String text, File file) {
    this.email = email;
    this.title = title;
    this.text = text;
    this.file = file;
  }
}
