package game;
import java.util.ArrayList;
public class Monster extends Creature{
    private String name;
    private int serial;
    private int id;
    private ArrayList<CreatureAction> creatureaction = null;
    public Monster(){
    }
    public void setID(int room, int _serial){
        id = room;
        serial = _serial;
    }
     public void setCreatureAction(CreatureAction _creatureaction){
        if (creatureaction == null){
            creatureaction = new ArrayList<CreatureAction>();
        }
        creatureaction.add(_creatureaction);
    }
        public ArrayList<CreatureAction> getCreatureAction(){
        return creatureaction;
    } 

}
