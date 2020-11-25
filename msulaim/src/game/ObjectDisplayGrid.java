package game;
import asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.*;
public class ObjectDisplayGrid extends JFrame implements KeyListener , InputSubject{
    private static final int DEBUG = 0;
    private List<InputObserver> inputObservers = null;
    private static int gameHeight;
    private static int gameWidth;
    private static int topHeight;
    private static int botHeight;
    private int playerX;
    private int playerY;
    private Creature player;
    private boolean playerDead = false;
    private int numMoves = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";
    private static AsciiPanel terminal;
    private static Stack<Character>[][] objectGrid = null;
    private static Stack<Displayable>[][] objectGridDisplayable = null;
    private Stack<Item> pack = null;
    private static ArrayList<Floor> floors = null;
    private int wearingIndex = -1;
    private int wieldingIndex = -1;
    private static boolean startHallucinate = false;
    public ObjectDisplayGrid(int _width,int _gameHeight,int _topHeight, int _botHeight, Dungeon dungeon){
        gameHeight = _gameHeight;
        gameWidth = _width;
        topHeight = _topHeight;
        botHeight = _botHeight;
        int height = gameHeight + botHeight + topHeight;
        terminal = new AsciiPanel(gameWidth, height);
        objectGrid = new Stack [gameWidth][height];
        objectGridDisplayable = new Stack[gameWidth][height];
        floors = new ArrayList<Floor>();
        super.add(terminal);
        super.setSize(gameWidth * 9, (height) * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
        terminal.setVisible(true);
        super.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

    private void notifyInputObservers(char ch) {
        Iterator i =  inputObservers.iterator();
        while(i.hasNext()){
            InputObserver inputObserver =(InputObserver) i.next();
            if (inputObserver.observerUpdate(ch)){
                i.remove();
            }
        }
    }

    public void fireUp() {
        if (terminal.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }
     @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }
    public void updateDisplay(char ch){
        int posX = getPlayerX();
        int posY = getPlayerY();
        Displayable player = objectGridDisplayable[posX][posY].peek();
        pack = player.getPack();
        if (pack == null){
            pack = new Stack<Item>();
            player.setPack(pack);
        }
        if (numMoves >= player.getHpMoves()){
            player.setHp(player.getHp()+1);
            numMoves = 0;
        }
        if (playerDead == false ){
        switch(ch){
                case 'h':
                    if(addObjectToDisplay(posX - 1, posY, '@', objectGridDisplayable[posX][posY].peek())){
                        objectGrid[posX][posY].pop();
                        objectGridDisplayable[posX][posY].pop();
                        writeToTerminal(posX,posY);
                    }
                    break;
                case 'l':
                    if(addObjectToDisplay(posX + 1, posY, '@', objectGridDisplayable[posX][posY].peek())){
                        objectGrid[posX][posY].pop();
                        objectGridDisplayable[posX][posY].pop();
                        writeToTerminal(posX,posY);
                    }
                    break;
                case 'k':
                    if(addObjectToDisplay(posX, posY - 1, '@', objectGridDisplayable[posX][posY].peek())){
                        objectGrid[posX][posY].pop();
                        objectGridDisplayable[posX][posY].pop();
                        writeToTerminal(posX,posY);    
                    
                    }
                    break;
                case 'j':
                    if(addObjectToDisplay(posX, posY + 1, '@',objectGridDisplayable[posX][posY].peek())){
                        objectGrid[posX][posY].pop();
                        objectGridDisplayable[posX][posY].pop();
                        writeToTerminal(posX,posY);
                    }
                    break;
                case 'p':
                    if (objectGridDisplayable[posX][posY].get(objectGridDisplayable[posX][posY].size()-2) instanceof Item){
                    objectGrid[posX][posY].remove( objectGrid[posX][posY].size()-2);
                    Item itemPick = (Item)objectGridDisplayable[posX][posY].remove( objectGridDisplayable[posX][posY].size()-2);
                    pack.push(itemPick);
                    }
                    else{
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:Nothing to pick",0, topHeight+gameHeight+1);
                        terminal.repaint();
                    }
                    break;
                case 'c':
                    if (wearingIndex != -1){
                        Item item  = pack.get(wearingIndex);
                        item.setWorn(false);
                        wearingIndex = -1;
                    }
                    else{
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:No Armor is being worn",0, topHeight+gameHeight+1);
                        terminal.repaint();
                    }
                    break;
                case 'i':
                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                    String str = new String("");
                    if (pack.empty()!= true){
                        str += "Info:Inventory["+pack.size()+" Items"+"]"+": ";
                        for (int i = 0 ; i < pack.size(); i++){
                            Item item = pack.get(i);
                            if (item instanceof Scroll){
                                str += " ["+(i+1)+"]"+item.getName()+": "+"'"+item.getType()+"'";
                            }
                            else{
                                if (item.getWorn()){
                                        str += " ["+(i+1)+"]"+item.getName()+": "+"'"+'a'+"'";
                                }
                                else if (item.getWield()){
                                    str += " ["+(i+1)+"]"+item.getName()+": "+"'"+'w'+"'";
                                }
                                else{
                                    str += " ["+(i+1)+"]"+item.getName()+": "+"'"+item.getItemIntValue()+"'";
                                }
                            }
                        }
                    }
                    else{
                        str += "Info:Inventory is Empty";
                    }
                    terminal.repaint();
                    terminal.write(str,0, topHeight+gameHeight+1);
                    break;
                case '?':
                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                    terminal.write("Info:h,l,k,j,?,c,d,p,r,T,w,E,0-9.H<cmd> for more info",0, topHeight+gameHeight+1);
                    terminal.repaint();
                    break;
                default:
                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                    terminal.write("Info:Invalid Command",0, topHeight+gameHeight+1);
                    terminal.repaint();
                    break;
        }
      
      }
      else{
           System.out.println("Your Player is dead, the game is over"); 
        }
}
    public void endGame(){
            terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
            terminal.write("Info:Do you want to end game: Y|y ?",0, topHeight+gameHeight+1);
            terminal.repaint();
    }
    public void endGame(char c){
            terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
            if ( c == 'Y' || c == 'y'){
                terminal.write("Info:You ended the game",0, topHeight+gameHeight+1);
                terminal.repaint();
            }
            else{
                terminal.write("Info:You did not end the game, continue playing",0, topHeight+gameHeight+1);
                terminal.repaint();
            }
    }
    public void help(char c){
        switch(c){
            case 'h':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(h) move left 1 space",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'l':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(l) move right 1 space",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'k':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(k) move up 1 space",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'j':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(j) move down 1 space",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'i':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(i) inventory, show pack contents",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'c':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(c) take off/change amror",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'd':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(d) drop<item number> item from pack",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'p':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(p) pick up item from under player and put it into pack",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'r':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(r) read<scroll number> scroll in pack",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'T':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(T) take out<weapon number> weapon from pack",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            case 'w':
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:(w) wear <armor number> armor",0, topHeight+gameHeight+1);
                terminal.repaint();  
                break;
            default:
                terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                terminal.write("Info:Invalid command, press ? to see list of commands",0, topHeight+gameHeight+1);
                terminal.repaint();
                break;
        }
    }
    public void scrollActions(Scroll scroll, int scrollIndex){
        ArrayList<ItemAction> itemactions = scroll.getItemAction();
        for (ItemAction action : itemactions){
            String name = action.getName();
            switch(name){
                case "BlessArmor":
                    char c = action.getChar();
                    if (c == 'a' && wearingIndex != -1){
                        Armor armor = (Armor)pack.get(wearingIndex);
                        int value = action.getIntValue();
                        armor.setIntValue(armor.getItemIntValue() + value);
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:"+armor.getName()+" cursed!"+value+" taken from its effectivness",0, topHeight+gameHeight+1);
                        terminal.repaint();
                        terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                        terminal.write("ActionMessage:"+action.getMessage(),0, topHeight+gameHeight);
                        terminal.repaint();
                        if (scrollIndex+1 == pack.size()){
                            pack.remove(scrollIndex);
                        }
                        else{
                            pack.remove(scrollIndex);
                            wearingIndex = scrollIndex;
                        }
                    }
                    else if(c == 'w' && wieldingIndex != -1){
                        Sword sword = (Sword)pack.get(wieldingIndex);
                        int value = action.getIntValue();
                        sword.setIntValue(sword.getItemIntValue() + value);
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:"+sword.getName()+" cursed!"+value+" taken from its effectivness",0, topHeight+gameHeight+1);
                        terminal.repaint();
                        terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                        terminal.write("ActionMessage:"+action.getMessage(),0, topHeight+gameHeight);
                        terminal.repaint();
                        if (scrollIndex+1 == pack.size()){
                            pack.remove(scrollIndex);
                        }
                        else{
                            pack.remove(scrollIndex);
                            wieldingIndex = scrollIndex;
                        }
                    }
                    else{
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:"+scroll.getName()+" does nothing,used when the object is not worn or wielded",0, topHeight+gameHeight+1);
                        terminal.repaint();
                    }
                    break;
                case "Hallucinate":
                    startHallucinate = true;
                    Hallucinate hallucination = new Hallucinate(this,action.getOwner(), action.getName(), action.getIntValue());
                    terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                    terminal.write("ActionMessage:"+action.getMessage(),0, topHeight+gameHeight);
                    terminal.repaint();
                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                    terminal.write("Info:Hallucinations will continue for "+action.getIntValue()+" moves",0, topHeight+gameHeight+1);
                    terminal.repaint();
                    pack.remove(scrollIndex);
                    break;
            }
        }
    }
    public void hallucinate(){
        char[] arrChar = new char[]{'T','X', 'S', 'H', ']', '?', ')','.', '#', '+'};
        for (Floor floor : floors){
            int x = floor.getFloorX();
            int y = floor.getFloorY();
            if (objectGrid[x][y].peek() != '@'){
                Random rand = new Random();
                int randInd = rand.nextInt(arrChar.length);
                objectGrid[x][y].push(arrChar[randInd]);
                floor.increment();
                writeToTerminal(x,y);
            }
        }
    }
    public void unhallucinate(){
        for (Floor floor : floors){
            int x = floor.getFloorX();
            int y = floor.getFloorY();
            int popNum = floor.getNum();
            if (objectGrid[x][y].peek() != '@'){
                for (int i = 0 ; i < popNum - 1 ; i++){
                    objectGrid[x][y].pop();
                    writeToTerminal(x,y);
                    floor.decrement();
                }
            }
            else{
                playerX = x;
                playerY = y;
                for(int j = popNum - 1 ; j > 0 ; j--){
                    objectGrid[playerX][playerY].remove(j);
                }
                writeToTerminal(x,y);
            }
        }
    }
    public void read(char c){
       int index = 0;
       if (c >= '1' && c <= '9'){
            index = Integer.parseInt(String.valueOf(c)); 
        }
       if (pack.empty() != true){
            if (index-1 >= 0 && index - 1 < pack.size()){
                if (pack.get(index - 1) instanceof Scroll){
                    scrollActions((Scroll)pack.get(index-1), index-1);
                }
                else{
                     Item item = pack.get(index - 1);
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:"+item.getName()+" cannot be read,execute r<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();
                }
                
            }
            else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Invalid index,execute r<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();
            }
       }    
       else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Pack is empty",0, topHeight+gameHeight+1);
                     terminal.repaint();
        }
    } 
    public void wield(char c){
       int index = 0;
       if (c >= '1' && c <= '9'){
            index = Integer.parseInt(String.valueOf(c)); 
        }
        if (pack.empty() != true){
            if (index-1 >= 0 && index - 1 < pack.size()){
                if (pack.get(index - 1) instanceof Sword){
                    if (wieldingIndex ==  -1){
                        Displayable sword = (Displayable)pack.get(index - 1);
                        sword.setWield(true);
                        wieldingIndex = index - 1;
                    }
                    else{
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:Already wielding sword, drop using 'd'<integer>",0, topHeight+gameHeight+1);
                        terminal.repaint();
                    }
                }
                else{
                     Item item = pack.get(index - 1);
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:"+item.getName()+" cannot be wielded,execute T<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();

                }
            }
            else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Invalid index,execute T<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();
            }
        }
        else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Pack is empty",0, topHeight+gameHeight+1);
                     terminal.repaint();
        }
    }
    public void drop(char c){
       int index = 0;
       int posX = getPlayerX();
       int posY = getPlayerY();
       if (c >= '1' && c <= '9'){
            index = Integer.parseInt(String.valueOf(c)); 
        }
        if (pack.empty() != true){
             if ( index - 1 >= 0 && index - 1 < pack.size()){
                    Item itemDrop = (Item)pack.remove(index - 1);
                    if (itemDrop.getWorn() == true){
                        itemDrop.setWorn(false);
                        wearingIndex = -1;
                    }
                    else if(itemDrop.getWield() ==  true){
                        itemDrop.setWield(false);
                        wieldingIndex = -1;
                    }
                    objectGridDisplayable[posX][posY].insertElementAt(itemDrop,objectGridDisplayable[posX][posY].size()-1);
                    objectGrid[posX][posY].insertElementAt(itemDrop.getType(),objectGrid[posX][posY].size()-1);
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Dropped an item",0, topHeight+gameHeight+1);
                     terminal.repaint();
             }
             else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Invalid index,execute d<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();
             }
        }
        else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Pack is empty",0, topHeight+gameHeight+1);
                     terminal.repaint();
        }
    }
    public void wear(char c){
       int index = 0;
       if (c >= '1' && c <= '9'){
            index = Integer.parseInt(String.valueOf(c)); 
        }
       if (pack.empty() != true){
            if (index-1 >= 0 && index - 1 < pack.size()){
                if (pack.get(index - 1) instanceof Armor){
                    if (wearingIndex ==  -1){
                        Displayable armor = (Displayable)pack.get(index - 1);
                        armor.setWorn(true);
                        wearingIndex = index -1;
                    }
                    else{
                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                        terminal.write("Info:Already wearing armor, take off using 'c'",0, topHeight+gameHeight+1);
                        terminal.repaint();
                    }
                }
                else{
                     Item item = pack.get(index - 1);
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:"+item.getName()+" cannot be worn,execute w<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();

                }
            }
            else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Invalid index,execute w<integer> again",0, topHeight+gameHeight+1);
                     terminal.repaint();
            }
        }
        else{
                     terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                     terminal.write("Info:Pack is empty",0, topHeight+gameHeight+1);
                     terminal.repaint();
        }
    }
    public boolean addObjectToDisplay(int x, int y, char type, Displayable obj){
            boolean added = false;
            int damagebyP = 0;
            int damagebyM = 0;
            int hpP = 0;
            int hpM = 0;
            if ( (objectGrid[x][y] != null && objectGridDisplayable[x][y] != null) && (objectGridDisplayable[x][y].peek() instanceof Room == false)){
                if (objectGridDisplayable[x][y].peek() instanceof Monster){
                    hpP = obj.getHp();
                    Monster monster = (Monster)objectGridDisplayable[x][y].peek();
                    hpM = monster.getHp();
                    if (hpP >=0 && hpM >= 0){
                    damagebyP = obj.getMaxHits();
                    Random randP = new Random();
                    damagebyP = randP.nextInt(damagebyP+1); 
                    damagebyM = monster.getMaxHits();
                    Random randM = new Random();
                    damagebyM = randM.nextInt(damagebyM+1);
                    hpM = hpM-damagebyP;
                    monster.setHp(hpM);
                    hpP = hpP-damagebyM;
                    Player creatureP =(Player) obj;
                    Monster creatureM = (Monster)objectGridDisplayable[x][y].peek();
                    ArrayList<CreatureAction> creatureactionsP = creatureP.getCreatureAction();
                    ArrayList<CreatureAction> creatureactionsM = creatureM.getCreatureAction();
                    obj.setHp(hpP);
                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                    terminal.repaint();
                    String strDmgP = new String("Info:Damage[Player:"+damagebyP+"]["+monster.getName()+":"+damagebyM+"]");
                    terminal.write(strDmgP,0, topHeight+gameHeight+1);
                    terminal.repaint();
                    String strHpP = new String("HP:"+obj.getHp()+" "+"Score:0"+" ");
                    terminal.write(strHpP,0,0);
                    terminal.repaint();
                    numMoves++;
                    added = false;
                    if (creatureactionsP != null){
                    for (CreatureAction actionP :creatureactionsP){
                            switch(actionP.getName()){
                                case "DropPack":
                                if (pack.empty() != true){
                                    char c = Character.forDigit(pack.size(), 10);
                                    drop(c);
                                    terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                                    terminal.repaint();
                                    terminal.write("ActionMessage:"+actionP.getMessage(),0, topHeight+gameHeight);
                                    terminal.repaint();
                                }
                                else{
                                    terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                                    terminal.repaint();
                                    terminal.write("Info:Cannot execute DropPack, pack is empty",0, topHeight+gameHeight+1);
                                    terminal.repaint();
                                }
                                break;
                                case "YouWin":
                                    if(hpP <= 0){
                                        terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("ActionMessage:"+actionP.getMessage(),0, topHeight+gameHeight);
                                        terminal.repaint();
                                    }
                                    break;
                                case "EndGame":
                                    if (hpP <= 0){
                                        terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("ActionMessage:"+actionP.getMessage(),0, topHeight+gameHeight);
                                        terminal.repaint();
                                        playerDead = true;
                                    }
                                    break;
                                case "ChangeDisplayedType":
                                    if(hpP <= 0){
                                        int posX = getPlayerX();
                                        int posY = getPlayerY();
                                        char c = actionP.getChar();
                                        objectGrid[posX][posY].pop();
                                        objectGridDisplayable[posX][posY].pop();
                                        objectGrid[posX][posY].push(c);
                                        obj.setType(c);
                                        objectGridDisplayable[posX][posY].push(obj);
                                        writeToTerminal(posX,posY);
                                    }
                                    break;
                                case "UpdateDisplay":
                                    terminal.write("HP:"+obj.getHp()+" "+"Score:0"+" ",0,0);
                                    terminal.repaint();
                                    break;
                                case "EmptyPack":
                                    if (pack.empty() == true){
                                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("Info:Cannot execute EmptyPack, pack is empty",0, topHeight+gameHeight+1);
                                        terminal.repaint();
                                    }
                                    else{
                                        while(pack.empty() != true){
                                            notifyInputObservers('d');
                                        }
                                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("Info:"+actionP.getMessage(),0, topHeight+gameHeight+1);
                                        terminal.repaint();

                                    }
                                    break;
                                case "Remove":
                                    if (hpP <= 0){
                                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("Info:Player"+" has been killed and removed",0, topHeight+gameHeight+1);
                                        terminal.repaint();
                                        objectGridDisplayable[playerX][playerY].pop();
                                        objectGrid[playerX][playerY].pop();
                                        writeToTerminal(playerX,playerY);    
                                    }
                                    break;
                                default:
                                    System.out.println("CreatureAction not found for player");
                                    break;
                                }
                            }
                        }
                        if (creatureactionsM != null){
                        for (CreatureAction actionM :creatureactionsM){
                            switch(actionM.getName()){
                                case "Remove":
                                    if (hpM <= 0){
                                        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("Info:Monster"+" has been killed and removed",0, topHeight+gameHeight+1);
                                        terminal.repaint();
                                        objectGridDisplayable[x][y].pop();
                                        objectGrid[x][y].pop();
                                        objectGridDisplayable[playerX][playerY].pop();
                                        objectGrid[playerX][playerY].pop();
                                        writeToTerminal(playerX,playerY);
                                        playerX = x;
                                        playerY = y;
                                        objectGridDisplayable[x][y].push(obj);
                                        objectGrid[x][y].push(type);
                                        writeToTerminal(x,y);    
                                    }
                                    break;
                                case "YouWin":
                                    if(hpM <= 0){
                                        terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                                        terminal.repaint();
                                        terminal.write("ActionMessage:"+actionM.getMessage(),0, topHeight+gameHeight);
                                        terminal.repaint();
                                        terminal.write("HP:"+obj.getHp()+" "+"Score:0"+" ",0,0);
                                        terminal.repaint();
                                    
                                    }
                                    break;
                                case "Teleport":
                                    if (hpM > 0){
                                        Random rand = new Random();
                                        int randInd = rand.nextInt(floors.size());
                                        Floor randFloor = floors.get(randInd);
                                        int posXrandom = randFloor.getFloorX();
                                        int posYrandom = randFloor.getFloorY();
                                        if (objectGrid[posXrandom][posYrandom] != null && objectGridDisplayable[posXrandom][posYrandom] != null){
                                            if (objectGrid[posXrandom][posYrandom].peek() == '.' || objectGrid[posXrandom][posYrandom].peek() == '#'){
                                                Displayable creature = objectGridDisplayable[x][y].peek();
                                                objectGridDisplayable[posXrandom][posYrandom].push(creature);
                                                objectGrid[posXrandom][posYrandom].push(creature.getType());
                                                objectGrid[x][y].pop();
                                                objectGridDisplayable[x][y].pop();
                                                writeToTerminal(x,y);
                                                writeToTerminal(posXrandom, posYrandom);
                                                terminal.clear('\0', 0, topHeight+gameHeight, gameWidth, 1);
                                                terminal.repaint();
                                                terminal.write("ActionMessage:"+actionM.getMessage(),0, topHeight+gameHeight);
                                                terminal.repaint(); 
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println("CreatureAction not found for creature");
                                    break;
                            
                            }
                        }
                    }    
                }   
                    if (hpP <= 0){
                         terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                         terminal.repaint();
                         terminal.write("Info:Your Player is dead, game over",0, topHeight+gameHeight+1);
                         terminal.repaint();
                         playerDead = true;
                    }
                    else if (hpM <= 0){
                         terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
                         terminal.repaint();
                         terminal.write("Info:"+monster.getName()+" has been killed",0, topHeight+gameHeight+1);
                         terminal.repaint();
                         added = false;
                    }
                    
         }
                
         else{
             if (type == '@'){
                playerX = x;
                playerY = y;
                String strHpP = new String("HP:"+obj.getHp()+" "+"Score:0"+" ");
                numMoves++;
                terminal.write(strHpP,0,0);
                terminal.repaint();
                }
                if (objectGridDisplayable[x][y].peek() instanceof Player){
                    int size = objectGridDisplayable[x][y].size();
                    objectGridDisplayable[x][y].add(size - 1,obj);
                    objectGrid[x][y].add(size - 1,type);
                    added = true;
                }
                else{
                    objectGridDisplayable[x][y].push(obj);
                    objectGrid[x][y].push(type);
                    writeToTerminal(x,y);
                    added = true;
                }
            }
    }
    else{
        terminal.clear('\0', 0, topHeight+gameHeight+1, gameWidth, 1);
        terminal.write("Info:Cannot move onto wall or empty space",0, topHeight+gameHeight+1);
        terminal.repaint();
        added = false;
        }
    return added;
}
    public void addObjectToDisplay(int posX, int posY, int width, int height, Room room){
        
        int posYEnd = 0;
        if (posY + height >= gameHeight + topHeight){
            posYEnd = gameHeight + topHeight; 
        }
        else{
            posYEnd = posY + height;
        }
        for (int x = posX ; x < posX + width ; x++){
             for (int y = posY ; y < posYEnd ; y++ ){
                if (x == posX || x == posX + width -1){
                    objectGridDisplayable[x][y] = new Stack <Displayable>();
                    objectGridDisplayable[x][y].push(room);
                    objectGrid[x][y] = new Stack<Character>();
                    objectGrid[x][y].push('X');
                }
                else if (y == posY || y == posYEnd - 1){
                    objectGridDisplayable[x][y] = new Stack <Displayable>();
                    objectGridDisplayable[x][y].push(room);
                    objectGrid[x][y] = new Stack<Character>();
                    objectGrid[x][y].push('X');
                }
                else{
                    objectGridDisplayable[x][y] = new Stack <Displayable>();
                    Floor floor = new Floor(x,y);
                    objectGridDisplayable[x][y].push(floor);
                    objectGrid[x][y] = new Stack<Character>();
                    objectGrid[x][y].push('.');
                    floors.add(floor);
                }
                
                writeToTerminal(x,y);
            }
        }
    }
    public void addObjectToDisplay(Stack xPosStack, Stack yPosStack, Room room, Passage passage){
        int endX = 0;
        int endY = 0;
        int currentX = 0;
        int currentY = 0;
        ListIterator<Integer> valueX = xPosStack.listIterator();
        ListIterator<Integer> valueY = yPosStack.listIterator();
        currentX = (int) valueX.next();
        currentY = (int) valueY.next();
        currentX += room.getposX();
        currentY += room.getposY() + topHeight;
  
        while (valueX.hasNext() && valueY.hasNext()){
                endX = (int)valueX.next();
                endY = (int)valueY.next();
                endX += room.getposX();
                endY += room.getposY()+ topHeight;
                if (currentX == endX){
                    for (int i = currentY ; i <= endY ; i++){
                        if(objectGrid[currentX][i] != null && objectGrid[currentX][i].peek() == 'X'){
                            Floor floor = new Floor(currentX,i);
                            objectGrid[currentX][i].push('+');
                            objectGridDisplayable[currentX][i].push(floor);
                            writeToTerminal(currentX,i);
                        }
                        else{
                            objectGrid[currentX][i] = new Stack<Character>();
                            Floor floor = new Floor(currentX,i);
                            objectGrid[currentX][i].push('#');
                            objectGridDisplayable[currentX][i] = new Stack<Displayable>();
                            objectGridDisplayable[currentX][i].push(floor);
                            floors.add(floor);
                            writeToTerminal(currentX, i);
                        }
                    }
                    for(int i = currentY ; i >= endY ; i--){
                        if(objectGrid[currentX][i] != null && objectGrid[currentX][i].peek() == 'X'){
                            Floor floor = new Floor(currentX,i);
                            objectGrid[currentX][i].push('+');
                            objectGridDisplayable[currentX][i].push(floor);
                            writeToTerminal(currentX,i);
                        }
                        else{
                            objectGrid[currentX][i] = new Stack<Character>();
                            Floor floor = new Floor(currentX,i);
                            objectGrid[currentX][i].push('#');
                            objectGridDisplayable[currentX][i] = new Stack<Displayable>();
                            objectGridDisplayable[currentX][i].push(floor);
                            floors.add(floor);
                            writeToTerminal(currentX, i);
                        }
                    }
                }
                else{
                    for (int j = currentX ; j <= endX ; j++){
                        if(objectGrid[j][currentY] != null && objectGrid[j][currentY].peek() == 'X'){
                            Floor floor = new Floor(j,currentY);
                            objectGrid[j][currentY].push('+');
                            objectGridDisplayable[j][currentY].push(floor);
                            writeToTerminal(j, currentY);
                        }
                        else{
                            objectGrid[j][currentY] = new Stack<Character>();
                            Floor floor = new Floor(j,currentY);
                            objectGrid[j][currentY].push('#');
                            objectGridDisplayable[j][currentY] = new Stack<Displayable>();
                            objectGridDisplayable[j][currentY].push(floor);
                            floors.add(floor);
                            writeToTerminal(j, currentY);
                        }
                    }
                    for (int j = currentX ; j >= endX ; j--){
                        if(objectGrid[j][currentY] != null && objectGrid[j][currentY].peek() == 'X'){
                            Floor floor = new Floor(j,currentY);
                            objectGrid[j][currentY].push('+');
                            objectGridDisplayable[j][currentY].push(floor);
                            writeToTerminal(j, currentY);
                        }
                        else{
                            objectGrid[j][currentY] = new Stack<Character>();
                            Floor floor = new Floor(j,currentY);
                            objectGrid[j][currentY].push('#');
                            objectGridDisplayable[j][currentY] = new Stack<Displayable>();
                            objectGridDisplayable[j][currentY].push(floor);
                            floors.add(floor);
                            writeToTerminal(j, currentY);
                        }
                    }
                }
            currentX = endX;
            currentY = endY;
        } 
}    
private void writeToTerminal(int x, int y) {
        char ch = objectGrid[x][y].peek();
        terminal.write(ch, x, y);
        terminal.repaint();
    }    
public void setTopMessageHeight(int _topHeight){
        topHeight = _topHeight;
        
    }
    public int getPlayerX(){
        return playerX;
    }
    public int getPlayerY(){
        return playerY;
    }
    public Creature getPlayer(){
        return player;
    }
    public  Stack<Character>[][] getObjectGrid(){
        return objectGrid;
    }
    public boolean getPlayerDead(){
        return playerDead;
    }
    public String getObjectDisplayGrid(int gameHeight, int width, int topHeight){
        String str = "";
        str += Integer.toString(gameHeight);
        str += Integer.toString(width);
        str += Integer.toString(topHeight);
        return str;
    }
}