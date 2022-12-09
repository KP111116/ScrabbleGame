import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class BoardController implements ActionListener, Serializable {

    private ScrabbleFrame scrabbleFrame;
    private BoardModel model;
    private boolean isScrabbleTileInHand;
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
        if(e.getSource() == scrabbleFrame.save){
            String saveToFile =JOptionPane.showInputDialog("Enter the file name for your game: ");
            scrabbleFrame.exportGame(saveToFile);
            return;
        }
        if(e.getSource() == scrabbleFrame.load){
            String loadFromFile = JOptionPane.showInputDialog("What is the name of your save game: ");
            this.model = scrabbleFrame.importGame(loadFromFile);
            model.addViews(scrabbleFrame);
            model.updateViews();
            return;
        }
        //todo: redo, undo, setting up ai with frame
        if(e.getSource() == scrabbleFrame.undo){
            model.undo();
            return;
        }
        if(e.getSource() == scrabbleFrame.redo){
            scrabbleFrame.exportGame("resources/undoState");
            this.model = scrabbleFrame.importGame("resources/redoState");
            model.addViews(scrabbleFrame);
            model.updateViews();
            return;
        }

        if(e.getSource() instanceof ScrabbleTile && !isScrabbleTileInHand){
            s = (ScrabbleTile) e.getSource();
            s.setEnabled(false);
            s.setBackground(Color.gray);
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
        if(b.getActionCommand().equals("swap")){
            if(scrabbleFrame.isPlayer1){
                scrabbleFrame.submitPlayer1.setEnabled(false);
            }else{
                scrabbleFrame.submitPlayer2.setEnabled(false);
            }
            ScrabbleTile tileToSwap = stackOfScrabbleTileDropped.get(stackOfScrabbleTileDropped.size()-1);
            model.swapCharacter(tileToSwap.getChar());
            isScrabbleTileInHand = false;
        }
        if(b.getActionCommand().equals("submit")){

            Move m = new Move(tileMoves);
            if(model.playMove(m)){
                model.placement(m);
                stackOfScrabbleTileDropped.clear();
                tileMoves.clear();
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
            s.setBackground(Color.green);
        }
        for(BoardTile t: stackOfBoardTilesOccupied){
            t.setC(model.matrix[(int)t.getP().getY()][(int)t.getP().getX()]);
        }
        stackOfBoardTilesOccupied.clear();
        stackOfScrabbleTileDropped.clear();
        tileMoves.clear();
    }
}
