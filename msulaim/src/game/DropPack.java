package game;
public class DropPack extends CreatureAction{
    private String name;
    public DropPack(String _name, Creature _owner){
        super(_owner,_name);
        name = _name;
    }
}
