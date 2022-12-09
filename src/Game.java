// Imports
import java.io.*;

/**
 * The following class is used as the primary injection point for the Scrabble game.
 * @author Kathan and Merek
 * @version 1.0
 *
 * The primary purpose of the following class is used to check if there was a previous instance.
 * If there was, the user is prompted via cli if they want to continue the previous game or if
 * they want to start a new game instance. Old versions will not be discarded but rather simply
 * overwritten.
 *
 * Otherwise, this is a simple wrapper class that handles loading game states.
 */
public class Game {

    /**
     * The following is used for checking if an instance is available. If it is,
     * the user is prompted otherwise a new session is created.
     * @throws IOException some sort of IO error.
     */
    public Game() throws IOException {

        // Check if there is a previous instance.
        if (new File("instance.ser").isFile()) {
            System.out.println("A game instance has been found. Would you like to continue? (y/n)");

            // Get user input.
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = br.readLine();

            // We only need to handle if the user wants to otherwise we can just create a new instance.
            if (input.equals("y")) {
                ScrabbleFrame previous = this._loadInstance();
                previous.setVisible(true);
            } else {
                this._newInstance();
            }
        } else {
            this._newInstance();
        }
    }

    /**
     * The following is used for loading a new instance.
     */
    private void _newInstance() {
        System.out.println("Starting a new game instance.");
        // Let's start the new instance
        new ScrabbleFrame("Scrabble Game");
    }

    /**
     * The following is used for loading a previous instance.
     * @return the previous instance of the game.
     */
    private ScrabbleFrame _loadInstance() {
        try {
            // Let's get the file and build the stream.
            FileInputStream file = new FileInputStream("instance.ser");
            ObjectInputStream in = new ObjectInputStream(file);

            // Read the incoming object
            ScrabbleFrame b = (ScrabbleFrame) in.readObject();

            // Close the stream and file.
            in.close();
            file.close();

            System.out.println("Object has been deserialized... ");
            return b;
        }

        // This operation can cause errors so lets catch anything that might cause a problem.
        catch(Exception e) {
            System.out.println("Exception is caught: " + e);
        }

        // This should only ever be hit if we errored out.
        return null;
    }

    /**
     * The following is used as the injection point for the game.
     * @param args {String[]} We don't use any cli arguments.
     */
    public static void main(String[] args) {
        try {
            // Lets run the instance checker.
            new Game();
        } catch (IOException e) {
            // We'll just handle the errors form the constructor if we are trying to load a previous instance.
            e.printStackTrace();
        }
    }
}