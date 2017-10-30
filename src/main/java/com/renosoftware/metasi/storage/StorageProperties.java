package com.renosoftware.metasi.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * StorageProperties class
 */
@ConfigurationProperties("storage")
public class StorageProperties {

  /**
   * Folder location for upload files
   */
  @Setter
  @Getter
  private String uploadLocation = "upload";

  /**
   * Folder location for download files
   */
  @Setter
  @Getter
  private String downloadLocation = "download";

}
