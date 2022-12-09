import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class BoardTile extends JButton implements Serializable {

    private Point p;
    private Character c;

    public BoardTile(Character c, Point p) {
       this.c = c;
        this.p = p;
    }

    public void setC(Character c){
        this.c = c;
        setText(c.toString());
    }
    public Point getP(){return p;}
    public Character getC() {
        return this.c;
    }
}
