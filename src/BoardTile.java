import javax.swing.*;
import java.awt.*;

public class BoardTile extends JButton {

    private Point p;
    private Character c;
    private int value;

    public BoardTile(Character c) {
        super(String.valueOf(c));
        this.value = value;
    }
    public void setC(Character c){
        setText(c.toString());
    }

    public void setP(int x, int y){
        p = new Point(x,y);
    }
    public Point getP(){return p;}

}
