package game;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
import java.lang.*;
import org.xml.sax.SAXException;
public class Rogue implements Runnable{

    private static ObjectDisplayGrid displayGrid = null;
    private static Dungeon dungeon;
    private static int gameWidth;
    private static int gameHeight;
    private static int topHeight;
    private static int botHeight;
    private Thread keyStrokePrinter;
    public Rogue(Dungeon dungeon){
        gameWidth = dungeon.getWidth();
        gameHeight = dungeon.getHeight();
        topHeight = dungeon.getTopHeight();
        botHeight = dungeon.getBotHeight();
        displayGrid = new ObjectDisplayGrid(gameWidth,gameHeight,topHeight,botHeight, dungeon);
    }
    @Override
    public void run() {
            ArrayList<Room> rooms = dungeon.getRooms();
            int posX = 0;
            int posY = 0;
            int width = 0;
            int height = 0;
            int id = 0;
            char type = 'X';
            Stack<Integer> posXStack;
            Stack<Integer> posYStack;
            if (rooms.size() != 0){
            for (Room room : rooms){
                posX = room.getposX();
                posY = room.getposY() + topHeight;
                width = room.getWidth();
                height = room.getHeight();
                displayGrid.addObjectToDisplay(posX, posY, width, height, room);
                }
            }
            ArrayList<Creature> creatures = dungeon.getCreatures();
            if (creatures.size() != 0){
            for (Creature creature : creatures){
                Room room = creature.getRoom();
                posX = creature.getposX();
                posY = creature.getposY() + topHeight;
                posX += room.getposX();
                posY += room.getposY();
                type = creature.getType();
                displayGrid.addObjectToDisplay(posX, posY, type, creature);
                }
            }
            ArrayList<Item> items = dungeon.getItems();
            if (items.size() != 0){
                for (Item item : items){
                    Room room = item.getRoom();
                    posX = item.getposX();
                    posY = item.getposY() + topHeight;
                    posX += room.getposX();
                    posY += room.getposY();
                    type = item.getType();
                    if (posX != 0 && posY != 0 + topHeight){
                        displayGrid.addObjectToDisplay(posX, posY, type, item);
                    }
                }
            }
            ArrayList<Passage> passages = dungeon.getPassages();
            if (passages.size() != 0){
                for (Passage passage : passages){
                    posXStack = passage.getStackX();
                    posYStack = passage.getStackY();
                    displayGrid.addObjectToDisplay(posXStack, posYStack,rooms.get(0), passage);
                }
            }
            try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        String fileName = null;
        switch (args.length) {
        case 1:
           fileName = "xmlFiles/"+args[0];
           break;
        default:
           System.out.println("java game.Rogue <xmlfilename>");
	   return;
        }
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DungeonXMLHandler handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);
            dungeon = handler.getDungeons();
            Rogue test = new Rogue(dungeon);
            Thread testThread = new Thread(test);
            testThread.start();
            test.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
            test.keyStrokePrinter.start();
            testThread.join();
            test.keyStrokePrinter.join();
            } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        
       }
    
    }
}
