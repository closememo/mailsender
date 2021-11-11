package com.closememo.mailsender.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class PostDTO {

  private final String title;
  private final String content;
  private final List<String> tags;
  private final ZonedDateTime createdAt;

  public PostDTO(String title, String content, List<String> tags, ZonedDateTime createdAt) {
    this.title = title;
    this.content = content;
    this.tags = tags;
    this.createdAt = createdAt;
  }
}
