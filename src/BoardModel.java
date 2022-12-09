import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class BoardModel implements Serializable {
    public Character[][] matrix;
    /*public int[][] premiumMatrix;*/
    public int turn = 0;
    private String bus = "";
    private ArrayList<ScrabbleView> views;
    private Bag tileBag;
    private boolean isPlayer1, isMatrixClear, canMakeMove, isAI;
    private Tray trayPlayer1, trayPlayer2;
    private static int player1Score= 0, player2Score=0;
    private Stack<Move> playedMoves;
    private ScrabbleDictionary dictionary;
    Stack<WordToPlace> wordsCreated = new Stack<>();
    private TileScoreManager tileScoreManager = new TileScoreManager();
    private Stack<Character> trayStackForSelectedWord = new Stack<>();
    public BoardModel() {
        setMatrix();
        dictionary = new ScrabbleDictionary();
        tileBag = new Bag();
        isPlayer1 = true;
        views = new ArrayList<>();
        isMatrixClear = true;
        setTraysForPlayers();
    }

    public void setIsAi(){
        isAI = true;
    }

    //TODO: set premium tiles
//    public void setPremiumMatrix(int option){
//        switch (option){
//            case 0:
//                normalMatrix();
//                break;
//            case 1:
//                crossMatrix();
//                break;
//            case 2:
//                alternatingMatrix();
//                break;
//        }
//    }
    public void addViews(ScrabbleView view){
        this.views.add(view);
        view.update(new ScrabbleEvent(this,trayPlayer1,trayPlayer2,matrix,player1Score,player2Score,isPlayer1, bus));
    }
    public void setMatrix() {
        matrix = new Character[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                matrix[i][j] = ' ';
            }
        }
        playedMoves = new Stack<Move>();
    }
    public Stack<Move> getPlacedWords(){
        return this.playedMoves;
    }
    private void setTraysForPlayers(){
        trayPlayer1 = new Tray(tileBag.testTray(), tileBag);
        trayPlayer2 = new Tray(tileBag.testTray(), tileBag);
        System.out.println("Tray 1 size: " + trayPlayer1.getSize() + " " + trayPlayer1);
        System.out.println("Tray 2 size: " + trayPlayer2.getSize() + " " + trayPlayer2);
        System.out.println("tileBag size:" + tileBag.getSize());
    }
    public boolean checkContinuity(Point start, Point end){
        if(start.getX() - end.getX() == 0){
            return true;
        }if(start.getY() - end.getY() == 0){
            return true;
        }return false;
    }


    private boolean isMoveInTray(Move move) {
        if(isPlayer1){
            //check player1 tray
            for(TileMove tileMove : move.getTileMoves()) {
                if (!trayPlayer1.checkString(tileMove.c)) return true;
            }
        }else{
            //check player2 tray
            for(TileMove tileMove : move.getTileMoves()) {
                if (!trayPlayer2.checkString(tileMove.c)) return true;
            }
        }
        return false;
    }

    private boolean checkMatrixIfPointsAreEmpty(Move move) {
        for(Point p : move.getPoints()){
            int y = (int) p.getY();
            int x = (int) p.getX();
            if(matrix[y][x] != ' '){
                System.out.println("Move not valid");
                return true;
            }
        }
        return false;
    }

   /* public void playWord(String word, Point startPoint, Point endPoint) {
        if(!checkContinuity(startPoint,endPoint)){
            System.out.println("Invalid coordinates");
            return;
        }
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
            bus = "success";
            System.out.println("Player 1 score = " + player1Score);
            System.out.println("Player 2 score = " + player2Score);
        } else {
            addCharactersBackToTray();
            bus = "error";
        }
        for(ScrabbleView v: views){
            v.update(new ScrabbleEvent(this,trayPlayer1,trayPlayer2,matrix,player1Score,player2Score,isPlayer1, bus));
        }
    }*/

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
    public int calculateScore(Move m){
        int score = 0;
        System.out.println("stack size: "+wordsCreated.size());
        Iterator<WordToPlace> iterator = wordsCreated.iterator();
        while (iterator.hasNext()){
            System.out.println("Calculating score: ");
            WordToPlace w = iterator.next();
            String word = w.getWord();
            score+= getBoardTilePoints(m);
            for(Character c: word.toCharArray()){
                score += tileScoreManager.getTilePoints(c);
                System.out.println(c + " : " + tileScoreManager.getTilePoints(c));
            }
            System.out.println("Score for this turn : " +score );
        }
        wordsCreated.clear();
        return score;
    }
    private int getBoardTilePoints(Move m){
        int tilePoints = 0;
        for(TileMove t: m.getTileMoves()){
           // tilePoints += premiumMatrix[t.getY()][t.getX()];
        }
        return tilePoints;
    }

    private boolean putWord(Point startPoint, Point endPoint, Move m, boolean isHorizontal) {
        if(startPoint.equals(endPoint)){
            System.out.println("singular point");
            return false;
        }
        String word = getWordFromPoint(startPoint, endPoint, m, isHorizontal);
        System.out.println("word : " + word);
        if(word.equals("kathan")){ //just for fun
            System.out.println("Word cannot be added because it is not connected to other words");
            return false;
        }
        if (dictionary.isWord(word)) {
            System.out.println("Word added to stack");
            wordsCreated.add(new WordToPlace(word, startPoint, endPoint));
            System.out.println("stack size: " + wordsCreated.size());
            return true;
        }else{
            System.out.println("Word not added");
            wordsCreated.clear();
            return false;
        }
    }
    public void status(Move m){
        printMatrix();
        if(isPlayer1){
            player1Score += calculateScore(m);
            isPlayer1 = false;
            if(isAI){
                AIPlayer ai = new AIPlayer(this,matrix,trayPlayer2);
                ai.play();
                isPlayer1 = true;
            }
        }else if (!isPlayer1 && !isAI){
            player2Score += calculateScore(m);
            isPlayer1 = true;
        }
    }

    public String getWordFromPoint(Point startPoint, Point endPoint, Move m, boolean isHorizontal) {
        String word = "";
        boolean done = true;
        boolean matrixInvoked = false;
        Point p = startPoint;
        int i = 0;
        while(done){
            if (p.getX() > endPoint.getX() && isHorizontal) {
                done = false;
                break;
            }
            if(p.getY() > endPoint.getY() && !isHorizontal){
                done = false;
                break;
            }
            if ((i < m.getTileMoves().size() ) && (p.getX() == m.getTileMoves().get(i).getX() )&& (p.getY() == m.getTileMoves().get(i).getY())) {

                word += m.getTileMoves().get(i).getC();
                i++;
            } else {
                word += matrix[(int) p.getY()][(int) p.getX()];
                matrixInvoked = true;
            }
            if (isHorizontal) {
                p = new Point((int) p.getX() + 1, (int) p.getY());
            } else {
                p = new Point((int) p.getX(), (int) p.getY() + 1);
            }
        }
        System.out.println("The word generated is: " + word);

        if((!matrixInvoked) && (!isMatrixClear)){
            return "kathan";
        }
        return word;
    }

