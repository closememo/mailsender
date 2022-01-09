package com.closememo.mailsender.infra.file;

import com.closememo.mailsender.dto.PostDTO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileHandler {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final String workspace;

  public FileHandler(FileProperties properties) {
    this.workspace = properties.getWorkspace();
  }

  public File getZipFile(String email, List<PostDTO> posts) throws IOException {
    LinkedHashMap<String, PostInfo> nameStreamMap = getNameStreamMap(posts);
    return getZip(email, nameStreamMap);
  }

  private LinkedHashMap<String, PostInfo> getNameStreamMap(List<PostDTO> posts) {
    LinkedHashMap<String, PostInfo> map = new LinkedHashMap<>();

    for (PostDTO post : posts) {
      String title = post.getTitle();

      for (int i = 0; ; i++) {
        String name = (i == 0) ? title : title + "(" + i + ")";

        if (!map.containsKey(name)) {
          map.put(name, getPostInfo(post));
          break;
        }

        if (i > 1000) {
          throw new RuntimeException("Too many iterations");
        }
      }
    }

    return map;
  }

  private File getZip(String email, LinkedHashMap<String, PostInfo> nameStreamMap)
      throws IOException {

    createWorkspaceIfNotExist();

    String zipFileName = email + "-" + nowString() + ".zip";
    File zipFile = Paths.get(workspace, zipFileName).toFile();

    try (ZipOutputStream zipOut =
        new ZipOutputStream(new FileOutputStream(zipFile))) {

      for (Map.Entry<String, PostInfo> entry : nameStreamMap.entrySet()) {
        String name = entry.getKey() + ".txt";
        ZonedDateTime createdAt = entry.getValue().createdAt;
        InputStream inputStream = entry.getValue().inputStream;

        FileTime fileTime = FileTime.from(createdAt.toInstant());
        ZipEntry zipEntry = new ZipEntry(name)
            .setLastModifiedTime(fileTime)
            .setLastAccessTime(fileTime)
            .setCreationTime(fileTime);

        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = inputStream.read(bytes)) >= 0) {
          zipOut.write(bytes, 0, length);
        }
        inputStream.close();
      }
    }

    return zipFile;
  }

  private static PostInfo getPostInfo(PostDTO post) {
    String title = post.getTitle();
    String content = post.getContent();
    List<String> tags = post.getTags();
    ZonedDateTime createdAt = Optional.ofNullable(post.getCreatedAt())
        .orElseGet(ZonedDateTime::now)
        .withZoneSameInstant(ZoneId.systemDefault());

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(title);
    stringBuilder.append("\n\n");
    stringBuilder.append(content);
    if (CollectionUtils.isNotEmpty(tags)) {
      stringBuilder.append("\n\n");
      stringBuilder.append(createTagString(tags));
    }
    stringBuilder.append("\n\n");
    stringBuilder.append(createdAt.format(DATE_FORMAT));

    return new PostInfo(createdAt,
        new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)));
  }

  private static String createTagString(List<String> tags) {
    if (CollectionUtils.isEmpty(tags)) {
      return StringUtils.EMPTY;
    }

    return tags.stream()
        .map(s -> "#" + s)
        .collect(Collectors.joining(","));
  }

  private static String nowString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return ZonedDateTime.now().format(formatter);
  }

  private void createWorkspaceIfNotExist() throws IOException {
    Files.createDirectories(Paths.get(workspace));
  }

  public void clear(File file) {
    if (!file.exists()) {
      return;
    }
    if (!file.delete()) {
      log.error("file deletion failed. file=" + file.getName());
    }
  }

  private static class PostInfo {

    ZonedDateTime createdAt;
    InputStream inputStream;

    public PostInfo(ZonedDateTime createdAt, InputStream inputStream) {
      this.createdAt = createdAt;
      this.inputStream = inputStream;
    }
  }
}
