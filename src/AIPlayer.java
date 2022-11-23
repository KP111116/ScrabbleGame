import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class AIPlayer extends BoardModel {
    Character[][] matrix;
    private TileScoreManager tileScoreManager = new TileScoreManager();
    BoardModel bm;
    Map<Character, Integer> letterCount;
    HashMap<Point, HashMap<String, Boolean>> generatedGuesses;

    ScrabbleDictionary dictionary = new ScrabbleDictionary();
    ArrayList<PlacedWords> pAsHorizontal, pAsVertical; //prefixes and suffixes
    Tray tray;

    public AIPlayer(BoardModel bm,Character[][] matrix, Tray tray) {
        super();
        this.matrix = matrix;
        this.tray = tray;
        this.bm = bm;
        this.letterCount = this.countLetters(tray.getTray());
        generatedGuesses = new HashMap<>();
        this.findPrefixAndSuffix();
    }

    private HashMap<Character, Integer> countLetters(ArrayList<Character> tray) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (Character s : tray) {
            int count = map.containsKey(s) ? map.get(s) : 0;
            map.put(s, count + 1);
        }
        return map;
    }

    public void guessHorizontal() {
        for(PlacedWords word: this.pAsHorizontal){
            Point leftLimit = this.checkLeft(word.getStart());
            Point rightLimit = this.checkRight(word.getEnd());
            int leftLength = word.getStartX() - (int)leftLimit.getX();
            int rightLength = (int)rightLimit.getX() - word.getEndX();
            this.generatePlayableMoves(word, leftLength, rightLength, true);
            System.out.println(generatedGuesses);
        }
    }
    public void guessVertical(){
        for(PlacedWords word: this.pAsVertical){
            Point topLimit = this.checkUp(word.getStart());
            Point downLimit = this.checkDown(word.getEnd());
            int topLength = word.getStartY() - (int) topLimit.getY();
            int downLength = (int)downLimit.getY() - word.getEndY();
            System.out.println(topLength + " ---  " + downLength);
            System.out.println(word.getWord() + " from " + word.getStart() + " to " + word.getEnd());
            this.generatePlayableMoves(word, topLength, downLength,false);
            System.out.println(this.generatedGuesses);
        }
    }

    private void generatePlayableMoves(PlacedWords word, int lengthBeforeWord, int lengthAfterWord, boolean isHorizontal) {
        ArrayList<String> wordsWithoutCurrentSuffix= this.removeSuffixFromDictionaryWords(word.getWord(), lengthBeforeWord);
        ArrayList<String> wordsWithoutCurrentPrefix = this.removePrefixFromDictionaryWords(word.getWord(), lengthAfterWord);
        if(!wordsWithoutCurrentSuffix.isEmpty()){
            generatedGuesses.put(word.getStart(), this.generateGuesses(wordsWithoutCurrentSuffix,isHorizontal));
        }
        if(!wordsWithoutCurrentPrefix.isEmpty()) {
            generatedGuesses.put(word.getEnd(), this.generateGuesses(wordsWithoutCurrentPrefix,isHorizontal));
        }
    }

    private ArrayList<String> removeSuffixFromDictionaryWords(String suffix, int length){
        ArrayList<String> words = new ArrayList<>();
        for(String d: this.dictionary.getDictionary()){
            if(d.endsWith(suffix)){
                if(d.length() > suffix.length()+1){
                    String word = d.substring(0,d.length() - suffix.length());
                    if(word.length() <= length) {
                        words.add(word);
                    }
                }
            }
        }
        return words;
    }
    private ArrayList<String> removePrefixFromDictionaryWords(String prefix, int length){
        ArrayList<String> words = new ArrayList<>();
        for(String d: this.dictionary.getDictionary()){
            if(d.startsWith(prefix)){
                if(d.length() > prefix.length()){
                    String word = d.substring(prefix.length());
                    if(word.length() <= length){
                        words.add(word);
                    }
                }
            }
        }
        return words;
    }
    private Point checkUp(Point p) {
        if (p.getY() == 0) {
            return p;
        }else if(this.matrix[(int) p.getY()-1][(int) p.getX()] == ' '){
            return new Point((int)p.getX(),(int) p.getY()-1);
        } else {
            return this.checkUp(new Point((int) p.getX(), (int) p.getY()));
        }
    }
    private Point checkDown(Point p) {
        if (p.getY() == 14) {
            return p;
        }else if(this.matrix[(int) p.getY()+1][(int) p.getX()] == ' '){
            return this.checkDown(new Point((int) p.getX(), (int) p.getY() + 1));
        } else {
            return new Point((int)p.getX() ,(int) p.getY());
        }
    }
    private Point checkLeft(Point p) {
        if (p.getX() == 0) {
            return p;
        }else if(this.matrix[(int) p.getY()][(int) p.getX()-1] == ' '){
            return this.checkLeft(new Point((int) p.getX() - 1, (int) p.getY()));
        } else {
            return new Point((int)p.getX(),(int) p.getY());
        }
    }
    private Point checkRight(Point p) {
        if (p.getX() == 14) {
            return p;
        }else if(this.matrix[(int) p.getY()][(int) p.getX()+1] == ' '){
            return this.checkRight(new Point((int) p.getX() + 1, (int) p.getY()));
        } else {
            return new Point((int)p.getX(),(int) p.getY());
        }
    }
    private HashMap<Character, Integer> countLetters(String word) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (Character s : word.toCharArray()) {
            int count = !map.containsKey(s) ? 0 : map.get(s);
            map.put(s, count + 1);
        }
        return map;
    }

    //this method checks if the number of alphabets are same or not,
    // between the tray and the words passed in wordsToCheck
    public HashMap<String,Boolean> generateGuesses(ArrayList<String> wordsToCheck, boolean isHorizontal) {
        HashMap<String,Boolean> guessedWords = new HashMap<>();
        for (String word : wordsToCheck) {
            Map<Character, Integer> currentWordMap = this.countLetters(word);
            boolean flag = true;
            for (Character s : currentWordMap.keySet()) {
                int characterCount = this.letterCount.containsKey(s) ? this.letterCount.get(s) : 0;
                int mapCharacterCount = currentWordMap.get(s);
                if (characterCount < mapCharacterCount) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                guessedWords.put(word, isHorizontal);
            }
        }
        return guessedWords;
    }
    private void findPrefixAndSuffix(){
        ArrayList<PlacedWords> horizontalWords = new ArrayList<>();
        ArrayList<PlacedWords> verticalWords = new ArrayList<>();
        Point startPointH = null, endPointH = null;
        Point startPointV = null, endPointV = null;
        String horizontalWord = "", verticalWord = "";
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Character c = this.matrix[i][j];
                switch (c){
                    case ' ':
                        if (horizontalWord != "") {
                            endPointH = new Point(j - 1, i);
                            horizontalWords.add(new PlacedWords(horizontalWord, startPointH, endPointH));
                            horizontalWord = "";
                            endPointH = startPointH = null;
                        }
                        break;
                    default:
                        if(j==14){
                            if(startPointH == null){
                                startPointH = new Point(14,i);
                            }
                            horizontalWords.add(new PlacedWords(c.toString(), startPointH, new Point(j,i)));
                            horizontalWord = "";
                            endPointH = startPointH = null;
                            break;
                        }
                        if(startPointH == null){
                            horizontalWord += c;
                            startPointH = new Point(j,i);
                            break;
                        }if(horizontalWord!=""){
                            horizontalWord+=c;
                            break;
                        }
                }
            }
        }
        verticalWord = "";
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(this.matrix[j][i]!=' '){
                    if(startPointV == null){
                        startPointV= new Point(i, j);
                    }
                    verticalWord+= this.matrix[j][i];
                }
                if(j==14&& startPointV !=null){
                    endPointV = new Point(i, 14);
                    verticalWords.add(new PlacedWords(verticalWord, startPointV,endPointV));
                    startPointV = null;
                    endPointV = null;
                    verticalWord = "";
                }else if(this.matrix[j][i] == ' ' && startPointV != null) {
                    endPointV = new Point(i,j-1);
                    verticalWords.add(new PlacedWords(verticalWord,startPointV,endPointV));
                    startPointV = null;
                    endPointV = null;
                    verticalWord = "";
                }
            }
        }
        this.pAsHorizontal = horizontalWords;
        this.pAsVertical = verticalWords;
    }
    public void printMatrix() {
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                for (int k = 0; k < 15; k++) {
                    if (k == 0) {
                        System.out.print("\t");
                    }
                    System.out.print("\t" + k);
                }
                System.out.print("\n\n\n");
            }
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print(i + "\t");
                }
                System.out.print("\t" + matrix[i][j] + "");
                if (j + 1 == 15) {
                    System.out.print("\n");
                }
            }
        }
    }
    public static void main(String[] args) {
        Bag b = new Bag();
        Character[][] matrix = new Character[15][15];
        for(int i = 0; i < 15;i++){
            for(int j = 0; j < 15; j++){
                matrix[i][j] = ' ';
            }
        }
        matrix[2][0] = 'g';
        matrix[2][1]='o';
        matrix[2][3] = 'o';
        matrix[2][4]='f';
        matrix[3][0] = 'c';
        matrix[3][1] = 'f';
        ArrayList<Character> tray = new ArrayList<>();
        tray.addAll(List.of('d','t','v','z','f','a'));
        AIPlayer p = new AIPlayer(new BoardModel(), matrix, new Tray(tray, b));
        p.printMatrix();
        p.guessHorizontal();
        p.guessVertical();

    }

}
