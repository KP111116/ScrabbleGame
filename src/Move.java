import org.w3c.dom.html.HTMLTitleElement;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Move implements Serializable {
    String word;
    ArrayList<Point> points;
    ArrayList<TileMove> tileMoves;
    boolean isSuffix;
    Point aiPoint;
    boolean isHorizontal, isVertical;
    WordToPlace wordToPlace;
    boolean isValid = true;

    public Move(ArrayList<TileMove> tileMoves) {
        points = new ArrayList<>();
        for(TileMove t: tileMoves){
            points.add(t.getPoint());
        }
        checkContinuity(points);
        sortMoves(tileMoves);
    }
    public Move(String word, boolean isSuffix, Point aiPoint, boolean isHorizontal) {
        this.word = word;
        this.isSuffix = isSuffix;
        this.aiPoint = aiPoint;
        this.isHorizontal = isHorizontal;
        points = new ArrayList<>();
        generateWordToPlace();
        tileMoves = new ArrayList<>();
        generateTiles();
    }
    public void generateTiles() {
        for(int i = 0; i< points.size();i++){
            this.tileMoves.add(new TileMove(word.charAt(i), (int)points.get(i).getX(),(int)points.get(i).getY()));
        }
        System.out.println("New Move-------------->");
        for (TileMove tileMove : tileMoves) {
            System.out.println(tileMove.c + " at " + tileMove.p);
        }
    }
    public TileMove getStart(){
        return tileMoves.get(0);
    }
    public TileMove getEnd(){
        return tileMoves.get(tileMoves.size() - 1);
    }
    private boolean sortMoves(ArrayList<TileMove> tileMoves) {
        ArrayList<Point> points = new ArrayList<>();

        for(TileMove t: tileMoves){
            points.add(t.getPoint());
        }
        if(checkContinuity(points)){
            Collections.sort(tileMoves, new TileMove.tileComparator());
            points.clear();
            for(TileMove t: tileMoves){
                points.add(t.getPoint());
            }
            if(checkContinuity(points)){
                this.points = points;
            }

            this.tileMoves = tileMoves;
        }
        return false;
    }

    public boolean isValid() {
        return this.isValid;
    }
    public String getWord() {
        String word = "";
        for(TileMove s: tileMoves){
            word+=s.getC();
        }
        return word;
    }

    public boolean isSuffix() {
        return this.isSuffix;
    }

    public Point getAiPoint() {
        return this.aiPoint;
    }

    public boolean isHorizontal() {
        return this.isHorizontal;
    }

    public boolean isVertical() {
        return this.isVertical;
    }

    public WordToPlace getWordToPlace() {
        return this.wordToPlace;
    }

    private boolean checkContinuity(ArrayList<Point> points){
        for(int i = 0; i < points.size()-1; i++){
            if(points.get(i).getX()-points.get(i+1).getX() == 0){
                isVertical = true;
            }else{
                isVertical = false;
            }
            if(points.get(i).getY()-points.get(i+1).getY() == 0){
                isHorizontal = true;
            }else{
                isHorizontal = false;
            }
        }
        if((isHorizontal && !isVertical) || (isVertical && !isHorizontal) ){
            isValid = true;
            System.out.println("IsValid: " + isValid);
            return true;
        }
        isValid = false;
        return false;
    }
    public void sort(){

    }

    public ArrayList<TileMove> getTileMoves() {
        return this.tileMoves;
    }

    //for ai
    private void generateWordToPlace(){
        int length = word.length();
        if(isSuffix){
            for(int i = 1; i <= length; i++){
                if(isHorizontal){
                    points.add(new Point((int)aiPoint.getX() + i, (int)aiPoint.getY()));
                }else{
                    points.add(new Point((int)aiPoint.getX(),(int)aiPoint.getY()+i));
                }
            }
        }else{
            System.out.println(length);
            for(int j = length; j >= 1; j--){
                if(isHorizontal){
                    points.add(new Point((int)aiPoint.getX() - j, (int)aiPoint.getY()));
                }else{
                    points.add(new Point((int)aiPoint.getX(),(int)aiPoint.getY() - j));
                }
            }
        }
        this.wordToPlace = new WordToPlace(word,points.get(0),points.get(points.size()-1));
     }

    public ArrayList<Point> getPoints() {
        return points;
    }


    public static void main(String[] args) {
        TileMove t0 = new TileMove('a', 2,3);
        TileMove t1 = new TileMove('a', 2,4);
        TileMove t2 = new TileMove('a', 2,2);
        TileMove t3 = new TileMove('a', 2,1);
        ArrayList<TileMove> moves = new ArrayList<>();
        moves.add(t0);
        moves.add(t1);
        moves.add(t2);
        moves.add(t3);
        Move m = new Move(moves);
        m.tileMoves.forEach(tileMove -> System.out.println(tileMove.getPoint() + " char: " + tileMove.getC()));
    }
}
