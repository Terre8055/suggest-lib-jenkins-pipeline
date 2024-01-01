package com.omrispector.spelling.implementations;

import com.omrispector.spelling.interfaces.SpellingDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Based on Peter Norvigs http://norvig.com/spell-correct.html
 * Adapted for use as an explanation of the merits and flavours of Dependency Injection
 */
public class SpellChecker {
  private final SpellingDB spellingDB;


  public SpellChecker(SpellingDB spellingDB) {
    this.spellingDB = spellingDB;
  }

  private final ArrayList<String> edits(String word) {
    ArrayList<String> result = new ArrayList<String>();
    for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
    for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
    for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
    for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
    return result;
  }

  public final String correct(String word) {
    if(spellingDB.wordExists(word)) return null;
    ArrayList<String> list = edits(word);
    HashMap<Integer, String> candidates = new HashMap<Integer, String>();
    for(String s : list) if(spellingDB.wordExists(s)) candidates.put(spellingDB.getWordPopularity(s),s);
    if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
    for(String s : list) for(String w : edits(s)) if(spellingDB.wordExists(w)) candidates.put(spellingDB.getWordPopularity(w),w);
    return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
  }

  public void addWord(String w) {
    spellingDB.addWord(w);
  }
}