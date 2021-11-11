package com.closememo.mailsender.controller.system.requests;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class SendMailRequest {

  private String email;
  private List<Post> posts;

  @Getter
  public static class Post {
    private String title;
    private String content;
    private List<String> tags;
    private ZonedDateTime createdAt;
  }
}
