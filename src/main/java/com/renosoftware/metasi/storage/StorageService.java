package com.renosoftware.metasi.storage;

import com.renosoftware.metasi.domain.WordCount;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * StorageService interface
 */
public interface StorageService {

  /**
   * Initialize storage if needed
   */
  void init();

  /**
   * Store file
   * @param file file to store
   */
  void store(MultipartFile file);

  /**
   * Return a Stream that is populated with Path by walking upload location
   * @return the Stream of Path
   */
  Stream<Path> getUploadFiles();

  /**
   * Return a Stream that is populated with Path by walking download location
   * @return the Stream of Path
   */
  Stream<Path> getDownloadFiles();

  /**
   * Converts a given upload path string to a Path
   * @param filename the path string to resolve against this path
   * @return the resulting path
   */
  Path loadUpload(String filename);

  /**
   * Converts a given download path string to a Path
   * @param filename the path string to resolve against this path
   * @return the resulting path
   */
  Path loadDownload(String filename);

  /**
   * Loads as resource uploaded file
   * @param filename filename to load
   * @return loaded resource
   */
  Resource loadAsResourceUpload(String filename);

  /**
   * Loads as Resource downloaded file
   * @param filename filename to load
   * @return loaded resource
   */
  Resource loadAsResourceDownload(String filename);

  /**
   * Delete uploaded and downloaded files
   */
  void deleteAll();

  /**
   * Get uploaded file
   * @return Returns a File object representing this path
   */
  File getUploadedFile();

  /**
   * Read file to WordCount list and return it
   * @param filename filename to read
   * @return WordCount list
   */
  List<WordCount> getResultWordList(String filename);

  /**
   * Process uploaded files
   */
  void process();

}
