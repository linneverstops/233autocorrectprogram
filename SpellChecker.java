/** 
 * EECS233 WrittenHW3
 * Tung Ho Lin
 * Spelling Checker: SpellChecker
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

//D:\\School Documents\\EECS233\\HW5\\input2.txt
public class SpellChecker {
  
  private String inputfile;
  
  private Dictionary dict;  
  
  public SpellChecker(String main, String personal, String input) {
    this.inputfile = input;
    dict = new Dictionary();
    dict.build(main);
    dict.build(personal);
  }
  
  //main method
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Please enter the path of the main dictionary file.");
    String main = sc.nextLine();
    System.out.println("Please enter the path of the personal dictionary file.");
    String personal = sc.nextLine();
    System.out.println("Please enter the path of the text file to be spell checked.");
    String input = sc.nextLine();
    SpellChecker checker = new SpellChecker(main, personal, input);
    checker.spellCheck();
  }
  
  //method to spell check and print out the errors
  public void spellCheck() {
    try {
    Scanner linesc = new Scanner(new File(inputfile));
    int linenumber = 0;
    String line;
    String word;
    while(linesc.hasNextLine()) {
      line = linesc.nextLine();   //scan a whole line of text
      linenumber++;  //increment line number
      Scanner wordsc = new Scanner(line);
      while(wordsc.hasNext()) {
        word = wordsc.next().toLowerCase();  //scan a word from the line of text
        word = word.replaceAll("\\s*\\p{Punct}+\\s*$", "");
        if(dict.findWord(word) == false) {  //if the word is not found in the built dictionary
          System.out.println(word + " : in Line " + linenumber + " is not spelled correctly.");
          System.out.println(appendSuggestions(word) + "\n");
        }
      }
      wordsc.close();
    }
    linesc.close();
    System.out.println("Spellcheck completed!");
    }
    catch (FileNotFoundException e) {
      System.err.println("File not found!");
    }
  }
  
  //collect all the suggestions from the 3 methods and delete identical suggestions
  public String appendSuggestions(String input) {
    ArrayList<String> add = addChar(input);
    ArrayList<String> remove = removeChar(input);
    ArrayList<String> swap = swapChar(input);
    ArrayList<String> suggestions = new ArrayList<String>();
    String output = "Suggestions: ";
    for(int i=0; i<add.size(); i++)  //add all suggestions from first method
      suggestions.add(add.get(i));
    for(int i=0; i<remove.size(); i++) {  //add non-recurrent suggestions
      if(suggestions.contains(remove.get(i)) == false)
        suggestions.add(remove.get(i));
    }
    for(int i=0; i<swap.size(); i++) {  //add non-recurrent suggestions
      if(suggestions.contains(swap.get(i)) == false)
        suggestions.add(swap.get(i));
    }
    for(int i=0; i<suggestions.size()-1; i++) {  //append a String of suggestions
      output += suggestions.get(i) + ", ";  //do not print out ", " on the last word
    }
    try{
    output += suggestions.get(suggestions.size()-1);  //deal with the last word in the list
    }
    catch(ArrayIndexOutOfBoundsException e){
      System.err.println("Suggestions cannot be generated by the built-in methods, Sorry!");  //if the misspelled word cannot be fixed by the 3 methods
      output += "N/A";
    }
    return output;
  }
  
  //create suggestions by adding a character to anywhere in the word each time
  public ArrayList<String> addChar(String input) {
    ArrayList<String> suggestions = new ArrayList<String>();
    char[] alphabets = new char[26];
    for(int i=0; i<alphabets.length; i++)  //create an array of all 26 alphabets
      alphabets[i] = (char)('a'+ i);
    for(char c : alphabets) {  //for each alphabet to be inserted in the word
      for(int i=0; i<=input.length(); i++) {  //for each space in the word to be inserted
        String suggest = input.substring(0, i) + c + input.substring(i, input.length()); //insert the character between each adjacent characters
        if(dict.findWord(suggest))  //check if it is a correct word
          suggestions.add(suggest);
      }
    }
    return suggestions;
  }
  
  //create suggestions by removing a character from anywhere in the word each time
  public ArrayList<String> removeChar(String input) {
    ArrayList<String> suggestions = new ArrayList<String>();
    for(int i=0; i<input.length(); i++) {
      StringBuilder builder = new StringBuilder(input);
      builder.deleteCharAt(i);  //delete one character each time
      String suggest = builder.toString();
      if(dict.findWord(suggest))  //compare to the dictionary if it is a correct word
        suggestions.add(suggest);
    }
    return suggestions;
  }
  
  //create suggestions by swapping 2 characters in a word
  public ArrayList<String> swapChar(String input) {
    ArrayList<String> suggestions = new ArrayList<String>();
    for(int i=0; i<input.length()-1; i++) {
      char[] decon = input.toCharArray();
      char swap = decon[i];
      decon[i] = decon[i+1];  //swapping the characters
      decon[i+1] = swap;
      String suggest = new String(decon);  //back to String
      if(dict.findWord(suggest))  //check if it is a correct word
        suggestions.add(suggest);
    }
    return suggestions;
  }
}
  
  
    
    
    