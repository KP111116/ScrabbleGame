import java.io.Serializable;

public interface ScrabbleView extends Serializable {
    void update(ScrabbleEvent e);
}
