import java.io.Serializable;
import java.util.ArrayList;

public class Tray implements Serializable {
    private ArrayList<Character> tray;
    private Bag b;

    public Tray(ArrayList<Character> tray, Bag b) {
        this.tray = tray;
        this.b = b;
    }
    public void refill(){
        while(this.tray.size() < 7){
            this.tray.add(this.b.getNewAlphabet());
        }
    }
    private void addAlphabet(Character _alphabet){
        if(this.tray.size() <= 7) {
            this.tray.add(_alphabet);
        }
    }
    public boolean swapAlphabet(Character c){
        if(this.contains(c)){
            tray.remove(tray.indexOf(c));
            boolean flag = true;
            b.returnAlphabet(c);
            Character newChar;
            while(flag) {
                newChar = b.getNewAlphabet();
                if(newChar == c){
                    flag = true;
                    b.returnAlphabet(newChar);
                }else {
                    this.tray.add(newChar);
                    return true;
                }
            }
        }
        return false;
    }
    public int getSize(){
        return this.tray.size();
    }

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
