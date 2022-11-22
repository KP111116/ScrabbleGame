
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private ArrayList<Character> arrayOfLetters, tray;

    public Bag() {
        arrayOfLetters = new ArrayList<>();
        this.arrayOfLetters.addAll(List.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
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
                'K', 'J', 'X', 'Q', 'Z', ' ', ' ' ));
        Collections.shuffle(arrayOfLetters);
    }

    public ArrayList getTray(){
        tray = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            if(arrayOfLetters.isEmpty()){
                return null;
            }else{
                tray.add(Character.toLowerCase(arrayOfLetters.remove(arrayOfLetters.size() - 1)));
            }
        }
        return tray;
    }
    public int getSize(){
        return arrayOfLetters.size();
    }
    public Character getNewAlphabet(){
        return Character.toLowerCase(arrayOfLetters.remove(arrayOfLetters.size()-1));
    }
}
