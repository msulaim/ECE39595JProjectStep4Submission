package game;
import java.util.*;
public class Dungeon{
    private int roomCount = 0;
    private int creatureCount = 0;
    private int passageCount = 0;
    private int itemCount = 0;
    private ArrayList<Room> rooms = null;
    private ArrayList<Creature> creatures = null;
    private ArrayList<Passage> passages = null;
    private ArrayList<Item> items = null;
    private String name;
    private int width;
    private int topHeight;
    private int gameHeight;
    private int bottomHeight;
    public Dungeon (String _name, int _width, int _topHeight, int _gameHeight, int _bottomHeight){
        name = _name;
        width = _width;
        topHeight = _topHeight;
        gameHeight = _gameHeight;
        bottomHeight = _bottomHeight;
        rooms = new ArrayList<Room>();
        creatures = new ArrayList<Creature>();
        passages = new ArrayList<Passage>();
        items = new ArrayList<Item>();
    }
    public void addRoom(Room room){
        rooms.add(room);
        roomCount++;
    }
    public void addCreature(Creature creature){
        creatures.add(creature);
        creatureCount++;
    }
    public void addPassage(Passage passage){
        passages.add(passage);
        passageCount++;
    }
    public void addItem(Item item){
        items.add(item);
        itemCount++;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return gameHeight;
    }
    public int getTopHeight(){
        return topHeight;
    }
    public int getBotHeight(){
        return bottomHeight;
    }
    public ArrayList<Room> getRooms(){
        return rooms;
    }
    public ArrayList<Creature> getCreatures(){
        return creatures;
    }
    public ArrayList<Item> getItems(){
        return items;
    }
    public ArrayList<Passage> getPassages(){
        return passages;
    }
}
