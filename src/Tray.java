import java.util.ArrayList;

public class Tray {
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
    public boolean swapAlphabet(char c){
        if(this.contains(c)){
            b.returnAlphabet(c);
            this.tray.add(b.getNewAlphabet());
            return true;
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
