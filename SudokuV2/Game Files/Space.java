/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

public class Space extends Board{

   private Number num;
   private boolean isPlayed = false;
   
   public Space(Number n){
      num = n;
   }
   
   public Number getNum(){ return num; }
   public void setNum(Number n){ num = n; }
   public int getVal(){ return num.getVal(); }
   public void setVal(int i){ num.setVal(i); }
   public boolean getPlayed(){ return isPlayed; }
   public void setPlayed(boolean iP){ isPlayed = iP; }
   public void draw(Graphics g, int x, int y, int val){
      Color lightGray = new Color(204, 204, 204);
      g.setColor(lightGray);
      g.fillRect(x, y, spaceHeight, spaceHeight); //originally 99,99
      g.setColor(Color.BLACK);
      g.setFont(new Font("TimesNewRoman", Font.PLAIN, 50));
      g.drawString(""+val, x+spaceHeight/3+3, y+spaceHeight*2/3-6); //originally x+36,y+60
   }
   public int inBorders(int xVal, int yVal){
      return (xVal/spaceHeight)+9*(yVal/spaceHeight);
   }
}