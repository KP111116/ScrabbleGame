import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

public class BoardController implements ActionListener {

    private ScrabbleFrame scrabbleFrame;
    private BoardModel model;
    private boolean isScrabbleTileInHand = false;
    private ScrabbleTile s;
    ArrayList<BoardTile> stackOfBoardTilesOccupied = new ArrayList<>();
    ArrayList<ScrabbleTile> stackOfScrabbleTileDropped = new ArrayList<>();
    ArrayList<TileMove> tileMoves = new ArrayList<>();
    public BoardController(ScrabbleFrame scrabbleFrame, BoardModel model) {
        this.scrabbleFrame = scrabbleFrame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof ScrabbleTile && !isScrabbleTileInHand){
            s = (ScrabbleTile) e.getSource();
            s.setEnabled(false);
            stackOfScrabbleTileDropped.add(s);
            isScrabbleTileInHand = true;
        }
        if(e.getSource() instanceof BoardTile && isScrabbleTileInHand){
            BoardTile b = (BoardTile) e.getSource();
            stackOfBoardTilesOccupied.add(b);
            tileMoves.add(new TileMove(stackOfScrabbleTileDropped.get(stackOfScrabbleTileDropped.size()-1).getChar(), (int)b.getP().getX(), (int)b.getP().getY()));
            b.setC(s.getChar());
            s = null;
            isScrabbleTileInHand = false;
        }
        JButton b = (JButton) e.getSource();
        if(b.getActionCommand().equals("clear") ){
            clear();
        }
        if(b.getActionCommand().equals("submit")){
            Move m = new Move(tileMoves);
            if(model.playMove(m)){
                model.placement(m);
            }else{
                JOptionPane.showMessageDialog(scrabbleFrame, "Try again please", "Invalid Move",JOptionPane.ERROR_MESSAGE );
                clear();
            }
        }
    }
    public void refreshStack(){
        stackOfScrabbleTileDropped.clear();
        stackOfBoardTilesOccupied.clear();
    }
    public void clear() {
        for(ScrabbleTile s : stackOfScrabbleTileDropped){
            s.setEnabled(true);
        }
        for(BoardTile t: stackOfBoardTilesOccupied){
            t.setC(model.matrix[(int)t.getP().getY()][(int)t.getP().getX()]);
        }
        stackOfBoardTilesOccupied.clear();
        stackOfScrabbleTileDropped.clear();
    }
}
