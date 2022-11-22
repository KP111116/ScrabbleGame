import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonController implements MouseListener {

    private String _type;

    public ButtonController(String _type) {
        this._type = _type;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(_type.equals("bag")){
            System.out.println("Button clicked");
        }else if(_type.equals("board")){

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
