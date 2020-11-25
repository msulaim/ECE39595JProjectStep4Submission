package game;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
public class Hallucinate extends ItemAction implements InputObserver{
     private ObjectDisplayGrid displayGrid;
     private static Queue<Character> inputQueue = null;
     private int numMoves;
     private boolean startHallucinate = false;
     public Hallucinate(Item _owner, String _name){
        super(_owner, _name);
    }
    
    public Hallucinate(ObjectDisplayGrid grid, Item _owner, String _name, int _numMoves) {
        super(_owner, _name);
        displayGrid = grid;
        displayGrid.registerInputObserver(this);        
        numMoves = _numMoves;
        inputQueue = new ConcurrentLinkedQueue<>();
        startHallucinate = true;
    }

    public boolean observerUpdate(char ch) {
        boolean remove = false;
        if (inputQueue.size() != numMoves){
            if (ch == 'h'|| ch == 'j' || ch == 'k' || ch == 'l'){
                inputQueue.add(ch);        
                displayGrid.hallucinate();
            }  
         }
         else{
             displayGrid.unhallucinate();
             remove = true;
        }
        return remove;
    }
}
