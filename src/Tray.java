import java.util.ArrayList;

public class Tray {
    private ArrayList<Character> tray;
    private Bag b;

    public Tray(ArrayList<Character> tray, Bag b) {
        this.tray = tray;
        this.b = b;
    }
    public void refill(){
        while(tray.size() <= 7){
            tray.add(b.getNewAlphabet());
        }
    }
    public void addAlphabet(Character _alphabet){
        if(tray.size() <= 7) {
            tray.add(_alphabet);
        }
    }
    public int getSize(){
        return tray.size();
    }
    public boolean contains(Character _alphabet){
        return tray.contains(_alphabet);
    }
    public ArrayList<Character> getTray(){
        return this.tray;
    }
    public boolean removeAlphabet(Character _alphabet){
        return tray.remove(_alphabet);
    }
    public String toString(){
        return tray.toString();
    }
    
}
