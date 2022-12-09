import javax.swing.*;
import java.awt.*;

public class ScrabbleTile extends JPanel {
    private Character c;
    private int value;
    private JButton button;
    private TileScoreManager tsc = new TileScoreManager();
    private JLabel label;
    public ScrabbleTile(Character c) {
        this.setPreferredSize(new Dimension(30,30));
        button = new JButton();
        this.add(button);
        this.c = c;
        this.value = tsc.getTilePoints(this.c);
        label = new JLabel("<html>"+c+"<sub>"+value+"</sub></html>");
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        button.add(label);
    }
    public void changeValue(Character c){
        this.c = c;
        this.value = tsc.getTilePoints(c);
    }
    public int getValue(){return value;}
    public Character getChar(){
        return c;
    }


    public void addActionListener(BoardController controller) {
        this.button.addActionListener(controller);
    }
}
