import java.util.EventObject;

public class ScrabbleEvent extends EventObject {
    private final Tray trayPlayer1;
    private final Tray trayPlayer2;
    private final Character[][] matrix;
    private final int player1Score;
    private final int player2Score;
    private final boolean isPlayer1;

    public ScrabbleEvent(BoardModel m, Tray trayPlayer1, Tray trayPlayer2, Character[][] matrix, int player1Score, int player2Score, boolean isPlayer1) {
        super(m);
        this.trayPlayer1 = trayPlayer1;
        this.trayPlayer2 = trayPlayer2;
        this.matrix = matrix;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.isPlayer1 = isPlayer1;
    }

    public Tray getTrayPlayer1() {
        return trayPlayer1;
    }

    public Tray getTrayPlayer2() {
        return trayPlayer2;
    }

    public Character[][] getMatrix() {
        return matrix;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }
}
