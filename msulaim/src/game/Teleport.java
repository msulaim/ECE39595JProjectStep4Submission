package game;
public class Teleport extends CreatureAction{
    private String name;
    public Teleport(String _name, Creature _owner){
        super(_owner, _name);
        name = _name;
    }
}
