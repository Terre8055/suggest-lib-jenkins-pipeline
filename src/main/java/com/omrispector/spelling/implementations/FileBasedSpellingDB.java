package com.omrispector.spelling.implementations;

import com.omrispector.spelling.SpellingHelper;
import com.omrispector.spelling.interfaces.SpellingDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileBasedSpellingDB implements SpellingDB {
  private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();

  public FileBasedSpellingDB(String trainingCorpusUrl) {
    try {
      BufferedReader trainingCorpus = new BufferedReader(new InputStreamReader(SpellingHelper.class.getResourceAsStream(trainingCorpusUrl)));
      init(trainingCorpus);
      trainingCorpus.close();
    } catch (Exception e) {
      throw new RuntimeException("Failed loading training data",e);
    }
  }

  private void init(BufferedReader trainingCorpus) throws IOException {
    Pattern p = Pattern.compile("\\w+");
    for(String temp = ""; temp != null; temp = trainingCorpus.readLine()){
      Matcher m = p.matcher(temp.toLowerCase());
      while(m.find()) addWord(m.group());
    }

  }

  @Override
  public void addWord(String w) {
    String word = w.toLowerCase();
    nWords.put(word, nWords.containsKey(word) ? nWords.get(word) + 1 : 1);
  }

  @Override
  public boolean wordExists(String s) {
    return nWords.containsKey(s.toLowerCase());
  }

  @Override
  public Integer getWordPopularity(String s) {
    return nWords.get(s.toLowerCase());
  }
}
