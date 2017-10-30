package com.renosoftware.metasi.storage;

/**
 * Custom StorageException class
 */
public class StorageException extends RuntimeException {

  /**
   * StorageException constructor with message
   * @param message exception message
   */
  public StorageException(String message) {
    super(message);
  }

  /**
   * StorageException constructor with message and cause
   * @param message exception message
   * @param cause exception cause
   */
  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
