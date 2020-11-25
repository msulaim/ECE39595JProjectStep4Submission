package game;
public class UpdateDisplay extends CreatureAction{
    private String name;
    public UpdateDisplay(String _name, Creature _owner){
        super(_owner,_name);
        name = _name;
    }
}
