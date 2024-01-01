package com.omrispector.spelling;

import com.omrispector.spelling.implementations.SpellChecker;
import com.omrispector.spelling.interfaces.InputReader;

public class SpellingHelper {
  private final InputReader wordFetcher;
  private final InputReader qualityAsker;
  private final SpellChecker spellChecker;


  public SpellingHelper(InputReader wordFetcher, InputReader qualityAsker, SpellChecker spellChecker) {
    this.qualityAsker = qualityAsker;
    this.wordFetcher = wordFetcher;
    this.spellChecker = spellChecker;
  }

  public void go() {
    for (String word=wordFetcher.read(); !"q".equals(word);word=wordFetcher.read()) {
      String suggest = spellChecker.correct(word);
      if (suggest!=null && !suggest.equalsIgnoreCase(word)) {
        String qualityIndication = qualityAsker.read(suggest);
        if (!"y".equals(qualityIndication)) {
          spellChecker.addWord(qualityIndication);
          System.out.println("Got it.\n");
        }
      } else {
        System.out.println("Nice word.\n");
      }
    }
  }
}
