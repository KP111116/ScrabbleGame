import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardModel {
    public Character[][] matrix;
    private ArrayList<ScrabbleView> views;
    private Bag tileBag;
    private boolean isPlayer1;
    private Tray trayPlayer1, trayPlayer2;
    private static int player1Score= 0, player2Score=0;
    private Stack<PlacedWords> placedWords;
    private ScrabbleDictionary dictionary;
    Stack<PlacedWords> wordsCreated = new Stack<>();
    private TileScoreManager tileScoreManager = new TileScoreManager();
    private Stack<Character> trayStackForSelectedWord = new Stack<>();
    public BoardModel() {
        setMatrix();
        dictionary = new ScrabbleDictionary();
        tileBag = new Bag();
        isPlayer1 = true;
        views = new ArrayList<>();
        setTraysForPlayers();
    }
    public void setMatrix() {
        matrix = new Character[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                matrix[i][j] = ' ';
            }
        }
        placedWords = new Stack<PlacedWords>();
    }
    public Stack<PlacedWords> getPlacedWords(){
        return this.placedWords;
    }
    private void setTraysForPlayers(){
        //trayPlayer1 = new Tray(tileBag.getTray(), tileBag);
        //trayPlayer2 = new Tray(tileBag.getTray(), tileBag);
        ArrayList<Character> tray = new ArrayList<>();
        tray.addAll(List.of('m','y','g', 'o', 'n','d', 'l', 'o'));
        trayPlayer1 = new Tray( tray, tileBag);
        trayPlayer2 = new Tray(tray, tileBag);
        System.out.println("Tray 1 size: " + trayPlayer1.getSize() + " " + trayPlayer1);

        System.out.println("Tray 2 size: " + trayPlayer2.getSize() + " " + trayPlayer2);
        System.out.println("tileBag size:" + tileBag.getSize());
    }
    public void playWord(String word, Point startPoint, Point endPoint) {
        if(!checkWordInTray(word)){
            return;
        }
        if (placeWord(word, startPoint, endPoint)) {
            System.out.println("Word placed");
            if(isPlayer1){
                player1Score += calculateScore();
                trayPlayer1.refill();
                isPlayer1 = false;
            }else{
                player2Score += calculateScore();
                trayPlayer2.refill();
                isPlayer1 = true;
            }
            trayStackForSelectedWord.clear();
            System.out.println("Player 1 score = " + player1Score);
            System.out.println("Player 2 score = " + player2Score);
        } else {
            addCharactersBackToTray();
            System.out.println("Coordinates error, try again");
        }
        for(ScrabbleView v: views){
            v.update(new ScrabbleEvent(this,trayPlayer1,trayPlayer2,matrix,player1Score,player2Score,isPlayer1));
        }
    }
    //tray check
    public boolean checkWordInTray(String word){
        for(char c: word.toCharArray()){
            if(isPlayer1){
                if(trayPlayer1.removeAlphabet(c)) {
                    trayStackForSelectedWord.push(c);
                    System.out.println(trayStackForSelectedWord);
                }else{
                    addCharactersBackToTray();
                    System.out.println("character " + c +" not in tray 1");
                    return false;
                }
            }else{
                if(trayPlayer2.removeAlphabet(c)){
                    trayStackForSelectedWord.push(c);
                }else{
                    addCharactersBackToTray();
                    System.out.println("character " + c+" not in tray 2");
                    return false;
                }
            }
        }
        return true;
    }
    public void addCharactersBackToTray(){
        while(!trayStackForSelectedWord.isEmpty()){
            if (isPlayer1){
                trayPlayer1.addAlphabet(trayStackForSelectedWord.pop());
            }else{
                trayPlayer2.addAlphabet(trayStackForSelectedWord.pop());
            }
        }
    }

    //Score defining block
    private int calculateScore(){
        int score = 0;
        while(!wordsCreated.isEmpty()){
            String word = wordsCreated.pop().getWord();
            for(Character c: word.toCharArray()){
                score += tileScoreManager.getTilePoints(c+"");
                System.out.println(c + " : " + tileScoreManager.getTilePoints(c+""));
            }
            System.out.println("Score for this turn : " +score );
        }
        return score;
    }
    //Word placement block
    public boolean placeWord(String word, Point startPoint, Point endPoint) {
        ArrayList<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        System.out.println("Word: " + characters);
        PlacedWords p = new PlacedWords(word, startPoint, endPoint);
        //check if the cells are empty for placement
        if (p.isHorizontal()) {
            System.out.println("isHorizontal");
            for (int i = p.getStartX(); i <= p.getEndX(); i++) {
                if (matrix[p.getStartY()][i] != ' ') {
                    System.out.println("returning false for x: " + i + " y: " + p.getStartY());
                    return false;
                }
            }
            for (int i = p.getStartX(); i <= p.getEndX(); i++) {
                matrix[p.getStartY()][i] = characters.get(i - p.getStartX()) ;
            }
            return checkHorizontalWord(p); // check if the adjacent words make another word or not
        } else {
            System.out.println("isVertical");
            for (int i = p.getStartY(); i <= p.getEndY(); i++) {
                System.out.println("Checking at x: " + p.getStartX() + " y: " + i);
                if (matrix[i][p.getStartX()] != ' ') {
                    System.out.println("m: " + matrix[i][p.getStartX()]);
                    return false;
                }
            }
            for (int i = p.getStartY(); i <= p.getEndY(); i++) {
                matrix[i][p.getStartX()] = characters.get(i - p.getStartY());
                System.out.println(characters.get(i - p.getStartY()));
            }
            return checkVerticalWord(p); // check if the adjacent words make another word or not
        }
    }

    private void putWord(Point startPoint, Point endPoint) {
        String word = getWordFromPoint(startPoint, endPoint);
        if (dictionary.isWord(word)) {
            System.out.println("Word added to stack");
            wordsCreated.push(new PlacedWords(word, startPoint, endPoint));
        }else{
            wordsCreated.clear();
        }
    }

    private String getWordFromPoint(Point startPoint, Point endPoint) {
        String word = "";
        if (startPoint.getY() - endPoint.getY() == 0) {
            for (int i = (int) startPoint.getX(); i <= (int) endPoint.getX(); i++) {
                System.out.println("Checking matrix at x :" + i + " y: " + startPoint.getY());
                word += matrix[(int) startPoint.getY()][i];
            }
        } else if (startPoint.getX() - endPoint.getX() == 0) {
            for (int i = (int) startPoint.getY(); i <= (int) endPoint.getY(); i++) {
                System.out.println("Checking matrix at x :" + i + " y: " + startPoint.getY());
                word += matrix[i][(int) startPoint.getX()];
            }
        }
        System.out.println("The word generated is: " + word);
        return word;
    }

    private boolean checkHorizontalWord(PlacedWords p) {
        System.out.println("inCheckHorizontal");
        //checkEast -> endPoint for the word
        //checkWest -> startPoint for the word
        Point startPoint = checkWest(p.getStart());
        Point endPoint = checkEast(p.getEnd());

        System.out.println("West point " + startPoint + " ,East Point " + endPoint);
        putWord(startPoint, endPoint);
        //for each character at position X, checkNorth and checkSouth to get start and end points for the words
        for (int i = p.getStartX(); i <= p.getEndX(); i++) {
            Point startPointVertical = checkNorth(new Point(i, p.getStartY()));
            Point endPointVertical = checkSouth(new Point(i, p.getStartY()));
            System.out.println("North point " + startPoint + " ,South Point " + endPoint);
            if(startPointVertical.getY() - endPointVertical.getY() == 0){
                continue;
            }
            putWord(startPointVertical, endPointVertical);
        }
        System.out.println(wordsCreated);
        return !wordsCreated.isEmpty();
    }

    private boolean checkVerticalWord(PlacedWords p) {
        System.out.println("inCheckVertical");
        //checkNorth -> startPoint
        //checkSount -> endPoint
        //for each character at position Y, checkWest, checkEast to get start and endpoints for the word
        Point startPoint = checkNorth(p.getStart());
        Point endPoint = checkSouth(p.getEnd());
        System.out.println("North point " + startPoint + " ,South Point " + endPoint);
        putWord(startPoint, endPoint);
        //for each character at position X, checkNorth and checkSouth to get start and end points for the words
        for (int i = p.getStartX(); i <= p.getEndX(); i++) {
            Point startPointHorizontal = checkWest(new Point(i, p.getStartY()));
            Point endPointHorizontal = checkEast(new Point(i, p.getStartY()));
            if(startPointHorizontal.getX() - endPointHorizontal.getX() == 0){
                continue;
            }
            putWord(startPointHorizontal, endPointHorizontal);

        }
        System.out.println("Stack contains: " + wordsCreated);
        return !wordsCreated.isEmpty();
    }

    private Point checkNorth(Point p) {
        if (p.getY() == 0) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()] == ' '){
            return new Point((int)p.getX(),(int) p.getY()+1);
        } else {
            return checkNorth(new Point((int) p.getX(), (int) p.getY() - 1));
        }
    }

    private Point checkSouth(Point p) {
        if (p.getY() == 15) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()] == ' '){
            return new Point((int)p.getX() ,(int) p.getY()-1);
        } else {
            return checkSouth(new Point((int) p.getX(), (int) p.getY() + 1));
        }
    }

    private Point checkWest(Point p) {
        if (p.getX() == 0) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()] == ' '){
            return new Point((int)p.getX() + 1,(int) p.getY());
        } else {
            return checkWest(new Point((int) p.getX() - 1, (int) p.getY()));
        }
    }

    private Point checkEast(Point p) {
        if (p.getX() == 15) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()] == ' '){
            return new Point((int)p.getX() - 1,(int) p.getY());
        } else {
            return checkEast(new Point((int) p.getX() + 1, (int) p.getY()));
        }
    }

    public static void main(String[] args) {
        BoardModel b = new BoardModel();
        b.playWord("god", new Point(1, 2), new Point(1, 4));
        System.out.println("--------------------------------------");
        b.playWord("nly", new Point(2, 3), new Point(4, 3));
        System.out.println("--------------------------------------");
        b.playWord("m", new Point(4, 2), new Point(4, 2));
        System.out.println("--------------------------------------");
        b.playWord("o", new Point(2, 2), new Point(2, 2));

    }
}
