package game;
public class EndGame extends CreatureAction{
    private String name;
    public EndGame(String _name, Creature _owner){
        super(_owner, _name);
        name = _name;
    }
}
