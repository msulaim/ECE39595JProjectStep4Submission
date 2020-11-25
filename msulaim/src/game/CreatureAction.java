package game;
public class CreatureAction extends Action{
    Creature owner;
    private String name;
    public CreatureAction(Creature _owner, String _name){
        owner = _owner;
        name = _name;   
    }
    public String getName(){
        return name;
    }
    public Creature getOwner(){
        return owner;
    }
}
