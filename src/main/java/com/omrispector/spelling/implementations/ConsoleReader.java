package com.omrispector.spelling.implementations;
import com.omrispector.spelling.interfaces.InputReader;

import java.util.Scanner;

public class ConsoleReader implements InputReader {
  private final Scanner scanner;
  private final String prompt;

  public ConsoleReader(String prompt) {
    this.prompt = prompt;
    this.scanner = new Scanner(System.in);
  }

  @Override
  public String read() {
    System.out.print(prompt);
    return scanner.nextLine();
  }

  @Override
  public String read(String promptParam) {
    System.out.println(String.format(prompt,promptParam));
    return scanner.nextLine();
  }
}