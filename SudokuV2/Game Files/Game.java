/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.awt.Graphics;
import java.awt.*;

public interface Game{

   public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   public static final int screenHeight = screenSize.height;
   public static final int screenWidth = screenSize.width;
   public static final int boardHeight = screenHeight - 90;
   public static final int spaceHeight = (boardHeight-10)/9 - 1;

   public void draw(Graphics g, int x, int y, int val);
   
}