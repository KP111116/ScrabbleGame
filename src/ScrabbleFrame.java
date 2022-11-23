import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScrabbleFrame extends JFrame implements ScrabbleView{
    JPanel matrixPanel, player1Panel = new JPanel(), player2Panel= new JPanel();
    JButton submitPlayer1, submitPlayer2, clearPlayer1, clearPlayer2;
    private JLabel player1Label, player2Label, scorePlayer1, scorePlayer2;
    private int player1Score, player2Score;
    private BoardTile[][] cells;
    private BoardModel model = new BoardModel();
    private BoardController controller = new BoardController(this,model);
    private Tray trayPlayer1, trayPlayer2;
    public ArrayList<ScrabbleTile> tray1, tray2;
    private Character[][] matrix;
    public boolean isPlayer1;
    public ScrabbleFrame(String title){
        super(title);
        model.addViews(this);
        tray1 = new ArrayList<>();
        tray2=new ArrayList<>();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gb);
        //set up panels
        player1Panel = new JPanel(new GridLayout(13,3,10,10));
        matrixPanel = new JPanel(new GridLayout(15,15,3,3));
        player2Panel = new JPanel(new GridLayout(13,3,10,10));
        player1Panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK,Color.darkGray));
        player2Panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK,Color.darkGray));

        //set up components for player 1
        player1Label = new JLabel("<html><font size = 4>    Player 1    </font></html>",SwingConstants.CENTER);
        player1Panel.add(player1Label);
        scorePlayer1 = new JLabel("Score :" + this.player1Score,SwingConstants.CENTER);
        player1Panel.add(scorePlayer1);



        //set up components for player 2
        player2Label = new JLabel("<html><font size = 4>    Player 2    </font></html>",SwingConstants.CENTER);
        player2Panel.add(player2Label);
        scorePlayer2 = new JLabel("Score :" + this.player2Score,SwingConstants.CENTER);
        player2Panel.add(scorePlayer2);

        for(Character i: trayPlayer1.getTray()){
            if(tray1.size() < 7) {
                ScrabbleTile t = new ScrabbleTile(i);
                t.addActionListener(controller);
                player1Panel.add(t);
                tray1.add(t);
            }
        }
        for(Character i: trayPlayer2.getTray()){
            if(tray2.size() < 7) {
                ScrabbleTile t = new ScrabbleTile(i);
                t.addActionListener(controller);
                player2Panel.add(t);
                tray2.add(t);
            }
        }
        updateTray();

        char a = 'a';
        cells = new BoardTile[15][15];
        //set up matrix components
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){

                    cells[i][j] = new BoardTile(matrix[i][j]);
                    cells[i][j].setP(j,i);
                    cells[i][j].addActionListener(controller);

                    cells[i][j].setBackground(Color.GREEN);
                    matrixPanel.add(cells[i][j]);
            }
        }

        clearPlayer1 = new JButton("Clear");
        clearPlayer1.setActionCommand("clear");
        clearPlayer2 = new JButton("Clear");
        clearPlayer2.setActionCommand("clear");
        submitPlayer1 = new JButton("Submit");
        submitPlayer1.setActionCommand("submit");
        submitPlayer2 = new JButton("Submit");
        submitPlayer2.setActionCommand("submit");
        player1Panel.add(submitPlayer1);
        player2Panel.add(submitPlayer2);
        player1Panel.add(clearPlayer1);
        player2Panel.add(clearPlayer2);
        submitPlayer1.addActionListener(controller);
        submitPlayer2.addActionListener(controller);
        clearPlayer1.addActionListener(controller);
        clearPlayer2.addActionListener(controller);

        JLabel label = new JLabel("S C R A B B L E");
        label.setFont(new Font("Copperplate Gothic Bold", Font.ROMAN_BASELINE, 50));

        c.ipadx = 10;
        c.ipady = 10;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        add(label,c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(2,5,2,5);
        add(player1Panel, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        add(matrixPanel, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(player2Panel, c);

        model.turn++;
        setSize(new Dimension(975,630));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void updateTray(){
        for(int i = 0; i < 7; i ++){
            Character c = trayPlayer1.getTray().get(i);

        }
        for(Character i: trayPlayer2.getTray()){
            if(tray2.size() < 7) {
                ScrabbleTile t = new ScrabbleTile(i);
                t.addActionListener(controller);
                player2Panel.add(t);
                tray2.add(t);
            }
        }
        if(isPlayer1){
            for(Component c: player2Panel.getComponents()){
                c.setEnabled(false);
            }
            for(Component t: player1Panel.getComponents()){
                t.setEnabled(true);
            }
        }else{
            for(Component t: player1Panel.getComponents()){
                t.setEnabled(false);
            }
            for (Component t: player2Panel.getComponents()){
                t.setEnabled(true);
            }
        }
    }
    private void updateCells(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                cells[i][j].setText(matrix[i][j].toString());
            }
        }
    }
    private void updateScore(){
        scorePlayer1.setText("Score :" + this.player1Score);
        scorePlayer2.setText("Score :" + this.player2Score);
    }

    public static void main(String[] args) {
        ScrabbleFrame f = new ScrabbleFrame("Scrabble Game");
    }

    @Override
    public void update(ScrabbleEvent e) {
        System.out.println("now in update");
        System.out.println(e.getBus());
        if(e.getBus().equals("error")){
            JOptionPane.showMessageDialog(this, "Put the letter in order, left to right or top to bottom", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
            model = new BoardModel();
            controller.clear();
        } else if (e.getBus().equals("success")) {
            controller.refreshStack();
        }
        this.matrix = e.getMatrix();
        this.player1Score = e.getPlayer1Score();
        this.player2Score = e.getPlayer2Score();
        this.isPlayer1 = e.isPlayer1();
        this.trayPlayer1 = e.getTrayPlayer1();
        this.trayPlayer2 = e.getTrayPlayer2();
        System.out.println(model.turn);
        if(model.turn >= 1){
            updateTray();
            updateCells();
            updateScore();
        }
    }
}
