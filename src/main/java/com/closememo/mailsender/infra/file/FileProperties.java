package com.closememo.mailsender.infra.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties("file")
@Configuration
public class FileProperties {

  private String workspace;
}
