
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private ArrayList<Character> arrayOfLetters, tray;

    public Bag() {
        super();
        this.arrayOfLetters = new ArrayList<>();
        arrayOfLetters.addAll(List.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E',
                'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I',
                'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O',
                'N', 'N', 'N', 'N', 'N', 'N',
                'R', 'R', 'R', 'R', 'R', 'R',
                'T', 'T', 'T', 'T', 'T', 'T',
                'L', 'L', 'L', 'L',
                'S', 'S', 'S', 'S',
                'U', 'U', 'U', 'U',
                'D', 'D', 'D', 'D',
                'G', 'G', 'G',
                'B', 'B',
                'C', 'C',
                'M', 'M',
                'P', 'P',
                'F', 'F',
                'H', 'H',
                'V', 'V',
                'W', 'W',
                'Y', 'Y',
                'K', 'J', 'X', 'Q', 'Z', ' ', ' '));
        Collections.shuffle(this.arrayOfLetters);
    }

    public ArrayList getTray(){
        this.tray = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            if(this.arrayOfLetters.isEmpty()){
                return null;
            }else{
                this.tray.add(Character.toLowerCase(this.arrayOfLetters.remove(this.arrayOfLetters.size() - 1)));
            }
        }
        return this.tray;
    }
    public ArrayList testTray(){
        this.tray = new ArrayList<>();
        tray.addAll(List.of('g','o','d','o','g','l','e'));
        return tray;
    }
    public void returnAlphabet(char c){
        this.arrayOfLetters.add(Character.toUpperCase(c));
    }
    public int getSize(){
        return this.arrayOfLetters.size();
    }
    public Character getNewAlphabet(){
        return Character.toLowerCase(this.arrayOfLetters.remove(this.arrayOfLetters.size()-1));
    }
}
