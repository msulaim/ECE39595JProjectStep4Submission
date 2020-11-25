package game;
public class ChangedDisplayType extends CreatureAction{
    private String name;
    public ChangedDisplayType(String _name, Creature _owner){
        super(_owner, _name);
        name = _name;
    }
}
