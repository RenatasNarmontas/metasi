package com.renosoftware.metasi.storage;

/**
 * Custom StorageFileNotFoundException class
 */
public class StorageFileNotFoundException extends StorageException {

  /**
   * StorageFileNotFoundException with message
   *
   * @param message exception message
   */
  public StorageFileNotFoundException(String message) {
    super(message);
  }

  /**
   * StorageFileNotFoundException with message and cause
   *
   * @param message exception message
   * @param cause exception cause
   */
  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
