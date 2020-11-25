package game;
public class ItemAction extends Action{
    Item owner;
    private String name;
    public ItemAction(Item _owner, String _name){
        name = _name;
        owner = _owner;
    }
public String getName(){
        return name;
    }
public Item getOwner(){
    return owner;
}
}
