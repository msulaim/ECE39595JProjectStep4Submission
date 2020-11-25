package game;
public class YouWin extends CreatureAction{
    private String name;
    public YouWin(String _name, Creature _owner){
        super(_owner, _name);
        name = _name;
    }
}
