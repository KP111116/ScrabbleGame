import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ScrabbleButton extends JPanel{

    private String _text;
    private Point _p;
    private JLabel label;
    private int _tilePoints;
    private Color _c;

    public ScrabbleButton(String _text, int _tilePoints, Point _p, Color _c, ButtonController bc) {
        this._text = _text;
        this._p = _p;
        this._c = _c;
        this._tilePoints = _tilePoints;
        addMouseListener(bc);
        label = new JLabel("<html><font size = 5>"+_text+"<sub>"+_tilePoints+"</sub></font></html>", SwingConstants.CENTER );
        setLayout(new BorderLayout(2,2));
        setPreferredSize(new Dimension(30,30));
        add(label);
    }

    public void setColor(Color c){
        this._c = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(_c);
        g.fillRect(2,2,getWidth(),getHeight());
        g.setFont(new Font("", Font.BOLD,13));
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        f.setMinimumSize(new Dimension(40,40));
        ScrabbleButton s = new ScrabbleButton("A", 1, new Point(1,2), new Color(150,150,15), new ButtonController("bag"));
        f.add(s);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
