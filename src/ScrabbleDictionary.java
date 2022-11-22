import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ScrabbleDictionary {

    private static final String path="resources/Words";

    private ArrayList<String> dictionary;
    private HashMap<HashMap, String> mappedWords;
    public ScrabbleDictionary() {
        dictionary = new ArrayList<>();
        mappedWords = new HashMap<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                dictionary.add(data);
                HashMap<String,Integer> letterCount = countLetters(data);
                mappedWords.put(letterCount, data);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public boolean isWord(String word){
        word = word.toLowerCase();
        word = word.replaceAll(" ", "");
        System.out.println("In dictionary, checking " + word);
        return dictionary.contains(word);
    }
    private HashMap<String, Integer> countLetters(String word){
        word = word.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        for(Character c: word.toCharArray()){
            int count = map.containsKey(c+"")?map.get(c+""):0;
            map.put(c+"", count+1);
        }
        return map;
    }
    public HashMap<HashMap, String> getMappedWords() {
        return mappedWords;
    }
    public ArrayList<String> getDictionary(){
        return this.dictionary;
    }

}
