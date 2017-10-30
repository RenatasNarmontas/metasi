package com.renosoftware.metasi.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class FileSystemStorageServiceTests {

  private StorageProperties properties = new StorageProperties();
  private FileSystemStorageService service;

  @Before
  public void init() {
    properties.setUploadLocation("target/files/" + Math.abs(new Random().nextLong()));
    service = new FileSystemStorageService(properties);
    service.init();
  }

  @Test
  public void loadNonExistent() {
    assertThat(service.loadUpload("foo.txt")).doesNotExist();
  }

  @Test
  public void saveAndLoad() {
    service.store(new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
        "Hello World".getBytes()));
    assertThat(service.loadUpload("foo.txt")).exists();
  }

  @Test
  public void savePermitted() {
    service.store(new MockMultipartFile("foo", "bar/../foo.txt",
        MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    assertThat(service.loadUpload("foo.txt")).exists();
  }

}
