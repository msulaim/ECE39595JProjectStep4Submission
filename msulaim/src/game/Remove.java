package game;
public class Remove extends CreatureAction{
    private String name;
    public Remove(String _name, Creature _owner){
        super(_owner, _name);
        name = _name;
    }
}
