package game;
public class Floor extends Displayable{
    private int floorX;
    private int floorY;
    private int num = 1;
    public Floor(int _x, int _y){
        floorX = _x;
        floorY =_y;
    }
    public int getFloorX(){
        return floorX;
    }
    public int getFloorY(){
        return floorY;
    }
    public void increment(){
        num++;
    }
    public int getNum(){
        return num;
    }
    public void decrement(){
        num--;
    }
}