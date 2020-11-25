package game;
public class Room extends Structure{
    
    Creature Monster;
    private int id;
    public Room(String str){
    }
    public void setId(int room){
        
        id = room;
    }
    public void setCreature(Creature _Monster){
        Monster = _Monster;
        
    }

}
