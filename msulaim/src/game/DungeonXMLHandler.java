package game;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
public class DungeonXMLHandler extends DefaultHandler {

    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "DungeonXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Dungeon objects to this array so that it has a list of 
    // all specified Dungeons.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Students (ArrayList<Student>) and not needed
    // to keep tract of the length and maxStudents.  You should use
    // an ArrayList in your project.
    private Dungeon dungeon = null;
    private Stack<Displayable> displayableBeingParsed;
    private Stack<Room> rooms = null;
// The XML file contains a Dungeon with a list of Rooms , and within each 
     // Room is a list of coordinates, Monsters,Players their information etc.      
    private Dungeon dungeonBeingParsed = null;
    private Action actionBeingParsed = null;
// The bX fields here indicate that at corresponding field is
    // having a value defined in the XML file.  In particular, a
    // line in the xml file might be:
    // <posX>1</posX> 
    // The startElement method (below) is called when <posX>
    // is seen, and there we would set bX.  The endElement
    // method (below) is called when </posX> is found, and
    // in that code we check if bX is set.  If it is,
    // we can extract a string representing the posX name 
    // from the data variable above.
    private boolean bposX = false;
    private boolean bposY = false;
    private boolean bWidth = false;
    private boolean bHeight = false;
    private boolean bHp = false;
    private boolean bVisible = false;
    private boolean bItemIntValue = false;
    private boolean bMaxhit = false;
    private boolean bType = false;
    private boolean bHpMoves = false;
    private boolean bActionMessage = false;
    private boolean bActionIntValue = false;
    private boolean bActionCharValue = false;
    

