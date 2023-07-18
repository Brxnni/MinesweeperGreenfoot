import greenfoot.*;

import java.util.List;
import java.util.ArrayList;

// Literally Bosnia & Herzegovina
public class BombWorld extends World {
    
    // 10 = 10% chance that a field is a bomb
    // easy ~ 10%
    // hard ~ 25%
    public int bombChance = 19;
    
    public TextDisplayer display;
    
    public BombWorld()
    {
        // hardcoded >:-(
        super(28, 16, 32);
        populateMinefield();
        
        display = new TextDisplayer();
        addObject(display, getWidth() / 2, getHeight() / 2);
    }
    
    public void showPrettyText(String txt){
        display.changeText(txt);
    }

    public void act(){
        if (Greenfoot.mouseClicked(null)){
            MouseInfo mouse = Greenfoot.getMouseInfo();
            Actor actor = mouse.getActor();
            if (mouse.getButton() == 1 && actor != null && actor.getClass() == Field.class){
                ((Field) actor).leftClick();
            }
            
            if (mouse.getButton() == 3 && actor != null && actor.getClass() == Field.class){
                ((Field) actor).rightClick();
            }
        }
    }
    
    public List<Field> findZeroes(Field startField){
        List<Field> foundZeroes = new ArrayList<Field>();
        foundZeroes.add(startField);
        // pass list by reference so that all function calls can add to it globally
        recursivelyFindFields(startField, foundZeroes);
        return foundZeroes;
    }
    
    public void recursivelyFindFields(Field f, List<Field> connected){
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++){
                
                if (x == 0 && y == 0) continue;
                
                List<Field> neighbourObj = getObjectsAt(f.getX() + x, f.getY() + y, Field.class);
                if (neighbourObj.size() == 0) continue;
                
                Field neighbour = neighbourObj.get(0);
                
                if (neighbour.num == 0 && !connected.contains(neighbour)){
                    connected.add((Field) neighbour);
                    recursivelyFindFields((Field) neighbour, connected);
                }
                
            }
        }
    }
    
    public void gameOver(Field explodedBomb){
        List<Field> fields = getObjects(Field.class);
        for (Field f : fields){
            
            if (f == explodedBomb) continue;
            
            // Every bomb that was not flagged
            if (f.isBomb && !f.isFlagged){
                f.changeImage("bomb.png");
            // Field that was flagged but was not a bomb
            } else if (!f.isBomb && f.isFlagged){
                f.changeImage("false_flag.png");
            } else if (!f.isBomb){
                f.reveal();
            }
            
        }
        
        showPrettyText("You exploded. Skill issue!");
        Greenfoot.stop();        
    }
    
    public void checkHasWon(){
        boolean hasWon = true;
        
        List<Field> fields = getObjects(Field.class);
        for (Field f : fields){
            if (f.isBomb && !f.isFlagged){
                hasWon = false;
                break;
            }
            if (!f.isBomb && f.isCovered){
                hasWon = false;
                break;
            }
        }
        
        if (hasWon){
            showPrettyText("Congratulations! You found every Bomb.");
            Greenfoot.stop();
        }
    }
    
    public void populateMinefield(){
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                
                boolean isBomb = (Greenfoot.getRandomNumber(100) + 1) > (100 - bombChance);
                Field f = new Field(isBomb);
                addObject(f, i, j);
                
            }
        }
        
        List<Field> fields = getObjects(Field.class);
        for (Field f : fields){
            f.calculateNum();
        }
    }
}