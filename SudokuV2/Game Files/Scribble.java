/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;

public class Scribble{ //they are basically just small numbers that can be useful when solving

   private int val;
   
   public Scribble(int v){
      val = v;
   }
   
   public void setVal(int v){ val = v; }
   public int getVal(){ return val; }
   public void draw(Graphics g, int x, int y, int xIntPos, int yIntPos, int val){
      g.setColor(Color.BLACK);
      g.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
      if(val != 0)
         g.drawString(""+val, x+xIntPos, y+yIntPos);
   }
}