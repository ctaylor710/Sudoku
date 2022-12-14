/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.*;

public class PlayerSpace extends Space{

   private Number playerNum;
   private Scribble[] scribbles = new Scribble[9]; //each PlayerSpace has a scribble array
   
   public PlayerSpace(Number n){ 
      super(n);
      playerNum = n;
      for(int i = 0; i < 9; i++){
         Scribble s = new Scribble(0);
         scribbles[i] = s;
      }
   }
   
   public void change(Number n){
      playerNum = n;
   }
   public int getScribbleNum(int i){ return scribbles[i].getVal(); }
   public void setScribbleNum(int i, int v){ scribbles[i].setVal(v); }
   public Scribble getScribble(int i){ return scribbles[i]; }
   public void draw(Graphics g, int x, int y, Color c){
      g.setColor(c);
      g.fillRect(x, y, spaceHeight, spaceHeight);
      if(x/spaceHeight < 9 && y/spaceHeight < 9){
         if(playerNum.getVal() != 0){ //This hides any scribble numbers that aren't set to a number 1-9
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesNewRoman", Font.PLAIN, 50));
            g.drawString(""+playerNum.getVal(), x+spaceHeight/3+3, y+spaceHeight*2/3-6);
         }
      }
   }
}