    // Used by code outside the class to get the list of Student objects
    // that have been constructed.
    public Dungeon getDungeons() {
        return dungeon;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its 
    // functionality by deriving from it!
    public DungeonXMLHandler() {
    }

    // startElement is called when a <some element> is called as part of 
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Dungeon")) {
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            dungeon = new Dungeon(name, width, topHeight, gameHeight, bottomHeight);
            displayableBeingParsed = new Stack<Displayable>();
            dungeonBeingParsed = dungeon;
        
        } 
        else if (qName.equalsIgnoreCase("Room")) {
            String name = attributes.getValue("room");
            Room room = new Room(name);
            room.setId(Integer.parseInt(name));
            rooms = new Stack<Room>();
            rooms.push(room);
            if (displayableBeingParsed != null){
                displayableBeingParsed.push(room);
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(room);
            } 
           if (dungeonBeingParsed != null){dungeonBeingParsed.addRoom(room);}
        
        }
        else if (qName.equalsIgnoreCase("Monster")){
            String name = attributes.getValue("name");
            int roomID = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Monster monster = new Monster();
            monster.setName(name);
            monster.setID(roomID, serial);
            monster.setRoom(rooms.peek());
            if (displayableBeingParsed != null){
                displayableBeingParsed.push(monster);
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(monster);
            }
            if (dungeonBeingParsed != null){dungeonBeingParsed.addCreature(monster);}
        }
        else if (qName.equalsIgnoreCase("Armor")){
            String name = attributes.getValue("name");
            int roomID = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Armor armor = new Armor(name);
            armor.setName(name);
            armor.setID(roomID, serial);
            armor.setRoom(rooms.peek());
            armor.setType(']');
            if (displayableBeingParsed != null){
                if (displayableBeingParsed.peek() instanceof Player){
                    Displayable player = displayableBeingParsed.peek();
                    player.addtoPack(armor);
                    displayableBeingParsed.push(armor);
                }
                else{
                    displayableBeingParsed.push(armor);
                    if (dungeonBeingParsed != null){dungeonBeingParsed.addItem(armor);}
                }
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(armor);
            }
        }
        else if (qName.equalsIgnoreCase("Player")){
            String name = attributes.getValue("name");
            int roomID = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Player player = new Player(); 
            player.setName(name);
            player.setID(roomID, serial);
            player.setRoom(rooms.peek());
            player.setType('@');
            if (displayableBeingParsed != null){
                displayableBeingParsed.push(player);
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(player);
            }
            if (dungeonBeingParsed != null){dungeonBeingParsed.addCreature(player);}  
        }
        else if (qName.equalsIgnoreCase("CreatureAction")){
            String name = attributes.getValue("name");
            switch(name){
                case "EndGame":
                    EndGame endgame = new EndGame(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(endgame);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature = (Monster)displayableBeingParsed.peek();
                        creature.setCreatureAction(endgame);
                    }
                    actionBeingParsed = endgame;
                break;
                case "Remove":
                    Remove remove = new Remove(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(remove);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature = (Monster)displayableBeingParsed.peek();
                        creature.setCreatureAction(remove);
                    }
                    actionBeingParsed = remove;
                break;
                case "YouWin":
                    YouWin youwin = new YouWin(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(youwin);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature = (Monster)displayableBeingParsed.peek();
                        creature.setCreatureAction(youwin);
                    }
                    actionBeingParsed = youwin;
                break;
                case "ChangeDisplayedType":
                   ChangedDisplayType changeddisplaytype = new ChangedDisplayType(name, (Creature)displayableBeingParsed.peek());
                   if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(changeddisplaytype);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature = (Monster)displayableBeingParsed.peek();
                        creature.setCreatureAction(changeddisplaytype);
                    } 
                   actionBeingParsed = changeddisplaytype;
                break;
                case "UpdateDisplay":
                    UpdateDisplay updatedisplay = new UpdateDisplay(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(updatedisplay);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature =(Monster) displayableBeingParsed.peek();
                        creature.setCreatureAction(updatedisplay);
                    }
                    actionBeingParsed = updatedisplay;
                break;
                case "DropPack":
                    DropPack droppack = new DropPack(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(droppack);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature =(Monster) displayableBeingParsed.peek();
                        creature.setCreatureAction(droppack);
                    }
                    actionBeingParsed = droppack;
                break;
                case "Teleport":
                    Teleport teleport = new Teleport(name, (Creature)displayableBeingParsed.peek());
                    if (displayableBeingParsed.peek() instanceof Player){
                        Player creature = (Player)displayableBeingParsed.peek();
                        creature.setCreatureAction(teleport);
                    }
                    else if(displayableBeingParsed.peek() instanceof Monster){
                        Monster creature = (Monster)displayableBeingParsed.peek();
                        creature.setCreatureAction(teleport);
                    }
                    actionBeingParsed = teleport;
                break;
                default:
                    System.out.println("Unknown activity: ");
                    break;
            
            }           
        }
        else if (qName.equalsIgnoreCase("Scroll")){
            String name = attributes.getValue("name");
            int roomID = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Scroll scroll = new Scroll(name);
            scroll.setID(roomID, serial);
            scroll.setRoom(rooms.peek());
            scroll.setType('?');
            scroll.setName(name);
            displayableBeingParsed.push(scroll);
            dungeonBeingParsed.addItem(scroll);
        
        }
        else if (qName.equalsIgnoreCase("ItemAction")){
             String name = attributes.getValue("name");
             Scroll scroll = (Scroll)displayableBeingParsed.peek();
             switch(name){
                 case "BlessArmor":
                     BlessArmor blessarmor = new BlessArmor((Item)displayableBeingParsed.peek(), name);
                     scroll.setItemAction(blessarmor);
                     actionBeingParsed = blessarmor;      
                 break;
                 case "Hallucinate":
                     Hallucinate hallucinate = new Hallucinate((Item)displayableBeingParsed.peek(), name);
                     scroll.setItemAction(hallucinate);
                     actionBeingParsed = hallucinate; 
                 break;
             }
        }
        else if (qName.equalsIgnoreCase("Sword")){
            String name = attributes.getValue("name");
            int roomID = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Sword sword = new Sword(name);
            sword.setID(roomID, serial);
            sword.setRoom(rooms.peek());
            sword.setType(')');
            sword.setName(name);
            if (displayableBeingParsed != null){
                if (displayableBeingParsed.peek() instanceof Player){
                    Displayable player = displayableBeingParsed.peek();
                    player.addtoPack(sword);
                    displayableBeingParsed.push(sword);
                }
                else{
                    displayableBeingParsed.push(sword);
                    if (dungeonBeingParsed != null){dungeonBeingParsed.addItem(sword);}
                }
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(sword);
            }
            
        
        }
        else if (qName.equalsIgnoreCase("Passage")){
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            Passage passage = new Passage();
            passage.setID(room1, room2);
            if (displayableBeingParsed != null){
                displayableBeingParsed.push(passage);
            }
            else{
                displayableBeingParsed = new Stack<Displayable>();
		displayableBeingParsed.push(passage);
            }
            if (dungeonBeingParsed != null){dungeonBeingParsed.addPassage(passage);}
        }
        
        else if (qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } 
        else if (qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } 
        else if (qName.equalsIgnoreCase("width")) {
            bWidth = true;
        } 
        else if (qName.equalsIgnoreCase("height")) {
            bHeight = true;
        }  
        else if (qName.equalsIgnoreCase("type")){
            bType = true;
        }
        else if (qName.equalsIgnoreCase("hp")){
            bHp = true;
        }
        else if (qName.equalsIgnoreCase("visible")){
            bVisible = true;
        }
        else if (qName.equalsIgnoreCase("ItemIntValue")){
            bItemIntValue = true;
        }
        else if (qName.equalsIgnoreCase("maxhit")){
            bMaxhit = true;
        }
        else if (qName.equalsIgnoreCase("hpMoves")){
            bHpMoves = true;
        }
        else if (qName.equalsIgnoreCase("actionMessage")){
            bActionMessage = true;
        }
        else if (qName.equalsIgnoreCase("actionIntValue")){
            bActionIntValue = true;
        }
        else if (qName.equalsIgnoreCase("actionCharValue")){
            bActionCharValue = true;
        }
        else if (qName.equalsIgnoreCase("Rooms")){
        }
	else if (qName.equalsIgnoreCase("Passages")){
		
	}
	else {
            System.out.println("Unknown qname: " + qName);
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Displayable displayable;
        Action action;
        if (bposX) {
                displayable = displayableBeingParsed.peek();
                if (displayable instanceof Passage){
                    displayable.SetStackPosX(Integer.parseInt(data.toString()));
                }
                else{
                    displayable.SetPosX(Integer.parseInt(data.toString()));
                }
                bposX = false;
        } 
        else if (bposY){
                displayable = displayableBeingParsed.peek();
                if (displayable instanceof Passage){
                    displayable.SetStackPosY(Integer.parseInt(data.toString()));
                }
                else{
                    displayable.SetPosY(Integer.parseInt(data.toString()));
                }
                bposY = false;
        }
        else if (bWidth){
                displayable = displayableBeingParsed.peek();
                displayable.SetWidth(Integer.parseInt(data.toString()));
                bWidth = false;
        }
        else if (bHeight){
                displayable = displayableBeingParsed.peek();
                displayable.SetHeight(Integer.parseInt(data.toString()));
                bHeight = false;  
        }
        else if (bType){
                displayable = displayableBeingParsed.peek();
                displayable.setType(data.charAt(0));
                bType = false;
        }
        else if (bHp){
                displayable = displayableBeingParsed.peek();
                displayable.setHp(Integer.parseInt(data.toString()));
                bHp = false;  
        }
        else if (bVisible){
                displayable = displayableBeingParsed.peek();
                displayable.setVisible();
                bVisible = false;
        }
        else if (bItemIntValue){
                displayable = displayableBeingParsed.peek();
                displayable.setIntValue(Integer.parseInt(data.toString()));
                bItemIntValue = false;  
        }
        else if (bMaxhit){
                displayable = displayableBeingParsed.peek();
                displayable.setMaxHit(Integer.parseInt(data.toString()));
                bMaxhit = false;
        }
        else if (bHpMoves){
                displayable = displayableBeingParsed.peek();
                displayable.setHpMove(Integer.parseInt(data.toString()));
                bHpMoves = false;
        }
        else if (bActionMessage){
                action = actionBeingParsed;
                action.setMessage(data.toString());
                bActionMessage = false;
        }
        else if (bActionIntValue){
            action = actionBeingParsed;
            action.setIntValue(Integer.parseInt(data.toString()));
            bActionIntValue = false;
        }
        else if (bActionCharValue){
            action = actionBeingParsed;
            action.setCharValue(data.charAt(0));
            bActionCharValue = false;
        }
        if (qName.equalsIgnoreCase("Dungeon")){
            dungeonBeingParsed = null;
        }
        else if (qName.equalsIgnoreCase("Room")){
            displayableBeingParsed.pop();
            rooms.pop();
        }
        else if (qName.equalsIgnoreCase("Monster")){
             displayableBeingParsed.pop();
        }
        else if (qName.equalsIgnoreCase("Armor")){
             displayableBeingParsed.pop();
        }
        else if (qName.equalsIgnoreCase("Player")){
             displayableBeingParsed.pop();
        }
        else if (qName.equalsIgnoreCase("Scroll")){
             displayableBeingParsed.pop();
        }
         else if (qName.equalsIgnoreCase("CreatureAction")){
             actionBeingParsed = null;
         }
        else if (qName.equalsIgnoreCase("ItemAction")){
             actionBeingParsed = null;
         }
        else if (qName.equalsIgnoreCase("Sword")){
             displayableBeingParsed.pop();
        }
        else if (qName.equalsIgnoreCase("Passage")){
            displayableBeingParsed.pop();
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

}
