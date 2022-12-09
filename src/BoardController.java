import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class BoardController implements ActionListener, Serializable {

    private ScrabbleFrame scrabbleFrame;
    private BoardModel model;
    private boolean isScrabbleTileInHand = false;
    private ScrabbleTile s;
    ArrayList<BoardTile> stackOfBoardTilesOccupied = new ArrayList<>();
    ArrayList<ScrabbleTile> stackOfScrabbleTileDropped = new ArrayList<>();
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
            b.setC(s.getChar());
            s = null;
            isScrabbleTileInHand = false;
        }
        JButton b = (JButton) e.getSource();
        if(b.getActionCommand().equals("clear") ){
            clear();
        }
        if(b.getActionCommand().equals("submit")){

            System.out.println(stackOfBoardTilesOccupied.size());
            String word = "";
            for(ScrabbleTile c: stackOfScrabbleTileDropped){
                word+=c.getChar();
            }
            int j = 0;
            for (int i =0; i < stackOfBoardTilesOccupied.size()-1;i++){
                if(stackOfBoardTilesOccupied.get(i).getP().getY()-stackOfBoardTilesOccupied.get(i+1).getP().getY() == 0){
                    j = 1;
                }else if(stackOfBoardTilesOccupied.get(i).getP().getX()-stackOfBoardTilesOccupied.get(i+1).getP().getX() == 0){
                    j = 2;
                }
            }
            for(int i = 0; i < stackOfBoardTilesOccupied.size()-1; i++){
                if(j==1){
                    if(stackOfBoardTilesOccupied.get(i+1).getP().getX()-stackOfBoardTilesOccupied.get(i).getP().getX() == 1){
                        j = 1;
                    }
                }else if(j == 2){
                    if(stackOfBoardTilesOccupied.get(i+1).getP().getY()-stackOfBoardTilesOccupied.get(i).getP().getY() == 1){
                        j = 2;
                    }
                }
            }
            if(j == 0){
                JOptionPane.showMessageDialog(scrabbleFrame, "The tiles must be connected by the edges consecutively", "Illegal Move", JOptionPane.ERROR_MESSAGE);
                clear();
            } else {
                //TODO: model.playWord(word, stackOfBoardTilesOccupied.get(0).getP(), stackOfBoardTilesOccupied.get(stackOfBoardTilesOccupied.size() - 1).getP());
            }


        }
        if (b.getActionCommand().equals("save")){
            this.saveInstance(this.scrabbleFrame);
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

    /**
     * The following is used for saving a game.
     * @param curr {@link ScrabbleFrame} to be saved.
     */
    public void saveInstance(ScrabbleFrame curr){
        try {
            String pane = JOptionPane.showInputDialog(scrabbleFrame, "Input a file name to save the game to.");

            // Lets create a file to save the stream to
            FileOutputStream fileOut = new FileOutputStream(pane + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            // Now we can write the object to the file
            out.writeObject(curr);

            // Close the file
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + pane + ".ser" + ". Killing the game...");

            // Kill the game
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
