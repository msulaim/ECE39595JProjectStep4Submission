package game;
import java.util.ArrayList;
import java.util.Stack;
public class Player extends Creature{
    private Item sword;
    private Item armor;
    private String name;
    private int serial;
    private int id;
    private ArrayList<CreatureAction> creatureaction = null;
    public Player(){
    }
    public void setID(int room, int _serial){
        id = room;
        serial = _serial;
    }    
    public void setWeapon(Item _sword){
        sword = _sword;
    }
    public void setArmor(Item _armor){
        armor = _armor;
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
