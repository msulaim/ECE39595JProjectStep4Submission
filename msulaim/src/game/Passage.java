package game;

public class Passage extends Structure{
    private String name;
    private int id1;
    private int id2;
    public Passage(){
    }
    public void setID(int room1, int room2){
        id1 = room1;
        id2 = room2;        
    } 
    public int getID(){
        return id1;
    }
}
