import java.util.EventObject;

public class ScrabbleEvent extends EventObject {
    private final Tray trayPlayer1;
    private final Tray trayPlayer2;
    private final Character[][] matrix;
    private final int player1Score;
    private final int player2Score;
    private final boolean isPlayer1;


    private final boolean isAIplaying;
    public ScrabbleEvent(BoardModel m, Tray trayPlayer1, Tray trayPlayer2, Character[][] matrix, int player1Score, int player2Score, boolean isPlayer1, boolean isAIplaying) {
        super(m);
        this.isAIplaying = isAIplaying;
        this.trayPlayer1 = trayPlayer1;
        this.trayPlayer2 = trayPlayer2;
        this.matrix = matrix;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.isPlayer1 = isPlayer1;
    }

    public Tray getTrayPlayer1() {
        return this.trayPlayer1;
    }

    public Tray getTrayPlayer2() {
        return this.trayPlayer2;
    }

    public Character[][] getMatrix() {
        return this.matrix;
    }

    public int getPlayer1Score() {
        return this.player1Score;
    }

    public int getPlayer2Score() {
        return this.player2Score;
    }

    public boolean isPlayer1() {
        return this.isPlayer1;
    }

    public boolean isAIplaying() {
        return this.isAIplaying;
    }
}
