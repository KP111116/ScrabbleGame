import java.io.Serializable;

public interface ScrabbleView extends Serializable {
    void update(ScrabbleEvent e);
    BoardModel importGame(String filename);
    void exportGame(String filename);
}
