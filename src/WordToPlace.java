import java.awt.*;
import java.io.Serializable;

public class WordToPlace implements Serializable {
    private String word;
    private Point start;
    private Point end;

    public WordToPlace(String word, Point start, Point end) {
        this.word = word;
        this.start = start;
        this.end = end;
    }

    public String getWord() {
        return this.word;
    }

    public Point getStart() {
        return this.start;
    }

    public Point getEnd() {
        return this.end;
    }
    public int getStartX(){
        return (int) this.start.getX();
    }
    public int getStartY(){
        return (int) this.start.getY();
    }
    public int getEndX(){
        return (int) this.end.getX();
    }
    public int getEndY(){
        return (int) this.end.getY();
    }
    public boolean isHorizontal(){
        if(this.start.getY() - this.end.getY()== 0){
            return true;
        }
        System.out.println("Check fail -> horizontal");
        return false;
    }
    public boolean isVertical(){
        if(this.start.getX() - this.end.getX() == 0){
            return true;
        }
        System.out.println("Check fail -> vertical");
        return false;
    }

    @Override
    public String toString() {
        return "WordToPlace{" +
                "word='" + word + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
