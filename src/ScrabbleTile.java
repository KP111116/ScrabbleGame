import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ScrabbleTile extends JButton implements Serializable {
    private Character c;
    private int value;
    private TileScoreManager tsc = new TileScoreManager();
    private JLabel label;
    public ScrabbleTile(Character c) {
        super();
        this.c = c;
        this.value = tsc.getTilePoints(this.c);
        label = new JLabel("<html>"+c+"<sub>"+value+"</sub></html>");
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }
    public void changeValue(Character c){
        this.c = c;
        this.value = tsc.getTilePoints(c);
    }
    public int getValue(){return value;}
    public Character getChar(){
        return c;
    }

}
