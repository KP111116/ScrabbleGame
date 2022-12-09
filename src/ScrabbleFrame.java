import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Stack;

public class ScrabbleFrame extends JFrame implements ScrabbleView {
    public boolean isPlayer1, isUndo;

    private Stack<ScrabbleEvent> playedEvents, undoneEvents;

    JPanel matrixPanel, player1Panel, player2Panel;
    JButton submitPlayer1, submitPlayer2, clearPlayer1, clearPlayer2, swapPlayer1,swapPlayer2;
    private JLabel player1Label, player2Label, scorePlayer1, scorePlayer2;
    private ArrayList<ScrabbleTile> player1tiles, player2tiles;
    private int player1Score, player2Score;
    private boolean isAIplaying;
    private BoardTile[][] cells = new BoardTile[15][15];
    private BoardModel model = new BoardModel();
    private BoardController controller = new BoardController(this, model);
    private Tray trayPlayer1, trayPlayer2;
    private JMenuBar menuBar;
    private JMenu menu;
    JMenuItem save, load, undo, redo;
    JCheckBoxMenuItem  playWithAI;

    public ScrabbleFrame(String title) {
        super(title);
        model.addViews(this);

        playedEvents = new Stack<>();
        undoneEvents = new Stack<>();

        //set up menu items
        menuBar = new JMenuBar();
        menu = new JMenu("Game Controls");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        playWithAI = new JCheckBoxMenuItem("Play with AI");

        menu.add(undo);
        menu.add(redo);
        menu.add(save);
        menu.add(load);
        menu.add(playWithAI);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        playWithAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setIsAi(playWithAI.isSelected());
            }
        });
        save.addActionListener(controller);
        undo.addActionListener(controller);
        redo.addActionListener(controller);
        load.addActionListener(controller);


        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gb);
        isPlayer1 = model.isPlayer1();
        trayPlayer1 = model.getTrayPlayer1();
        trayPlayer2 = model.getTrayPlayer2();
        //set up panels
        player1Panel = new JPanel(new GridLayout(13, 1, 10, 10));
        matrixPanel = new JPanel(new GridLayout(16, 16, 3, 3));
        player2Panel = new JPanel(new GridLayout(13, 1, 10, 10));


        player1Panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.darkGray));
        player2Panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.darkGray));

        //set up components for player 1
        player1Label = new JLabel("<html><font size = 4>    Player 1    </font></html>", SwingConstants.CENTER);
        player1Panel.add(player1Label);
        scorePlayer1 = new JLabel("Score :" + this.player1Score, SwingConstants.CENTER);
        player1Panel.add(scorePlayer1);

        //set up components for player 2
        player2Label = new JLabel("<html><font size = 4>    Player 2    </font></html>", SwingConstants.CENTER);
        player2Panel.add(player2Label);
        scorePlayer2 = new JLabel("Score :" + this.player2Score, SwingConstants.CENTER);
        player2Panel.add(scorePlayer2);

        char a = 'a';

        //set up matrix components
        for (int i = -1; 15 > i; i++) {
            for (int j = -1; 15 > j; j++) {
                if ((i == -1) && (j == -1)) {
                    JLabel jLabel = new JLabel("*");
                    jLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    jLabel.setVerticalAlignment(SwingConstants.CENTER);
                    matrixPanel.add(jLabel);
                } else if (i == -1) {
                    JLabel jLabel = new JLabel((char) ('a' + j) + "");
                    jLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    jLabel.setVerticalAlignment(SwingConstants.CENTER);
                    matrixPanel.add(jLabel);
                } else if (j == -1) {
                    JLabel jLabel = new JLabel(" " + (i));
                    jLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    jLabel.setVerticalAlignment(SwingConstants.CENTER);
                    matrixPanel.add(jLabel);
                } else {
                    cells[i][j] = new BoardTile(' ', new Point(j, i));
                    cells[i][j].addActionListener(controller);
                    cells[i][j].setBackground(Color.green);
                    matrixPanel.add(cells[i][j]);
                }
            }
        }
        player1tiles = new ArrayList<>(7);
        player2tiles = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            player1tiles.add(new ScrabbleTile(trayPlayer1.getTray().get(i)));
            player2tiles.add(new ScrabbleTile(trayPlayer2.getTray().get(i)));
            player1tiles.get(i).setBorder(BorderFactory.createEmptyBorder());
            player2tiles.get(i).setBorder(BorderFactory.createEmptyBorder());
            player1tiles.get(i).addActionListener(controller);
            player2tiles.get(i).addActionListener(controller);
            player1Panel.add(player1tiles.get(i));
            player2Panel.add(player2tiles.get(i));
        }


        swapPlayer1 = new JButton("Swap Character");
        swapPlayer2 = new JButton("Swap Character");
        swapPlayer1.setActionCommand("swap");
        swapPlayer2.setActionCommand("swap");
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
        player1Panel.add(swapPlayer1);
        player2Panel.add(swapPlayer2);
        submitPlayer1.addActionListener(controller);
        submitPlayer2.addActionListener(controller);
        clearPlayer1.addActionListener(controller);
        clearPlayer2.addActionListener(controller);
        swapPlayer1.addActionListener(controller);
        swapPlayer2.addActionListener(controller);

        JLabel label = new JLabel("S C R A B B L E");
        label.setFont(new Font("Copperplate Gothic Bold", Font.ROMAN_BASELINE, 50));

        c.ipadx = 10;
        c.ipady = 10;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        add(label, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(2, 5, 2, 5);
        add(player1Panel, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        add(matrixPanel, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(player2Panel, c);

        setPlayerComponents();
        setSize(new Dimension(975, 630));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setPlayerComponents(){
        if(!isPlayer1){
            turnHandOff(player1Panel, player1tiles, player2Panel, player2tiles);
        }else{
            turnHandOff(player2Panel, player2tiles, player1Panel, player1tiles);
        }
    }

    private void turnHandOff(JPanel player1Panel, ArrayList<ScrabbleTile> player1tiles, JPanel player2Panel, ArrayList<ScrabbleTile> player2tiles) {
        for(Component c: player1Panel.getComponents()){
            c.setEnabled(false);
        }
        for(ScrabbleTile t: player1tiles){
            t.setBackground(Color.lightGray);
        }
        for(Component c: player2Panel.getComponents()){
            c.setEnabled(true);
        }
        for(ScrabbleTile t: player2tiles){
            t.setBackground(Color.green);
        }
    }

    public static void main(String[] args) {
        ScrabbleFrame f = new ScrabbleFrame("Scrabble Game");
    }

    public void exportGame(String filename){
        try {
            model.save(filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Game not found");
            throw new RuntimeException(e);
        }
    }

    public BoardModel importGame(String filename){
        try {
            this.model = model.load(filename);
            return this.model;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private void updateTray() {
        for(int i = 0; i < 7; i ++){
            player1tiles.get(i).changeValue(trayPlayer1.getTray().get(i));
            player2tiles.get(i).changeValue(trayPlayer2.getTray().get(i));
        }
    }

    private void updateCells(Character[][] matrix) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                cells[i][j].setC(matrix[i][j]);
            }
        }
    }

    private void updateScore() {
        scorePlayer1.setText("Score :" + this.player1Score);
        scorePlayer2.setText("Score :" + this.player2Score);
    }


    @Override
    public void update(ScrabbleEvent e) {
        this.model = e.getM();
        this.player1Score = e.getPlayer1Score();
        this.player2Score = e.getPlayer2Score();
        this.isPlayer1 = e.isPlayer1();
        this.trayPlayer1 = e.getTrayPlayer1();
        this.trayPlayer2 = e.getTrayPlayer2();
        isAIplaying = e.isAIplaying();
        System.out.println("now in update " + isPlayer1);
        updateTray();
        updateCells(e.getMatrix());
        updateScore();
        setPlayerComponents();
        if(!isUndo) {
            playedEvents.push(e);
        }
        if(isUndo){
            model.printMatrix();
        }
    }
}
