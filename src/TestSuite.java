// Import JUnit classes
import com.sun.tools.jconsole.JConsoleContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TestSuite {

    public void TestSuite() {
        System.out.println("Running test suite...");
    }

    // A test case
    @Test
    public void testWord() {
        ScrabbleDictionary d = new ScrabbleDictionary();
        assert(d.isWord("hello"));
        assert(!d.isWord("qwertyuiopopasdfgjklzxcvbnm"));
        System.out.println("Test passed");
    }

    @Test
    public void testLetterCount() {
        ScrabbleDictionary d = new ScrabbleDictionary();
        HashMap<String, Integer> map = d.countLetters("hello");
        assert(map.get("h") == 1);
        assert(map.get("e") == 1);
        assert(map.get("l") == 2);
        assert(map.get("o") == 1);
        System.out.println("Test passed");
    }

    @Test
    public void testMappedWords() {
        ScrabbleDictionary d = new ScrabbleDictionary();
        HashMap<HashMap, String> map = d.getMappedWords();
        assert(map.get(d.countLetters("hello")).equals("hello"));
        assert(map.get(d.countLetters("world")).equals("world"));
        System.out.println("Test passed");
    }

    @Test
    public void testBag() {
        Bag b = new Bag();
        assert(b.getTray().size() == 7);
        System.out.println("Test passed");
    }

    @Test
    public void testTileScoreManager() {
        TileScoreManager t = new TileScoreManager();
        assert(t.getTilePoints('a') == 1);
        assert(t.getTilePoints('b') == 3);
        assert(t.getTilePoints('c') == 3);
        assert(t.getTilePoints('d') == 2);
        assert(t.getTilePoints('e') == 1);
        assert(t.getTilePoints('f') == 4);
        assert(t.getTilePoints('g') == 2);
        assert(t.getTilePoints('h') == 4);
        assert(t.getTilePoints('i') == 1);
        assert(t.getTilePoints('j') == 8);
        assert(t.getTilePoints('k') == 5);
        assert(t.getTilePoints('l') == 1);
        assert(t.getTilePoints('m') == 3);
        assert(t.getTilePoints('n') == 1);
        assert(t.getTilePoints('o') == 1);
        assert(t.getTilePoints('p') == 3);
        assert(t.getTilePoints('q') == 10);
        assert(t.getTilePoints('r') == 1);
        assert(t.getTilePoints('s') == 1);
        assert(t.getTilePoints('t') == 1);
        assert(t.getTilePoints('u') == 1);
        assert(t.getTilePoints('v') == 4);
        assert(t.getTilePoints('w') == 4);
        assert(t.getTilePoints('x') == 8);
        assert(t.getTilePoints('y') == 4);
        assert(t.getTilePoints('z') == 10);
        System.out.println("Test passed");
    }

    public void testTray() {
        Tray t = new Tray(new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g')), new Bag());
        assert(t.getTray().size() == 7);
        System.out.println("Test passed");
    }

    public static void main(String[] args) {
        TestSuite test = new TestSuite();
        test.testWord();
        test.testLetterCount();
        test.testMappedWords();
        test.testBag();
        test.testTileScoreManager();
        test.testTray();
    }
}
