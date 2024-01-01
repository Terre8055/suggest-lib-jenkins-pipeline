package com.omrispector.spelling.interfaces;

public interface SpellingDB {
  /**
   * Check if a word is known
   * @param w word to check
   * @return true iff word found in DB
   */
  boolean wordExists(String w);

  /**
   * Add a word to the DB. If word already exists, it's popularity may be enhanced
   * @param w word to add
   */
  void addWord(String w);

  /**
   * Check popularity of a word.
   * @param w word to check
   * @return 0 if word unknown, 1 for lowest popularity, MAX_INTEGER for highest popularity
   */
  Integer getWordPopularity(String w);
}
