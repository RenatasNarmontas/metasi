package com.renosoftware.metasi.storage;

import com.renosoftware.metasi.domain.WordCount;
import com.renosoftware.metasi.service.CallableWorker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * StorageService implementation based on file system
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

  private final Path uploadLocation;
  private final Path downloadLocation;
  private List<WordCount> wordList = new CopyOnWriteArrayList<WordCount>();

  @Autowired
  ThreadPoolTaskExecutor threadPool;

  /**
   * Injects storage properties
   *
   * @param properties StorageProperties to inject
   */
  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.uploadLocation = Paths.get(properties.getUploadLocation());
    this.downloadLocation = Paths.get(properties.getDownloadLocation());
  }

  /**
   * Creates upload and download location directories
   */
  @Override
  public void init() {
    try {
      Files.createDirectories(uploadLocation);
      Files.createDirectories(downloadLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  /**
   * Stores MultipartFile to upload location
   *
   * @param file MultipartFile to store
   */
  @Override
  public void store(MultipartFile file) {
    String filename = StringUtils.getFilename(StringUtils.cleanPath(file.getOriginalFilename()));
    log.info("Uploading file: " + filename);
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // Security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory " + filename);
      }
      Files.copy(file.getInputStream(), this.uploadLocation.resolve(filename),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }

  /**
   * Return a Stream that is populated with Path by walking upload location
   *
   * @return the Stream of Path
   */
  @Override
  public Stream<Path> getUploadFiles() {
    try {
      return Files.walk(this.uploadLocation, 1)
          .filter(path -> !path.equals(this.uploadLocation))
          .map(path -> this.uploadLocation.relativize(path));
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  /**
   * Return a Stream that is populated with Path by walking download location
   *
   * @return the Stream of Path
   */
  @Override
  public Stream<Path> getDownloadFiles() {
    try {
      return Files.walk(this.downloadLocation, 1)
          .filter(path -> !path.equals(this.downloadLocation))
          .map(path -> this.downloadLocation.relativize(path));
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  /**
   * Converts a given upload path string to a Path
   *
   * @param filename the path string to resolve against this path
   * @return the resulting path
   */
  @Override
  public Path loadUpload(String filename) {
    return uploadLocation.resolve(filename);
  }

  /**
   * Converts a given download path string to a Path
   *
   * @param filename the path string to resolve against this path
   * @return the resulting path
   */
  @Override
  public Path loadDownload(String filename) {
    return downloadLocation.resolve(filename);
  }

  /**
   * Loads as resource uploaded file
   *
   * @param filename filename to load
   * @return loaded resource
   */
  @Override
  public Resource loadAsResourceUpload(String filename) {
    try {
      Path file = loadUpload(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  /**
   * Loads as Resource downloaded file
   *
   * @param filename filename to load
   * @return loaded resource
   */
  @Override
  public Resource loadAsResourceDownload(String filename) {
    try {
      Path file = loadDownload(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  /**
   * Delete files from upload and download directories
   */
  @Override
  public void deleteAll() {
    log.info("Deleting files in: " + uploadLocation.toFile().getAbsoluteFile().toString());
    cleanUploadDirectory(uploadLocation.toFile());

    log.info("Deleting files in: " + downloadLocation.toFile().getAbsoluteFile().toString());
    cleanDownloadDirectory(downloadLocation.toFile());
  }

  /**
   * Delete files from upload directory
   *
   * @param directory Delete files in directory
   */
  private void cleanUploadDirectory(File directory) {
    try {
      for (File file : directory.listFiles()) {
        log.trace("Deleting file: " + file.getName());
        if (!file.delete()) {
          log.error("Failed to delete file: " + file.getAbsoluteFile());
        };
      }
    } catch (NullPointerException npe) {
      log.error(npe.getMessage());
    }
  }

  /**
   * Delete files from download directory
   *
   * @param directory Delete files in directory
   */
  private void cleanDownloadDirectory(File directory) {
    try {
      for (File file : directory.listFiles()) {
        log.trace("Deleting file: " + file.getName());
        if (!file.delete()) {
          log.error("Failed to delete file: " + file.getAbsoluteFile());
        };
      }
    } catch (NullPointerException npe) {
      log.error(npe.getMessage());
    }
  }

  /**
   * Get uploaded file
   *
   * @return Returns a File object representing this path
   */
  @Override
  public File getUploadedFile() {
    return uploadLocation.toFile();
  }

  /**
   * Read csv file to WordCount list and return it
   *
   * @param fname filename to read
   * @return WordCount list
   */
  @Override
  public List<WordCount> getResultWordList(String fname) {
    List<WordCount> wordCountList = new ArrayList<WordCount>();

    String filename = downloadLocation.toFile().getAbsoluteFile() + File.separator + fname;
    log.info("Reading result filename: " + filename);
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filename));
      String[] lineWords;

      for (String line; (line = br.readLine()) != null; ) {
        lineWords = line.trim().split(","); //split words by space, tab, new line, etc

        wordCountList.add(new WordCount(lineWords[0], Integer.parseInt(lineWords[1])));
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          log.error(e.getMessage());
        }
      }
    }
    return wordCountList;
  }

  @Override
  public void process() {
    log.info("Processing uploaded files");

    try {
      for (File file : getUploadedFile().listFiles()) {
        CallableWorker callableWorker = new CallableWorker(file.getAbsolutePath(), wordList);
        threadPool.submit(callableWorker);
      }
    } catch (NullPointerException npe) {
      log.error(npe.getMessage());
    }

    while (true) {
      if (threadPool.getActiveCount() == 0) {
        break;
      } else {
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }

    // export result to files
    printWordList();
  }

  /**
   * Helper method to print data to files
   */
  private void printWordList() {
    log.info("Exporting words to files");

    try {
      // TODO: Hardcoded file names
      PrintWriter f1 = new PrintWriter(
          new FileWriter(downloadLocation.toFile().getAbsoluteFile() + File.separator + "rez1.txt",
              false));
      PrintWriter f2 = new PrintWriter(
          new FileWriter(downloadLocation.toFile().getAbsoluteFile() + File.separator + "rez2.txt",
              false));
      PrintWriter f3 = new PrintWriter(
          new FileWriter(downloadLocation.toFile().getAbsoluteFile() + File.separator + "rez3.txt",
              false));
      PrintWriter f4 = new PrintWriter(
          new FileWriter(downloadLocation.toFile().getAbsoluteFile() + File.separator + "rez4.txt",
              false));
      PrintWriter f5 = new PrintWriter(new FileWriter(
          downloadLocation.toFile().getAbsoluteFile() + File.separator + "uncategorized.txt",
          false));

      // TODO: Hardcoded patterns
      Pattern p1 = Pattern.compile("^[A-G]", Pattern.CASE_INSENSITIVE);
      Pattern p2 = Pattern.compile("^[H-N]", Pattern.CASE_INSENSITIVE);
      Pattern p3 = Pattern.compile("^[O-U]", Pattern.CASE_INSENSITIVE);
      Pattern p4 = Pattern.compile("^[V-Z]", Pattern.CASE_INSENSITIVE);

      for (WordCount wordCount : wordList) {
        String word = wordCount.getWord();
        Matcher matcher = p1.matcher(word);

        // Decide which file to write
        if (matcher.find()) {
          f1.println(wordCount.toFormattedString());
        } else {
          matcher = p2.matcher(word);
          if (matcher.find()) {
            f2.println(wordCount.toFormattedString());
          } else {
            matcher = p3.matcher(word);
            if (matcher.find()) {
              f3.println(wordCount.toFormattedString());
            } else {
              matcher = p4.matcher(word);
              if (matcher.find()) {
                f4.println(wordCount.toFormattedString());
              } else {
                // TODO: where do I have to put this word?
                f5.println(wordCount.toFormattedString());
              }
            }
          }
        }
      }
      f1.close();
      f2.close();
      f3.close();
      f4.close();
      f5.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

}
