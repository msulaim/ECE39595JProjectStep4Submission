package game;
import java.util.*;
public class Displayable{
    private int maxHit;
    private int hpMoves;
    private int Hp;
    private char t;
    private int v;
    private int xPos; 
    private int yPos;
    private Stack<Integer> xPosStack;
    private Stack<Integer> yPosStack;
    private int xWidth;
    private int yHeight;
    private boolean visible = false;
    private Room room;
    private String name;
    private boolean worn = false;
    private Stack<Item> pack = null;
    private boolean wield = false;
    public Displayable(){
    }
    public void setVisible(){
        visible = true;
    }
    public void setMaxHit(int _maxHit){
        maxHit = _maxHit;
    }
    public void setHpMove(int _hpMoves){
        hpMoves = _hpMoves;
    }
    public void setHp(int _Hp){
        Hp = _Hp;
    }
    public void setType(char _t){
        t = _t;
    }
    public void setIntValue(int _v){
        v = _v;
    }
    public void SetPosX(int _x){
        xPos = _x;    
    }
    public void SetStackPosX(int _x){
        if (xPosStack == null){
            xPosStack = new Stack <Integer>();
        }
        xPosStack.push(_x);
    }
    public void SetStackPosY(int _y){
        if (yPosStack == null){
            yPosStack = new Stack <Integer>();
        }
        yPosStack.push(_y);
    }
    public void SetPosY(int _y){
        yPos = _y;
    }
    public void SetWidth(int _x){
        xWidth = _x;
    }
    public void SetHeight(int _y){
        yHeight = _y;
    }
    public void setRoom(Room _room){
        room = _room;
    }
    public void setName(String str){
        name = str;
    }
    public void setWorn(boolean _worn){
        worn = _worn;
    }
     public void setWield(boolean _wield){
        wield = _wield;
    }
    public void setPack(Stack<Item> _pack){
       pack = _pack;
   }
   public void addtoPack(Item item){
       if (pack == null){
           pack = new Stack<Item>();
       }
       pack.push(item);
   }
   public Stack<Item> getPack(){
       return pack;
   }
     public int getposX(){
       return xPos;
    }
    public Stack getStackX(){
        return xPosStack;
    }
    public int getposY(){
        return yPos;
    }
    public Stack getStackY(){
        return yPosStack;
    }
    public int getWidth(){
        return xWidth;
    }
    public int getHeight(){
        return yHeight;
    }
    public char getType(){
        return t;
    }
    public int getHp(){
        return Hp;
    }
    public int getMaxHits(){
        return maxHit;
    }
    public int getHpMoves(){
        return hpMoves;
    }
    public boolean getVisible(){
        return visible;
    }
    public int getItemIntValue(){
        return v;
    }
    public Room getRoom(){
        return room;
    }
    public String getName(){
        return name;
    }
    public boolean getWorn(){
        return worn;
    }
    public boolean getWield(){
        return wield;
    }
}
