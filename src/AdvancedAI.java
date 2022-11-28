import Developer.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AdvancedAI {
    private Character[][] board;
    private Utils utils = new Utils();
    private Tray tray;
    private ScrabbleDictionary dictionary;
    private TileScoreManager tileScoreManager = new TileScoreManager();


    public void boardHandoff(Character[][] board, Tray tray, ScrabbleDictionary dictionary){
        this.board = board;
        this.tray = tray;
        this.dictionary = dictionary;
    }

    private boolean isWordValid(ArrayList<String> payload){
        // Check if we can add a letter to the word in the direction to make a valid word
        return true;
    }

    public void printMatrix() {
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                for (int k = 0; k < 15; k++) {
                    if (k == 0) {
                        System.out.print("\t");
                    }
                    System.out.print("\t" + k);
                }
                System.out.print("\n\n\n");
            }
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print(i + "\t");
                }
                System.out.print("\t" + this.board[i][j] + "");
                if (j + 1 == 15) {
                    System.out.print("\n");
                }
            }
        }
    }

    private void getPossibleTiles(){
        // Move over every tile and check around it to see if it is part of a word.
        // If it is, then add it to a list of possible tiles.

        ArrayList<String> payload = new ArrayList<String>();
        Map<String, ArrayList<String>> possibleTiles = new HashMap<String, ArrayList<String>>();
        Map<String, ArrayList<String>> actualTiles = new HashMap<String, ArrayList<String>>();
        for (int x = 0; x < this.board.length; x++) {
            for (int y = 0; y < this.board[x].length; y++) {
                // Define the current tile

                String word = "";
                Integer yPos = -1, xPos = -1;

                if (this.board[x][y] == null || this.board[x][y] == ' ' || this.board[x][y] == '\0') {
                    try {
                    // This tile is empty check if a word can be made here.
                    if (this.board[x][y-1] != null && this.board[x][y-1] != ' ' && this.board[x][y-1] != '\0') {
                        utils.devPrint("Found a tile above this one.");

                        // Build the word above this tile.
                        int i = 1;
                        word = "";
                        xPos = x;
                        yPos = y-i;

                        this.printMatrix();

                        while (this.board[x][y-i] != null && this.board[x][y-i] != ' ' && this.board[x][y-i] != '\0') {
                            word = this.board[x][y-i] + word;
                            i++;
                        }

                        if (word.length() >= 1) {
                            for (Character c : this.tray.getTray()) {
                                    String potentialWord = c.toString() + word;
                                    System.out.println(potentialWord);
                                    payload.add(potentialWord);
                                    payload.add(c + "");
                                    payload.add(xPos.toString());
                                    payload.add(yPos.toString());
                                    possibleTiles.put(potentialWord, payload);
                                    payload = new ArrayList<String>();
                                System.out.println(payload.toString());
                            }
                        } else {
                            word = "";
                            xPos = -1;
                            yPos = -1;
                        }
                    } else if (this.board[x][y+1] != null && this.board[x][y+1] != ' ' && this.board[x][y+1] != '\0') {
                        utils.devPrint("Found a tile below this one.");

                        // Build the word below this tile.
                        int i = 1;
                        word = "";
                        xPos = x;
                        yPos = y+i;

                        this.printMatrix();

                        while (this.board[x][y+i] != null && this.board[x][y+i] != ' ' && this.board[x][y+i] != '\0') {
                            word = word + this.board[x][y+i];
                            i++;
                        }

                        if (word.length() >= 1) {
                            for (Character c : this.tray.getTray()) {
                                    String potentialWord = c.toString() + word;
                                    System.out.println(potentialWord);
                                    payload.add(potentialWord);
                                    payload.add(c + "");
                                    payload.add(xPos.toString());
                                    payload.add(yPos.toString());
                                    possibleTiles.put(potentialWord, payload);
                                payload = new ArrayList<String>();
                                System.out.println(payload.toString());
                            }
                        } else {
                            word = "";
                            xPos = -1;
                            yPos = -1;
                        }
                    } else if (this.board[x-1][y] != null && this.board[x-1][y] != ' ' && this.board[x-1][y] != '\0') {
                        utils.devPrint("Found a tile to the left of this one.");

                        // Build the word to the left of this tile.
                        int i = 1;
                        word = "";
                        xPos = x-i;
                        yPos = y;

                        this.printMatrix();

                        while (this.board[x-i][y] != null && this.board[x-i][y] != ' ' && this.board[x-i][y] != '\0') {
                            word = this.board[x-i][y] + word;
                            i++;
                        }

                        if (word.length() >= 1) {
                            for (Character c : this.tray.getTray()) {
                                    String potentialWord = c.toString() + word;
                                    System.out.println(potentialWord);
                                    payload.add(potentialWord);
                                    payload.add(c + "");
                                    payload.add(xPos.toString());
                                    payload.add(yPos.toString());
                                    possibleTiles.put(potentialWord, payload);
                                payload = new ArrayList<String>();
                                System.out.println(payload.toString());
                            }
                        } else {
                            word = "";
                            xPos = -1;
                            yPos = -1;
                        }
                    } else if (this.board[x+1][y] != null && this.board[x+1][y] != ' ' && this.board[x+1][y] != '\0') {
                        utils.devPrint("Found a tile to the right of this one.");

                        // Build the word to the right of this tile.
                        int i = 1;
                        word = "";
                        xPos = x + i;
                        yPos = y;

                        this.printMatrix();

                        while (this.board[x + i][y] != null && this.board[x + i][y] != ' ' && this.board[x + i][y] != '\0') {
                            word = word + this.board[x + i][y];
                            i++;
                        }

                        if (word.length() >= 1) {
                            for (Character c : this.tray.getTray()) {
                                    String potentialWord = c.toString() + word;
                                    System.out.println(potentialWord);
                                    payload.add(potentialWord);
                                    payload.add(c + "");
                                    payload.add(xPos.toString());
                                    payload.add(yPos.toString());
                                    possibleTiles.put(potentialWord, payload);
                                payload = new ArrayList<String>();
                                    System.out.println(payload.toString());
                            }
                        } else {
                            word = "";
                            xPos = -1;
                            yPos = -1;
                        }
                    }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Gracefully handle the exception.
                        this.utils.devPrint("Out of bounds, next move.");
                    }
                }
            }
        }
        possibleTiles.forEach((k, v) -> {
            if (this.dictionary.isWord(k)) {
                this.utils.devPrint("Found a word: " + k);
                this.utils.devPrint("Payload: " + v.toString());
                actualTiles.put(k, v);
            }
           else {
                this.utils.devPrint("Not a word: " + k);
            }
        });

        AtomicReference<String> bestestWord = new AtomicReference<>("");
        AtomicReference<String> usingLetter = new AtomicReference<>("");
        AtomicReference<Integer> xAct = new AtomicReference<>(Integer.valueOf(1));
        AtomicReference<Integer> yAct = new AtomicReference<>(Integer.valueOf(1));
        AtomicReference<Integer> score = new AtomicReference<>(0);

        // get the highest scoring word
        actualTiles.forEach((k1, v1) -> {
            if (this.tileScoreManager.getTilePoints(v1.get(1).charAt(0)) > score.get()) {
                bestestWord.set(k1);
                usingLetter.set(v1.get(1));
                xAct.set(Integer.parseInt(v1.get(2)));
                yAct.set(Integer.parseInt(v1.get(3)));
                score.set(this.tileScoreManager.getTilePoints(v1.get(1).charAt(0)));
            }

        });

        this.utils.devPrint("Best word: " + bestestWord.get());

    }

    public void makeMove(){
        this.utils.devPrint("AI is making a move on current board frame... standby.");
        this.getPossibleTiles();

    }
}
