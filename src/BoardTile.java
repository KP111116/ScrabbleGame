import javax.swing.*;
import java.awt.*;

public class BoardTile extends JButton {

    private Point p;
    private Character c;

    public BoardTile(Character c, Point p) {
        super(String.valueOf(c));
        this.p = p;
    }
    public void setC(Character c){
        setText(c.toString());
    }
    public Point getP(){return p;}
    public Character getC() {
        return this.c;
    }
}