//TODO: remove characters from the tray.
    public void placement(Move m){
        if(isMatrixClear){
            isMatrixClear = false;
        }
        for(TileMove t: m.getTileMoves()){
            matrix[t.getY()][t.getX()] = t.getC();
            trayStackForSelectedWord.add(t.getC());
            if(isPlayer1){
                trayPlayer1.removeAlphabet(t.getC());
            }else{
                trayPlayer2.removeAlphabet(t.getC());
            }
        }
        playedMoves.push(m);
        System.out.println(m.getWord());
        System.out.println("Tray 1: " + trayPlayer1);
        System.out.println("Tray 2: " + trayPlayer2);
        status(m);
    }
    public boolean playMove(Move move){
        if(!move.isValid) return false;
        if (checkMatrixIfPointsAreEmpty(move)) return false;
        //check if the word in the move is present in the tray
        if (isMoveInTray(move)) return false;
        if(isMatrixClear){
            for (TileMove t: move.getTileMoves()){
                if(t.getX() == 7 && t.getY() == 7){
                    canMakeMove = true;
                }
            }
            if(canMakeMove){
                if(dictionary.isWord(move.getWord())){
                    putWord( move.tileMoves.get(0).getPoint(), move.tileMoves.get(move.tileMoves.size()-1).getPoint(), move, move.isHorizontal);
                    return !wordsCreated.isEmpty();
                }
            }else {
                System.out.println("Select a move that goes from the centre");
                return false;
            }
        }
        if(move.isHorizontal){
            checkHorizontalWord(move);
        }else{
            checkVerticalWord(move);
        }
        System.out.println("stack : " + wordsCreated.size());
        return !wordsCreated.isEmpty();
    }
    private boolean checkHorizontalWord(Move m) {
        System.out.println("inCheckHorizontal");
        //checkEast -> endPoint for the word
        //checkWest -> startPoint for the word
        Point startPoint = checkWest(m.getStart().getPoint());
        Point endPoint = checkEast(m.getEnd().getPoint());
        putWord(startPoint, endPoint, m, true);
        //for each character at position X, checkNorth and checkSouth to get start and end points for the words
        for (int i = m.getStart().getX(); i <= m.getEnd().getX(); i++) {
            Point startPointVertical = checkNorth(new Point(i, m.getStart().getY()));
            Point endPointVertical = checkSouth(new Point(i, m.getStart().getY()));
            if(startPointVertical.getY() - endPointVertical.getY() == 0){
                continue;
            }
            putWord(startPointVertical, endPointVertical, m, false);
        }
        System.out.println("Stack contains: " + wordsCreated);
        return !wordsCreated.isEmpty();
    }

    private boolean checkVerticalWord(Move p) {
        System.out.println("inCheckVertical");
        //checkNorth -> startPoint
        //checkSount -> endPoint
        //for each character at position Y, checkWest, checkEast to get start and endpoints for the word
        Point startPoint = checkNorth(p.getStart().getPoint());
        Point endPoint = checkSouth(p.getEnd().getPoint());
        putWord(startPoint, endPoint, p, false);
        for (int i = p.getStart().getY(); i <= p.getEnd().getY(); i++) {
            Point startPointHorizontal = checkWest(new Point( p.getStart().getX(),i));
            Point endPointHorizontal = checkEast(new Point( p.getStart().getX(), i));
            if(startPointHorizontal.getX() - endPointHorizontal.getX() == 0){
                continue;
            }
            putWord(startPointHorizontal, endPointHorizontal, p, true);

        }
        System.out.println("Stack contains: " + wordsCreated);
        return !wordsCreated.isEmpty();
    }

    private Point checkNorth(Point p) {
        if (p.getY() == 0) {
            return p;
        }else if(matrix[(int) p.getY()-1][(int) p.getX()] == ' '){
            return new Point((int)p.getX(),(int) p.getY());
        } else {
            return checkNorth(new Point((int) p.getX(), (int) p.getY() - 1));
        }
    }

    private Point checkSouth(Point p) {
        if (p.getY() == 15) {
            return p;
        }else if(matrix[(int) p.getY()+1][(int) p.getX()] == ' '){
            return new Point((int)p.getX() ,(int) p.getY());
        } else {
            return checkSouth(new Point((int) p.getX(), (int) p.getY() + 1));
        }
    }

    private Point checkWest(Point p) {
        if (p.getX() == 0) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()-1] == ' '){
            return new Point((int)p.getX(),(int) p.getY());
        } else {
            return checkWest(new Point((int) p.getX() - 1, (int) p.getY()));
        }
    }

    private Point checkEast(Point p) {
        if (p.getX() == 15) {
            return p;
        }else if(matrix[(int) p.getY()][(int) p.getX()+1] == ' '){
            return new Point((int)p.getX(),(int) p.getY());
        } else {
            return checkEast(new Point((int) p.getX() + 1, (int) p.getY()));
        }
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
}
