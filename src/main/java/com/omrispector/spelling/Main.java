package com.omrispector.spelling;

import com.omrispector.spelling.implementations.ConsoleReader;
import com.omrispector.spelling.implementations.FileBasedSpellingDB;
import com.omrispector.spelling.implementations.SpellChecker;
import com.omrispector.spelling.interfaces.InputReader;
import com.omrispector.spelling.interfaces.SpellingDB;

public class Main {
  public static void main(String[] args) throws Exception {
    InputReader wordFetcher = new ConsoleReader("Enter a word (q to exit):");
    InputReader qualityAsker = new ConsoleReader("Did you mean %s (y to accept, other to teach)?");
    SpellingDB spellingDB = new FileBasedSpellingDB("/somebooks.txt");
    SpellChecker spellChecker = new SpellChecker(spellingDB);
    SpellingHelper spellingHelper = new SpellingHelper(wordFetcher,qualityAsker,spellChecker);

    spellingHelper.go();
  }

}
