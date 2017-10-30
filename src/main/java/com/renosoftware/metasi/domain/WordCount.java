package com.renosoftware.metasi.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WordCount entity
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WordCount {

  @Setter
  @Getter
  private String word;

  @Setter
  @Getter
  private Integer count;

  /**
   * Constructor
   *
   * @param word initialize
   */
  public WordCount(String word) {
    this.word = word;
    this.count = 1;
  }

  /**
   * Increment count
   */
  public void incrementCount() {
    this.count = this.count + 1;
  }

  /**
   * Return formatted word for writing to result file
   *
   * @return formatted string
   */
  public String toFormattedString() {
    return word + "," + count;
  }

  /**
   * Indicates whether object is "equal to" this one
   *
   * @param obj Object to oompare
   * @return equality decision
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (!(obj instanceof WordCount))
      return false;

    WordCount wordCount = (WordCount) obj;
    if (this.word.equals(wordCount.getWord()))
      return true;

    return false;
  }

  /**
   * Hash code
   *
   * @return computed hash code
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getWord() == null) ? 0 : getWord().hashCode());
    //result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
    return result;
  }
}
