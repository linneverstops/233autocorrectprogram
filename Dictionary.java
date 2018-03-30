/** 
 * EECS233 WrittenHW3
 * Tung Ho Lin
 * Spelling Checker: Dictionary
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

//using a built in hashtable to implement the words database
/**
 * The Dictionary text file has to be written in: one word one line format 
 * for the build function to work
 */ 
public class Dictionary {
  
  private Hashtable<String, String> dict;
  
  public Dictionary() {
    dict = new Hashtable<String, String>();
  }
  
  public void addWord(String input) {
    String word = input.toLowerCase();
    word = word.replaceAll("\\s*\\p{Punct}+\\s*$", "");  //remove all whitespaces and punctuation at the end of the word
    if(findWord(word))
      return;
    else
      dict.put(word, word);
  }
  
  public boolean findWord(String input) {
    String word = input.toLowerCase();
    return dict.contains(word);
  }
  
 //limitation of this method, see top
  public void build(String inputfile) {
    try {
    BufferedReader reader = new BufferedReader(new FileReader(inputfile));
    String word;
    while((word = reader.readLine()) != null)
      addWord(word);
    reader.close();
    }
    catch (IOException e) {
      System.err.println("File not found!");
    }
  }
}