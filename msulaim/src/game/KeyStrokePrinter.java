package game;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue = null;
    private ObjectDisplayGrid displayGrid;

    public KeyStrokePrinter(ObjectDisplayGrid grid) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
    }
    @Override
    public boolean observerUpdate(char ch) {
        if (DEBUG > 0) {
           // System.out.println(CLASSID + ".observerUpdate receiving character " + ch);
        }
        inputQueue.add(ch);        
        return false;
    }
    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private boolean processInput() {

        char ch;

        boolean processing = true;
        while (processing) {
            if (inputQueue.peek() == null) {
                processing = false;
            } else {
                ch = inputQueue.poll();
                if (DEBUG > 1) {
                    System.out.println(CLASSID + ".processInput peek is " + ch);
                }
                else if(ch == 'd'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.drop(c);
                    System.out.println("character " + c + " entered on the keyboard");
                }
                else if(ch == 'w'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.wear(c);
                    System.out.println("character " + c + " entered on the keyboard");
                }
                else if(ch == 'T'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.wield(c);
                    System.out.println("character " + c + " entered on the keyboard");
                }
                else if(ch == 'r'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.read(c);
                    System.out.println("character " + c + " entered on the keyboard");
                }
                else if (ch == 'H'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.help(c);
                    System.out.println("character " + c + " entered on the keyboard");
                }
                else if (ch == 'E'){
                    System.out.println("character " + ch + " entered on the keyboard");
                    displayGrid.endGame();
                    while(inputQueue.peek() == null){
                    }
                    char c = inputQueue.poll();
                    displayGrid.endGame(c);
                    System.out.println("character " + c + " entered on the keyboard");
                    if (c == 'Y' || c == 'y'){
                        System.out.println("You ended the game");
                        System.out.println("Ending input checking");
                        return false;
                    }
                }
                else if (displayGrid.getPlayerDead()){
                    System.out.println("Your player is dead, ending input checking");
                    return false;
                }
                else {
                    System.out.println("character " + ch + " entered on the keyboard");
                    displayGrid.updateDisplay(ch);
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        displayGrid.registerInputObserver(this);
        boolean working = true;
        while (working) {
            rest();
            working = (processInput( ));
        }
    }
}