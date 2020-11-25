package game;
public class Action{;
    private String msg;
    int v;
    char c;
    public void setMessage(String _msg){
        msg = _msg;
    }
    public void setIntValue(int _v){
        v = _v;
    }
     public void setCharValue(char _c){
        c = _c;
    }
    public String getMessage(){
        return msg;
    }
    public int getIntValue(){
        return v;
    }
    public char getChar(){
        return c;
    }
}
