package com.renosoftware.metasi.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class WordCountTest {

  private WordCount wordCount;

  @Before
  public void setUp() throws Exception {
    wordCount = new WordCount();
  }

  @Test
  public void getWord() throws Exception {
    String word = "test";
    wordCount.setWord(word);
    assertEquals(word, wordCount.getWord());
  }

  @Test
  public void getCount() throws Exception {
    Integer count = 10;
    wordCount.setCount(count);
    assertEquals(count, wordCount.getCount());
  }
}
