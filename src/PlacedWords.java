import java.awt.*;

public class PlacedWords {
    private String word;
    private Point start;
    private Point end;

    public PlacedWords(String word, Point start, Point end) {
        this.word = word;
        this.start = start;
        this.end = end;
    }

    public String getWord() {
        return word;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
    public int getStartX(){
        return (int)start.getX();
    }
    public int getStartY(){
        return (int)start.getY();
    }
    public int getEndX(){
        return (int)end.getX();
    }
    public int getEndY(){
        return (int)end.getY();
    }
    public boolean isHorizontal(){
        if(start.getY() - end.getY()== 0){
            return true;
        }

        System.out.println("Check fail -> horizontal");
        return false;
    }
    public boolean isVertical(){
        if(start.getX() - end.getX() == 0){
            return true;
        }
        System.out.println("Check fail -> vertical");
        return false;
    }
}
