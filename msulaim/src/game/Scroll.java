package game;
import java.util.ArrayList;
public class Scroll extends Item{
    private String name;
    private int room;
    private int serial;
    private ArrayList<ItemAction> itemaction = null;
    public Scroll(String _name){
        name = _name;
    }
    public void setID(int _room, int _serial){
        room = _room;
        serial = _serial;
    }
    public void setItemAction(ItemAction _itemaction){
        if (itemaction == null){
            itemaction = new ArrayList<ItemAction>();
        }
        itemaction.add(_itemaction);
    }
    public ArrayList<ItemAction> getItemAction(){
        return itemaction;
    } 
}
