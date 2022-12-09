import java.io.Serializable;
import java.util.ArrayList;

public class Tray implements Serializable {
    private ArrayList<Character> tray;
    private Bag b;

    public Tray(ArrayList<Character> tray, Bag b) {
        super();
        this.tray = tray;
        this.b = b;
    }
    public void refill(){
        while(this.tray.size() <= 7){
            this.tray.add(this.b.getNewAlphabet());
        }
    }
    public void addAlphabet(Character _alphabet){
        if(this.tray.size() <= 7) {
            this.tray.add(_alphabet);
        }
    }
    public int getSize(){
        return this.tray.size();
    }

    //checks if the created word is in the tray
    public boolean checkString(char s){
        return tray.contains(s);
    }
    public boolean contains(Character _alphabet){
        return this.tray.contains(_alphabet);
    }
    public ArrayList<Character> getTray(){
        return tray;
    }
    public boolean removeAlphabet(Character _alphabet){
        return this.tray.remove(_alphabet);
    }
    public String toString(){
        return this.tray.toString();
    }
    
}
