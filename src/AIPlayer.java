import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class AIPlayer extends BoardModel implements Serializable {
    Character[][] matrix;
    ArrayList<Move> moves;
    private TileScoreManager tileScoreManager = new TileScoreManager();
    BoardModel bm;
    Map<Character, Integer> letterCount;
    HashMap<Point, HashMap<String, Boolean>> generatedGuesses;
    ScrabbleDictionary dictionary = new ScrabbleDictionary();
    ArrayList<WordToPlace> pAsHorizontal, pAsVertical; //prefixes and suffixes
    Tray tray;

    public AIPlayer(BoardModel bm,Character[][] matrix, Tray tray) {
        this.matrix = matrix;
        this.tray = tray;
        this.bm = bm;
        this.letterCount = this.countLetters(tray.getTray());
        generatedGuesses = new HashMap<>();
        this.findPrefixAndSuffix();
        moves = new ArrayList<>();
    }

    private HashMap<Character, Integer> countLetters(ArrayList<Character> tray) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (Character s : tray) {
            int count = map.containsKey(s) ? map.get(s) : 0;
            map.put(s, count + 1);
        }
        return map;
    }

    public void play(){
        int bestScore = 0;
        Move bestMove = null;
        guessVertical();
        guessHorizontal();
        printMatrix();

        for(Move m : moves){
            bm.wordsCreated.clear();
            if(bm.playMove(m)){
                int score = bm.calculateScore(m);
                if(score > bestScore){
                    bestScore = score;
                    bestMove = m;
                    System.out.println(bestMove.getTileMoves());
                }
            }
        }
        System.out.println(bestMove.getWord());
        System.out.println("score bestest: " + bestScore);
        bm.playMove(bestMove);
        bm.placement(bestMove);
        bm.status(bestMove);
        bm.setAIScore(bestMove);
        bm.addPlayedMove(bestMove);
    }

    public void guessHorizontal() {
        for(WordToPlace word: this.pAsHorizontal){
            Point leftLimit = this.checkLeft(word.getStart());
            Point rightLimit = this.checkRight(word.getEnd());
            int leftLength = word.getStartX() - (int)leftLimit.getX();
            int rightLength = (int)rightLimit.getX() - word.getEndX();
            this.generatePlayableMoves(word, leftLength, rightLength, true);
        }
    }
    public void guessVertical(){
        for(WordToPlace word: this.pAsVertical){
            Point topLimit = this.checkUp(word.getStart());
            Point downLimit = this.checkDown(word.getEnd());
            int topLength = word.getStartY() - (int) topLimit.getY();
            int downLength = (int)downLimit.getY() - word.getEndY();
            this.generatePlayableMoves(word, topLength, downLength,false);
        }
    }

    private void generatePlayableMoves(WordToPlace word, int lengthBeforeWord, int lengthAfterWord, boolean isHorizontal) {
        ArrayList<String> wordsWithoutCurrentSuffix= this.removeSuffixFromDictionaryWords(word.getWord(), lengthBeforeWord);
        ArrayList<String> wordsWithoutCurrentPrefix = this.removePrefixFromDictionaryWords(word.getWord(), lengthAfterWord);
      /*  for(String s: wordsWithoutCurrentPrefix){
            System.out.println("suffix: "+word.getWord() + " : " + s);
        }
        for(String s: wordsWithoutCurrentSuffix){
            System.out.println("prefix: " + s + " : " + word.getWord() );
        }*/
        if(!wordsWithoutCurrentSuffix.isEmpty()){ // these are for making prefixes
            HashMap<String, Boolean> temp = this.generateGuesses(wordsWithoutCurrentSuffix,isHorizontal);

            generatedGuesses.put(word.getStart(), this.generateGuesses(wordsWithoutCurrentSuffix,isHorizontal));
            for(String s: temp.keySet()){
                moves.add(new Move(s,false,word.getStart(),isHorizontal));
            }
        }
        if(!wordsWithoutCurrentPrefix.isEmpty()) {
            HashMap<String, Boolean> temp2 = this.generateGuesses(wordsWithoutCurrentPrefix,isHorizontal);
            generatedGuesses.put(word.getEnd(), this.generateGuesses(wordsWithoutCurrentPrefix,isHorizontal));

            for(String s1: temp2.keySet()){
                moves.add(new Move(s1, true,word.getEnd(),isHorizontal));
            }
        }
    }


    private ArrayList<String> removeSuffixFromDictionaryWords(String suffix, int length){
        ArrayList<String> words = new ArrayList<>();
        for(String d: this.dictionary.getDictionary()){
            if(d.endsWith(suffix)){
                if(d.length() > suffix.length()+1){
                    String word = d.substring(0,d.length() - suffix.length());
                    if(word.length() < length) {
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
                    if(word.length() < length){
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
        ArrayList<WordToPlace> horizontalWords = new ArrayList<>();
        ArrayList<WordToPlace> verticalWords = new ArrayList<>();
        Point startPointH = null, endPointH = null;
        Point startPointV = null, endPointV = null;
        String horizontalWord = "", verticalWord = "";
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Character c = matrix[i][j];
                switch (c){
                    case ' ':
                        if (horizontalWord != "") {
                            endPointH = new Point(j - 1, i);
                            horizontalWords.add(new WordToPlace(horizontalWord, startPointH, endPointH));
                            horizontalWord = "";
                            endPointH = startPointH = null;
                        }
                        break;
                    default:
                        if(j==14){
                            if(startPointH == null){
                                startPointH = new Point(14,i);
                            }
                            horizontalWords.add(new WordToPlace(c.toString(), startPointH, new Point(j,i)));
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
                    verticalWords.add(new WordToPlace(verticalWord, startPointV,endPointV));
                    startPointV = null;
                    endPointV = null;
                    verticalWord = "";
                }else if(this.matrix[j][i] == ' ' && startPointV != null) {
                    endPointV = new Point(i,j-1);
                    verticalWords.add(new WordToPlace(verticalWord,startPointV,endPointV));
                    startPointV = null;
                    endPointV = null;
                    verticalWord = "";
                }
            }
        }
        this.pAsHorizontal = horizontalWords;
        this.pAsVertical = verticalWords;
        System.out.println("Horizontal pAs: " + this.pAsHorizontal);
        System.out.println("Vertical pAs: " + this.pAsVertical);
    }
    /**
     * When the following method is invoked, it will return the word and position that yields the highest score
     * based on the possible placeable tiles in generatedGuesses.
     */
    public String findBestGuess(){
        int highestScore = 0;
        String bestWord = "";
        boolean isHorizontal = false;

        ArrayList<String> words = new ArrayList<>();


        // Check if the coordinate in the hashmap has a member and if so, build the word.
        for(Point p : this.generatedGuesses.keySet()){
            this.generatedGuesses.get(p).forEach((word, isHorizontal1) -> {
                // Check if the array
                boolean isEnded = false;
                int startingIndex = 0;

                // Using the letter in "word" and the coordinate in "p", build the word.
                String builtWord = "";

                // Check if the word is horizontal or vertical and then check surrounding.
                if (isHorizontal1) {
                    if (this.matrix[(int) p.getY()][(int) p.getX() + 1] != ' ') {
                        while (!isEnded) {
                            if ((int) p.getX() + startingIndex <= 14 && this.matrix[(int) p.getY()][(int) p.getX() + startingIndex] != ' ') {
                                builtWord += this.matrix[(int) p.getY()][(int) p.getX() - startingIndex];
                                startingIndex++;
                            } else {
                                isEnded = true;
                            }
                        }
                    } else if (this.matrix[(int) p.getY()][(int) p.getX() - 1] != ' ') {
                        while (!isEnded) {
                            if ((int) p.getX() - startingIndex >= 0 && this.matrix[(int) p.getY()][(int) p.getX() - startingIndex] != ' ') {
                                builtWord += this.matrix[(int) p.getY()][(int) p.getX() - startingIndex];
                                startingIndex++;
                            } else {
                                // Reverse the string
                                builtWord = new StringBuilder(builtWord).reverse().toString();

                                // Add the inital word
                                builtWord += word;

                                isEnded = true;
                            }
                        }
                    } else {
                        builtWord = word;
                    }
                } else {
                    if (this.matrix[(int) p.getY() + 1][(int) p.getX()] != ' ') {
                        while (!isEnded) {
                            if ((int) p.getY() + startingIndex <= 14 && this.matrix[(int) p.getY() + startingIndex][(int) p.getX()] != ' ') {
                                builtWord += this.matrix[(int) p.getY() + startingIndex][(int) p.getX()];
                                startingIndex++;
                            } else {
                                isEnded = true;
                            }
                        }
                    } else if (this.matrix[(int) p.getY() - 1][(int) p.getX()] != ' ') {
                        while (!isEnded) {
                            if ((int) p.getY() - startingIndex >= 0 && this.matrix[(int) p.getY() - startingIndex][(int) p.getX()] != ' ') {
                                builtWord += this.matrix[(int) p.getY() - startingIndex][(int) p.getX()];
                                startingIndex++;
                            } else {
                                // Reverse the string
                                builtWord = new StringBuilder(builtWord).reverse().toString();

                                // Add the inital word
                                builtWord += word;

                                isEnded = true;
                            }
                        }
                    } else {
                        builtWord = word;
                    }
                }
                words.add(builtWord);
            });
        }
        for (String word : words) {
            int score = 0;
            for (int i = 0; i < word.length(); i++) {
                score += this.tileScoreManager.getTilePoints(word.charAt(i));
            }
            if (score > highestScore) {
                highestScore = score;
                bestWord = word;
            }
        }
        return bestWord;
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
    //a move has a word from generatedGuesses, a direction, and it's startPoint

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
        matrix[2][2] = 'o';
        matrix[3][0] = 'c';
        matrix[3][1] = 'f';
        matrix[2][13] = 'o';
        ArrayList<Character> tray = new ArrayList<>();
        tray.addAll(List.of('d','g','l','e','f','f'));
        AIPlayer p = new AIPlayer(new BoardModel(), matrix, new Tray(tray, b));
        p.printMatrix();
        p.guessHorizontal();
        p.guessVertical();

        for(Move m: p.moves){
            System.out.println("Word: " + m.word);
            System.out.println("aiPoint: " + m.aiPoint);
            System.out.println("Points: " + m.getPoints());
            System.out.println("isSuffix: " + m.isSuffix);
            System.out.println("isHorizontal: " + m.isHorizontal);
            System.out.println("tiles: ");
            m.getTileMoves().forEach(tileMove -> System.out.println(tileMove.getPoint()));
        }
        System.out.println("The best letter / word the AI can play is: " + p.findBestGuess());
    }

}
