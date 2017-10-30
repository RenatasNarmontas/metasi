package com.renosoftware.metasi.service;

import com.renosoftware.metasi.domain.WordCount;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CallableWorker class
 */
@AllArgsConstructor
@Slf4j
public class CallableWorker implements Callable<Void> {

  private String filename;
  private List<WordCount> wordList;

  /**
   * Worker entry method
   * @return nothing
   * @throws Exception
   */
  @Override
  public Void call() throws Exception {
    log.trace("Thread started for " + filename);
    parseFile();
    log.trace("Thread finished for " + filename);
    return null;
  }

  /**
   * Parse file and populate WordCount list
   */
  private void parseFile() {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filename));
      String[] splittedWords;

      for (String line; (line = br.readLine()) != null; ) {
        splittedWords = line.trim().split("\\s+"); //split words by space, tab, new line, etc

        for (String word : splittedWords) {
          log.trace("Analyzing word: " + word);
          WordCount wordCount = new WordCount(word);

          if (wordList.contains(wordCount)) {
            // Word exists in the list. Incrementing count
            log.trace("Found word: " + wordCount.getWord());
            wordList.get(wordList.indexOf(wordCount)).incrementCount();
            log.trace(wordCount.toString());
          } else {
            // Word doesn't exist in the list. Adding it
            log.trace("Not found word: " + wordCount.getWord());
            wordList.add(wordCount);
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    } finally {
      if (br != null) {
        try { br.close(); } catch (IOException e) { log.error(e.getMessage()); }
      }
    }
  }
}
