import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public class ButtonController implements MouseListener, Serializable {

    private String _type;

    public ButtonController(String _type) {
        super();
        this._type = _type;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if("bag".equals(this._type)){
            System.out.println("Button clicked");
        }else if("board".equals(this._type)){

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
