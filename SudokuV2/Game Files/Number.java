/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Number implements MouseMotionListener, Game{ //more helpful than using an int because you can keep track of their position

   private int val;
   private int xPos;
   private int yPos;
   
   public Number(int v){
      val = v;
      xPos = 0;
      yPos = 0;
   }
   public Number(int x, int y){
      val = 0;
      xPos = x;
      yPos = y;
   }
   public Number(int x, int y, int v){
      val = v;
      xPos = x;
      yPos = y;
   }
   
   public int getXPos(){ return xPos; }
   public int getYPos(){ return yPos; }
   public int getVal(){ return val; }
   public void setXPos(int x){ xPos = x; }
   public void setYPos(int y){ yPos = y; }
   public void setVal(int v){ val = v; }
   public void draw(Graphics g, int x, int y, int val){ ; }
   public void draw(Graphics g, int x, int y, int val, int xIntPos, int yIntPos, Color c){
      g.setColor(c);
      g.fillRect(xPos, yPos, spaceHeight, spaceHeight);
      xPos = x;
      yPos = y;
      g.setColor(Color.BLACK);
      g.drawRect(x, y, spaceHeight, spaceHeight);
      g.drawString(""+val, x+xIntPos, y+yIntPos);
   }
   public void mouseDragged(MouseEvent e){} //MouseMotionListener is outside code.
   public void mouseMoved(MouseEvent e){ //This checks your mouse's motion, sets its position to wherever your mouse is.
      int x = e.getX();
      int y = e.getY();
      xPos = x;
      yPos = y;
   }
}