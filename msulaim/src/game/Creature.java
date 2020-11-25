package game;
import java.util.ArrayList;
public class Creature extends Displayable{
    private int hp;
    private int hpMoves;
    private int hitAction;
    public Creature (){
    }
    
    @Override
    public void setHp(int _hp){
        hp = _hp;
    }
    @Override
    public void setHpMove(int _hpm){
        hpMoves = _hpm;
    }
    public void setHitAction(int _ha){
        hitAction = _ha;
    }
    @Override
    public int getHpMoves(){
        return hpMoves;
    }
    @Override
    public int getHp(){
        return hp;
    }
    
}
