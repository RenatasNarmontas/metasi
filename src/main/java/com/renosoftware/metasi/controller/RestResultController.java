package com.renosoftware.metasi.controller;

import com.renosoftware.metasi.domain.WordCount;
import com.renosoftware.metasi.storage.StorageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application REST controller
 */
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestResultController {

  private final StorageService storageService;

  /**
   * Fetch first result file from storage
   *
   * @return WordCount list as JSON
   */
  @PostMapping("/rest/1")
  public List<WordCount> listFirstResult() {
    return storageService.getResultWordList("rez1.txt");
  }

  /**
   * Fetch second result file from storage
   *
   * @return WordCount list as JSON
   */
  @PostMapping("/rest/2")
  public List<WordCount> listSecondResult() {
    return storageService.getResultWordList("rez2.txt");
  }

  /**
   * Fetch third result file from storage
   *
   * @return WordCount list as JSON
   */
  @PostMapping("/rest/3")
  public List<WordCount> listThirdResult() {
    return storageService.getResultWordList("rez3.txt");
  }

  /**
   * Fetch fourth result file from storage
   *
   * @return WordCount list as JSON
   */
  @PostMapping("/rest/4")
  public List<WordCount> listFourthResult() {
    return storageService.getResultWordList("rez4.txt");
  }

}
