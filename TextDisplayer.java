import greenfoot.*;

public class TextDisplayer extends Actor
{
    public TextDisplayer(){
        // If you assign no image at all the actor will show the greenfoot logo so this is a workaround to make it invisible
        GreenfootImage i = new GreenfootImage(1, 1);
        setImage(i);
    }
    
    public void changeText(String text) 
    {
        // color1 = text color, color2 = background color as rgba (a = alpha/transparency, 0=invisible, 255=opaque)
        GreenfootImage txtImg = new GreenfootImage(text, 52, Color.WHITE, new Color(0, 0, 0, 128));
        setImage(txtImg);
    }    
}
