import java.awt.*;
import java.io.Serializable;
import java.util.Comparator;

public class TileMove implements Serializable {
    char c;
    Point p;

    public TileMove(final char c, int x, int y) {
        this.c = c;
        this.p = new Point(x,y);

    }

    public char getC() {
        return this.c;
    }

    public int getX(){
        return (int) p.getX();
    }
    public int getY(){
        return (int) p.getY();
    }
    public Point getPoint() {
        return this.p;
    }
    public static class tileComparator implements Comparator<TileMove>{
        @Override
        public int compare(TileMove o1, TileMove o2) {
            if(o1.getX() - o2.getX() == 0){
                return o1.getY() - o2.getY();
            }else if(o1.getY() - o2.getY() == 0){
                return o1.getX() - o2.getX();
            }
            System.out.println("Invalid points");
            return 0;
        }
    }
}
