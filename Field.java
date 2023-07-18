import greenfoot.*;

import java.util.List;
import java.util.ArrayList;

public class Field extends Actor
{
    public boolean isBomb;
    
    public int num = 0;
    public boolean isFlagged = false;
    public boolean isCovered = true;
    
    public Field(boolean b){
        isBomb = b;
        
        setImage(new GreenfootImage("covered.png"));
    }

    public List<Field> getNeighbours(){
        List<Field> neighbours = new ArrayList<Field>();
        
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++){
                if (x == 0 && y == 0) continue;
                
                List<Actor> found = getWorld().getObjectsAt(getX()+x, getY()+y, null);
                if (found.size() == 0) continue;
                Actor f = found.get(0);
                if (f != null){
                    neighbours.add((Field) f);
                }
            }
        }
        
        return neighbours;
    }
    
    public void calculateNum(){
        if (isBomb){ return; }
        
        num = 0;
        List<Field> neighbours = getNeighbours();
        for (Field f : neighbours){
            if (f.isBomb) num++;
        }
    }
    
    public void revealNeighbours(){
        List<Field> neighbours = getNeighbours();
        for (Field f : neighbours){
            f.reveal();
        }
    }
        
    public void reveal(){
        setImage(new GreenfootImage(num + ".png"));
        isCovered = false;
    }
    
    public void leftClick(){
        if (isFlagged) return;
        if (isBomb){
            // Bomb that was clicked on gets red highlight
            setImage("exploded_bomb.png");
            ((BombWorld) getWorld()).gameOver(this);
        }
        if (!isBomb){
            reveal();
            
            if (num == 0){
                // Recursively look for every connected 0 (even diagonally not just orthogonally)
                // And have each of those reveal every neighbour (also diagonally+orthogonally)
                List<Field> zeroFields = ((BombWorld) getWorld()).findZeroes(this);
                for (Field f : zeroFields){
                    f.revealNeighbours();
                }
            }
            
            ((BombWorld) getWorld()).checkHasWon();
            
        } else {
            ((BombWorld) getWorld()).gameOver(this);
        }
    }
    
    public void rightClick(){
        if (!isCovered) return;

        isFlagged = !isFlagged;
        
        if (isFlagged){
            setImage(new GreenfootImage("flagged.png"));
            ((BombWorld) getWorld()).checkHasWon();
        } else {
            setImage(new GreenfootImage("covered.png"));
        }
    }
}