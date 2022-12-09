import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class BoardModel implements Serializable {
    private static int player1Score, player2Score;
    public Character[][] matrix;
    /*public int[][] premiumMatrix;*/
    public int turn;
    Stack<WordToPlace> wordsCreated = new Stack<>();
    private final ArrayList<ScrabbleView> views;
    private final Bag tileBag;

    public boolean isPlayer1() {
        return this.isPlayer1;
    }

    private boolean isPlayer1, isMatrixClear, canMakeMove, isAI;
    private Tray trayPlayer1, trayPlayer2;
    private Stack<Move> playedMoves, undoneMoves;
    private final ScrabbleDictionary dictionary;
    private final TileScoreManager tileScoreManager = new TileScoreManager();

    public BoardModel() {
        this.setMatrix();
        this.dictionary = new ScrabbleDictionary();
        this.tileBag = new Bag();
        this.isPlayer1 = true;
        this.views = new ArrayList<>();
        this.isMatrixClear = true;
        this.setTraysForPlayers();
    }

    public void setIsAi(final boolean setAI) {
        this.isAI = setAI;
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

    public void addViews(final ScrabbleView view) {
        views.add(view);
    }

    public Tray getTrayPlayer1() {
        return this.trayPlayer1;
    }

    public Tray getTrayPlayer2() {
        return this.trayPlayer2;
    }

    public void setMatrix() {
        this.matrix = new Character[15][15];
        for (int i = 0; 15 > i; i++) {
            for (int j = 0; 15 > j; j++) {
                this.matrix[i][j] = ' ';
            }
        }
        this.playedMoves = new Stack<Move>();
        this.undoneMoves = new Stack<Move>();
    }

    private void setTraysForPlayers() {
        this.trayPlayer1 = new Tray(this.tileBag.testTray(), this.tileBag);
        this.trayPlayer2 = new Tray(this.tileBag.testTray(), this.tileBag);
        System.out.println("Tray 1 size: " + this.trayPlayer1.getSize() + " " + this.trayPlayer1);
        System.out.println("Tray 2 size: " + this.trayPlayer2.getSize() + " " + this.trayPlayer2);
        System.out.println("tileBag size:" + this.tileBag.getSize());
    }

    private boolean isMoveInTray(final Move move) {
        if (this.isPlayer1) {
            //check player1 tray
            for (final TileMove tileMove : move.getTileMoves()) {
                if (!this.trayPlayer1.checkString(tileMove.c)) return true;
            }
        } else {
            //check player2 tray
            for (final TileMove tileMove : move.getTileMoves()) {
                if (!this.trayPlayer2.checkString(tileMove.c)) return true;
            }
        }
        return false;
    }

    private boolean checkMatrixIfPointsAreEmpty(final Move move) {
        for (final Point p : move.getPoints()) {
            final int y = (int) p.getY();
            final int x = (int) p.getX();
            if (' ' != matrix[y][x]) {
                System.out.println("Move not valid");
                return true;
            }
        }
        return false;
    }

    //Score defining block
    public int calculateScore(final Move m) {
        int score = 0;
        System.out.println("stack size: " + this.wordsCreated.size());
        final Iterator<WordToPlace> iterator = this.wordsCreated.iterator();
        while (iterator.hasNext()) {
            System.out.println("Calculating score: ");
            final WordToPlace w = iterator.next();
            final String word = w.getWord();
            score += this.getBoardTilePoints(m);
            for (final Character c : word.toCharArray()) {
                score += this.tileScoreManager.getTilePoints(c);
                System.out.println(c + " : " + this.tileScoreManager.getTilePoints(c));
            }
            System.out.println("Score for this turn : " + score);
        }
        this.wordsCreated.clear();
        return score;
    }
    private int getBoardTilePoints(final Move m) {
        final int tilePoints = 0;
        for (final TileMove t : m.getTileMoves()) {
            // tilePoints += premiumMatrix[t.getY()][t.getX()];
        }
        return tilePoints;
    }

    private boolean putWord(final Point startPoint, final Point endPoint, final Move m, final boolean isHorizontal) {
        if (startPoint.equals(endPoint)) {
            System.out.println("singular point");
            return false;
        }
        final String word = this.getWordFromPoint(startPoint, endPoint, m, isHorizontal);
        System.out.println("word : " + word);
        if ("kathan".equals(word)) { //just for fun
            System.out.println("Word cannot be added because it is not connected to other words");
            return false;
        }
        if (this.dictionary.isWord(word)) {
            System.out.println("Word added to stack");
            this.wordsCreated.add(new WordToPlace(word, startPoint, endPoint));
            System.out.println("stack size: " + this.wordsCreated.size());
            return true;
        } else {
            System.out.println("Word not added");
            this.wordsCreated.clear();
            return false;
        }
    }

    public void status(final Move m) {
        this.printMatrix();

        trayPlayer1.refill();
        trayPlayer2.refill();
        System.out.println("Tray 1: " + this.trayPlayer1);
        System.out.println("Tray 2: " + this.trayPlayer2);
        this.updateViews();
    }

    public void updateViews() {
        for (final ScrabbleView view : this.views) {
            view.update(new ScrabbleEvent(this, this.trayPlayer1, this.trayPlayer2, matrix, BoardModel.player1Score, BoardModel.player2Score, this.isPlayer1, isAI));
        }
    }

    public String getWordFromPoint(final Point startPoint, final Point endPoint, final Move m, final boolean isHorizontal) {
        String word = "";
        boolean done = true;
        boolean matrixInvoked = false;
        Point p = startPoint;
        int i = 0;
        while (done) {
            if (p.getX() > endPoint.getX() && isHorizontal) {
                done = false;
                break;
            }
            if (p.getY() > endPoint.getY() && !isHorizontal) {
                done = false;
                break;
            }
            if ((i < m.getTileMoves().size()) && (p.getX() == m.getTileMoves().get(i).getX()) && (p.getY() == m.getTileMoves().get(i).getY())) {

                word += m.getTileMoves().get(i).getC();
                i++;
            } else {
                word += this.matrix[(int) p.getY()][(int) p.getX()];
                matrixInvoked = true;
            }
            if (isHorizontal) {
                p = new Point((int) p.getX() + 1, (int) p.getY());
            } else {
                p = new Point((int) p.getX(), (int) p.getY() + 1);
            }
        }
        System.out.println("The word generated is: " + word);

        if ((!matrixInvoked) && (!this.isMatrixClear)) {
            return "kathan";
        }
        return word;
    }
    public boolean swapCharacter(char c){
        System.out.println("Swapping character");
        if(isPlayer1){
            if(trayPlayer1.swapAlphabet(c)){
                updateViews();
                System.out.println("letter swapped: " + c);
                System.out.println("player1 tray: " +trayPlayer1.getTray());
                return true;
            }
        }else{
            if(trayPlayer2.swapAlphabet(c)){
                updateViews();
                return true;
            }
        }
        return false;
    }
    public void passTurn(){
        if(isPlayer1) isPlayer1 = false;
        else isPlayer1 = true;
        updateViews();
    }
    public void placement(final Move m) {
        if (this.isMatrixClear) {
            this.isMatrixClear = false;
        }
        for (final TileMove t : m.getTileMoves()) {
            this.matrix[t.getY()][t.getX()] = t.getC();
            if (this.isPlayer1) {
                this.trayPlayer1.removeAlphabet(t.getC());
            } else {
                this.trayPlayer2.removeAlphabet(t.getC());
            }
        }
        this.playedMoves.push(m);
        if (this.isPlayer1) {
            BoardModel.player1Score += this.calculateScore(m);
            this.isPlayer1 = false;
            if (this.isAI) {
                final AIPlayer ai = new AIPlayer(this, this.matrix, this.trayPlayer2);
                ai.play();
                this.isPlayer1 = true;
            }
        } else if (!this.isPlayer1 && !this.isAI) {
            BoardModel.player2Score += this.calculateScore(m);
            this.isPlayer1 = true;
        }
        this.status(m);
    }
    public void setAIScore(Move m){
        player2Score += this.calculateScore(m);
    }
    //undo
    //redo
    //save
    public void save(String filename) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename+".ser");
        ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
        oos.writeObject(this);
        fileOutputStream.close();
        oos.close();
    }
    //load
    public BoardModel load(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename+".ser");
        ObjectInputStream ois = new ObjectInputStream(fileInputStream);
        BoardModel bm = (BoardModel) ois.readObject();
        this.views.clear();
        System.out.println(bm);
        fileInputStream.close();
        ois.close();
        return bm;
    }

    public boolean playMove(final Move move) {
        if (!move.isValid) return false;
        if (this.checkMatrixIfPointsAreEmpty(move)) return false;
        //check if the word in the move is present in the tray
        if (this.isMoveInTray(move)) return false;
        if (this.isMatrixClear) {
            for (final TileMove t : move.getTileMoves()) {
                if (7 == t.getX() && 7 == t.getY()) {
                    this.canMakeMove = true;
                }
            }
            if (this.canMakeMove) {
                if (this.dictionary.isWord(move.getWord())) {
                    this.putWord(move.tileMoves.get(0).getPoint(), move.tileMoves.get(move.tileMoves.size() - 1).getPoint(), move, move.isHorizontal);
                    return !this.wordsCreated.isEmpty();
                }
            } else {
                System.out.println("Select a move that goes from the centre");
                return false;
            }
        }
        if (move.isHorizontal) {
            this.checkHorizontalWord(move);
        } else {
            this.checkVerticalWord(move);
        }
        System.out.println("stack : " + this.wordsCreated.size());
        return !this.wordsCreated.isEmpty();
    }

    private boolean checkHorizontalWord(final Move m) {
        System.out.println("inCheckHorizontal");
        //checkEast -> endPoint for the word
        //checkWest -> startPoint for the word
        final Point startPoint = this.checkWest(m.getStart().getPoint());
        final Point endPoint = this.checkEast(m.getEnd().getPoint());
        this.putWord(startPoint, endPoint, m, true);
        //for each character at position X, checkNorth and checkSouth to get start and end points for the words
        for (int i = m.getStart().getX(); i <= m.getEnd().getX(); i++) {
            final Point startPointVertical = this.checkNorth(new Point(i, m.getStart().getY()));
            final Point endPointVertical = this.checkSouth(new Point(i, m.getStart().getY()));
            if (0 == startPointVertical.getY() - endPointVertical.getY()) {
                continue;
            }
            this.putWord(startPointVertical, endPointVertical, m, false);
        }
        System.out.println("Stack contains: " + this.wordsCreated);
        return !this.wordsCreated.isEmpty();
    }

    private boolean checkVerticalWord(final Move p) {
        System.out.println("inCheckVertical");
        //checkNorth -> startPoint
        //checkSouth -> endPoint
        //for each character at position Y, checkWest, checkEast to get start and endpoints for the word
        final Point startPoint = this.checkNorth(p.getStart().getPoint());
        final Point endPoint = this.checkSouth(p.getEnd().getPoint());
        this.putWord(startPoint, endPoint, p, false);
        for (int i = p.getStart().getY(); i <= p.getEnd().getY(); i++) {
            final Point startPointHorizontal = this.checkWest(new Point(p.getStart().getX(), i));
            final Point endPointHorizontal = this.checkEast(new Point(p.getStart().getX(), i));
            if (0 == startPointHorizontal.getX() - endPointHorizontal.getX()) {
                continue;
            }
            this.putWord(startPointHorizontal, endPointHorizontal, p, true);

        }
        System.out.println("Stack contains: " + this.wordsCreated);
        return !this.wordsCreated.isEmpty();
    }

    private Point checkNorth(final Point p) {
        if (0 == p.getY()) {
            return p;
        } else if (' ' == matrix[(int) p.getY() - 1][(int) p.getX()]) {
            return new Point((int) p.getX(), (int) p.getY());
        } else {
            return this.checkNorth(new Point((int) p.getX(), (int) p.getY() - 1));
        }
    }

    private Point checkSouth(final Point p) {
        if (15 == p.getY()) {
            return p;
        } else if (' ' == matrix[(int) p.getY() + 1][(int) p.getX()]) {
            return new Point((int) p.getX(), (int) p.getY());
        } else {
            return this.checkSouth(new Point((int) p.getX(), (int) p.getY() + 1));
        }
    }

    private Point checkWest(final Point p) {
        if (0 == p.getX()) {
            return p;
        } else if (' ' == matrix[(int) p.getY()][(int) p.getX() - 1]) {
            return new Point((int) p.getX(), (int) p.getY());
        } else {
            return this.checkWest(new Point((int) p.getX() - 1, (int) p.getY()));
        }
    }

    private Point checkEast(final Point p) {
        if (15 == p.getX()) {
            return p;
        } else if (' ' == matrix[(int) p.getY()][(int) p.getX() + 1]) {
            return new Point((int) p.getX(), (int) p.getY());
        } else {
            return this.checkEast(new Point((int) p.getX() + 1, (int) p.getY()));
        }
    }

    public void printMatrix() {
        for (int i = 0; 15 > i; i++) {
            if (0 == i) {
                for (int k = 0; 15 > k; k++) {
                    if (0 == k) {
                        System.out.print("\t");
                    }
                    System.out.print("\t" + k);
                }
                System.out.print("\n\n\n");
            }
            for (int j = 0; 15 > j; j++) {
                if (0 == j) {
                    System.out.print(i + "\t");
                }
                System.out.print("\t" + this.matrix[i][j]);
                if (15 == j + 1) {
                    System.out.print("\n");
                }
            }
        }
    }

    public boolean isMatrixClear() {
        return isMatrixClear;
    }

    public static void main(final String[] args) {
        final BoardModel b = new BoardModel();
        b.isAI = true;
        //'g','o','d','o','g','l','e'
        final TileMove t0 = new TileMove('g', 6, 7);
        final TileMove t1 = new TileMove('o', 7, 7);
        final TileMove t3 = new TileMove('o', 8, 7);
        final TileMove t4 = new TileMove('g', 9, 7);
        final TileMove t5 = new TileMove('l', 10, 7);
        final TileMove t6 = new TileMove('e', 11, 7);
        final ArrayList<TileMove> moves = new ArrayList<>();
        moves.add(t0);
        moves.add(t1);
        final Move m = new Move(moves);
        System.out.println(m.getWord());
        if (b.playMove(m)) {
            b.placement(m);
        }
    }
}
