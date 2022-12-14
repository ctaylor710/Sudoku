/* Christopher Taylor
 * P5
 * 5/26/18
 * SudokuFinalProject
 */ 
import java.io.*; 
import java.awt.*; 
import java.awt.image.*; 
import javax.imageio.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.TreeSet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Test extends JPanel
{ 
   ///////////////////////////////////////////// Button Variables /////////////////////////////////////////////
   private BufferedImage beginUn = null;
   private BufferedImage interUn = null;
   private BufferedImage exUn = null;
   private BufferedImage begin = null;
   private BufferedImage inter = null;
   private BufferedImage ex = null;
   private BufferedImage toggleUn = null;
   private BufferedImage toggle = null;
   private BufferedImage resetUn = null;
   private BufferedImage reset = null;
   private BufferedImage checkUn = null;
   private BufferedImage check = null;
   private BufferedImage autoSolveUn = null;
   private BufferedImage autoSolve = null;
   private BufferedImage clearAnswersUn = null;
   private BufferedImage clearAnswers = null;
   private BufferedImage returnHomeUn = null;
   private BufferedImage returnHome = null;
   private boolean beginPressed = false;
   private boolean interPressed = false;
   private boolean exPressed = false;
   private boolean togglePressed = false;
   private boolean resetPressed = false;
   private boolean checkPressed = false;
   private boolean autoSolvePressed = false;
   private boolean clearAnswersPressed = false;
   private boolean returnHomePressed = false;
   ///////////////////////////////////////////// Button Variables /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Other Variables /////////////////////////////////////////////
   private boolean showAnswers = false;
   private boolean generated = false; //Returns if program is finished generating the board
   private int difficulty = 0;
   private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   private static int screenHeight = screenSize.height;
   private static int screenWidth = screenSize.width;
   private static int boardHeight = screenHeight - 90;
   private static int spaceHeight = (boardHeight-10)/9 - 1;
   private int linePositions[] = new int[10];
   private Color backgroundButtons = new Color(200, 200, 200);
   private Color backgroundScreen;
   private Color lightGray = new Color(204, 204, 204);
   private SpaceAlgorithm s = new SpaceAlgorithm(0);
   private Number[] numbers = new Number[9]; //These numbers are just for show
   private Number newNum; //This is what is actually used to fill in the board
   private boolean scribbleNums = false;
   ///////////////////////////////////////////// Other Variables /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Constructor /////////////////////////////////////////////
   public Test() {
      newNum = new Number(0); //instantiates the numbers
      for(int i = 1; i <= 9; i++){
         numbers[i-1] = new Number(i);
      }
      addMouseListener(new MouseAdapter() { //This is a ridiculously long parameter. Then again, this is an even more ridiculously long constructor. Also this is outside code
         public void mousePressed(MouseEvent e) { //If you clicked AND released. Outside code
            //These are all the conditionals for the title screen
            if(inSize(e.getX(), e.getY(), (screenWidth*3/4-480)/2, (screenWidth*3/4-480)/2 + 480, (screenHeight/2)-150, (screenHeight/2)-30) && difficulty == 0){ //if you chose Beginner
               beginPressed = true;
               new Timer().schedule(     //All Timer Class material is outside code
                  new TimerTask(){ //The timers are used for aesthetics so you can see the buttons being pressed    
                     public void run(){            
                        difficulty = 3; //you'd think it would go 1-3 but this way makes it more convenient for me
                        generated = false; //it hasn't been generated yet           
                        s.addSpaces(0, 0); //create a board.
                        s.setNumRemoved(45);
                        s.removeSpaces(difficulty);
                        
                        while(!s.isUnique(s.getSpaces(), 0, 0)){ //If it's unique, move on. Otherwise, keep creating boards until one is unique.
                           s = new SpaceAlgorithm(0);
                           s.addSpaces(0, 0);
                           s.setNumRemoved(45);
                           s.removeSpaces(difficulty);
                        }
                        new Timer().schedule( //In case the board generates really quickly, make it seem like it actually takes time
                           new TimerTask(){
                              public void run(){
                                 generated = true;
                              }
                           },
                           1000
                        );
                        beginPressed = false;
                     }
                  },
                  100
               );
            }
            if(inSize(e.getX(), e.getY(), (screenWidth*3/4-480)/2, (screenWidth*3/4-480)/2 + 480, (screenHeight/2), (screenHeight/2) + 120) && difficulty == 0){ //If you chose Intermediate
               interPressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        difficulty = 2;
                        generated = false;
                        s.addSpaces(0, 0);
                        s.setNumRemoved(52);
                        s.removeSpaces(difficulty);
                        while(!s.isUnique(s.getSpaces(), 0, 0)){
                           s = new SpaceAlgorithm(0);
                           s.addSpaces(0, 0);
                           s.setNumRemoved(52);
                           s.removeSpaces(difficulty);
                        }
                        new Timer().schedule(
                           new TimerTask(){
                              public void run(){
                                 generated = true;
                              }
                           },
                           1000
                        );
                        interPressed = false;
                     }
                  },
                  100
               );
            }
            if(inSize(e.getX(), e.getY(), (screenWidth*3/4-480)/2, (screenWidth*3/4-480)/2 + 480, (screenHeight/2)+150, (screenHeight/2)+270) && difficulty == 0){ //If you chose Expert
               exPressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        difficulty = 1;
                        generated = false;
                        s.addSpaces(0, 0);
                        s.setNumRemoved(54);
                        s.removeSpaces(difficulty); //This'll take a while
                        while(!s.isUnique(s.getSpaces(), 0, 0)){
                           s = new SpaceAlgorithm(0);
                           s.addSpaces(0, 0);
                           s.setNumRemoved(54);
                           s.removeSpaces(difficulty);
                       }
                        new Timer().schedule(
                           new TimerTask(){
                              public void run(){
                                 generated = true;
                              }
                           },
                           1000
                        );
                        exPressed = false;
                     }
                  },
                  100
               );
            }
            //These are all the conditionals for the game screen
            if(!win() && inSize(e.getX(), e.getY(), screenWidth*3/4-150, screenWidth*3/4-50, 50, 150) && difficulty != 0){ //If you pressed the toggle button
               togglePressed = true;
               if(!scribbleNums) //keeps track of whether the toggle is on or off. If it's on,
                  scribbleNums = true;  //then you are using the Scribble class. Otherwise, you are using the Number class.
               else if(scribbleNums)
                  scribbleNums = false;
               else
                  scribbleNums = false;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        togglePressed = false;
                     }
                  },
                  100
               );
            }
            if(!win() && inSize(e.getX(), e.getY(), screenWidth*3/4-150, screenWidth*3/4-50, screenHeight/4, screenHeight/4+100) && difficulty != 0){ //If you pressed the check answers button
               checkPressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        checkPressed = false;   //keep track of whether the answer checker is on or off. If it's on,
                        if(showAnswers)         //You can't alter the board in any way but you can see what you have right and wrong.
                           showAnswers = false;
                        else
                           showAnswers = true;
                     }
                  },
                 100
               );
            }
            if(!win() && inSize(e.getX(), e.getY(), screenWidth*3/4-150, screenWidth*3/4-50, screenHeight*3/4, screenHeight*3/4+100) && difficulty != 0){ //If you pressed the auto solve button
               autoSolvePressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        autoSolvePressed = false;
                        for(int i = 0; i < 9; i++){ 
                           for(int j = 0; j < 9; j++){
                              if(s.spaceAt(i, j).getVal() == 0 && s.playerSpaceAt(i, j).getVal() == 0){ //If the player space is not filled
                                 s.playerSpaceAt(i, j).change(s.answerAt(i, j).getNum()); //put in the correct number
                                 s.playerSpaceAt(i, j).setNum(s.answerAt(i, j).getNum());
                              }
                              else if(s.spaceAt(i, j).getVal() == 0 && s.playerSpaceAt(i, j).getVal() != 0){ //If the player space is filled in
                                 s.playerSpaceAt(i, j).change(new Number(0)); //first clear the number, just in case it's wrong
                                 s.playerSpaceAt(i, j).setNum(new Number(0));
                                 s.playerSpaceAt(i, j).change(s.answerAt(i, j).getNum()); //then put in the correct number
                                 s.playerSpaceAt(i, j).setNum(s.answerAt(i, j).getNum());
                              }
                              else ;//If it's not a player space, then do nothing, it's already good
                           }
                        }
                     }
                  },
                 100
               );
            }
            if(!win() && inSize(e.getX(), e.getY(), screenWidth*3/4-150, screenWidth*3/4-50, screenHeight/2, screenHeight/2+100) && difficulty != 0){ //If you pressed the clearAnswers button
               clearAnswersPressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        clearAnswersPressed = false;
                        for(int i = 0; i < 9; i++){ 
                           for(int j = 0; j < 9; j++){
                              if(s.spaceAt(i, j).getVal() == 0 && s.playerSpaceAt(i, j).getVal() != 0){ //If a player space is filled in
                                 s.playerSpaceAt(i, j).change(new Number(0)); //clear it
                                 s.playerSpaceAt(i, j).setNum(new Number(0));
                              }
                           }
                        }
                     }
                  },
                  100
               );
            }
            if(win() && inSize(e.getX(), e.getY(), (boardHeight-400)/2, (boardHeight-400)/2+400, (boardHeight-100)/2, (boardHeight-100)/2+100) && difficulty != 0){ //If you pressed the returnHome button
               returnHomePressed = true;
               new Timer().schedule(
                  new TimerTask(){
                     public void run(){
                        returnHomePressed = false;
                        difficulty = 0; //Congrats! The board resets and you are taken back to the title screen after marvelling in your victory.
                        s = new SpaceAlgorithm(0);
                     }
                  },
                  100
               );
            }
         }
         public void mouseClicked(MouseEvent e) { //If you clicked
            if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10, 10+spaceHeight) && difficulty != 0){ //If you clicked the number one
               newNum = new Number(e.getX(), e.getY(), 1); //Set newNum's position to your mouse and change its value
               addMouseMotionListener(newNum); //Make it follow your mouse
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+(spaceHeight+6), 10+2*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number two
               newNum = new Number(e.getX(), e.getY(), 2);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+2*(spaceHeight+6), 10+3*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number three
               newNum = new Number(e.getX(), e.getY(), 3);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+3*(spaceHeight+6), 10+4*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number four
               newNum = new Number(e.getX(), e.getY(), 4);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+4*(spaceHeight+6), 10+5*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number five
               newNum = new Number(e.getX(), e.getY(), 5);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+5*(spaceHeight+6), 10+6*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number six
               newNum = new Number(e.getX(), e.getY(), 6);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+6*(spaceHeight+6), 10+7*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number seven
               newNum = new Number(e.getX(), e.getY(), 7);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+7*(spaceHeight+6), 10+8*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number eight
               newNum = new Number(e.getX(), e.getY(), 8);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && inSize(e.getX(), e.getY(), boardHeight+40, boardHeight+40+spaceHeight, 10+8*(spaceHeight+6), 10+9*(spaceHeight+6) - 6) && difficulty != 0){ //If you clicked the number nine
               newNum = new Number(e.getX(), e.getY(), 9);
               addMouseMotionListener(newNum);
            }
            else if(!win() && !showAnswers && difficulty != 0){ //If you clicked anywhere else on the game screen
               if(newNum.getVal() != 0){ //If you're dragging a number
                  if(e.getX() < boardHeight && e.getY() < boardHeight){ //If you're within the game board
                     if(!scribbleNums){ //If you want to drag numbers
                        if(spaceOnBoard(e.getX(), e.getY()).getVal() == 0 && playerSpaceOnBoard(e.getX(), e.getY()).getVal() == 0){ //If the space is a PlayerSpace that is not filled
                           for(int i = 0; i < 9; i++){ //If there are any scribbles in the space, get rid of them
                              playerSpaceOnBoard(e.getX(), e.getY()).setScribbleNum(i, 0);
                           }
                           playerSpaceOnBoard(e.getX(), e.getY()).change(newNum); //sets the appropriate space equal to this number
                           playerSpaceOnBoard(e.getX(), e.getY()).setNum(newNum);
                           newNum = new Number(0); //un-draws the number you're dragging
                        }
                     }
                     else if(scribbleNums){ //If you want to drag scribbled numbers
                        if(spaceOnBoard(e.getX(), e.getY()).getVal() == 0 && playerSpaceOnBoard(e.getX(), e.getY()).getVal() == 0 && playerSpaceOnBoard(e.getX(), e.getY()).getScribbleNum(newNum.getVal()-1) != newNum.getVal()){ //If the space is a PlayerSpace that's unoccupied by an actual number and doesn't have the scribbled number in it you want
                           playerSpaceOnBoard(e.getX(), e.getY()).setScribbleNum(newNum.getVal()-1, newNum.getVal()); //Put the scribbled number into the space and draw it.
                           newNum = new Number(0);
                        }
                        else if(spaceOnBoard(e.getX(), e.getY()).getVal() == 0 && playerSpaceOnBoard(e.getX(), e.getY()).getVal() == 0 && playerSpaceOnBoard(e.getX(), e.getY()).getScribbleNum(newNum.getVal()-1) == newNum.getVal()){ //Else if the scribbled number is already in the space
                           playerSpaceOnBoard(e.getX(), e.getY()).setScribbleNum(newNum.getVal()-1, 0); //get rid of it
                           newNum = new Number(0);
                        }
                        else ; //else just do nothing
                     }      
                  }
                  if(!(e.getX() < boardHeight && e.getY() < boardHeight)) //If  you aren't within the game board
                     newNum = new Number(0); //get rid of any number you might be holding
               }
               else{ //If you don't have a number
                  if(e.getX() < boardHeight && e.getY() < boardHeight){ //If you clicked in the game board
                     newNum = new Number(e.getX(), e.getY(), playerSpaceOnBoard(e.getX(), e.getY()).getVal()); //set your portable number equal to the number you clicked on if it's a PlayerSpace
                     addMouseMotionListener(newNum);
                     playerSpaceOnBoard(e.getX(), e.getY()).setVal(0); //get rid of the number on the board. So this basically allows you to remove numbers from PlayerSpaces
                  }
               }
            }
            else{ } //You better not get to this else statement...
         }
      });   
      try
      {                
         beginUn = ImageIO.read(new File("BeginnerUnpressed.png"));     //Lots of images
         interUn = ImageIO.read(new File("IntermediateUnpressed.png"));
         exUn = ImageIO.read(new File("ExpertUnpressed.png"));
         begin = ImageIO.read(new File("BeginnerPressed.png"));
         inter = ImageIO.read(new File("IntermediatePressed.png"));
         ex = ImageIO.read(new File("ExpertPressed.png"));
         toggleUn = ImageIO.read(new File("ToggleUnpressed.png"));
         toggle = ImageIO.read(new File("TogglePressed.png"));
         resetUn = ImageIO.read(new File("resetUnpressed.png"));
         reset = ImageIO.read(new File("resetPressed.png"));
         checkUn = ImageIO.read(new File("checkUnpressed.png"));
         check = ImageIO.read(new File("checkPressed.png"));
         autoSolveUn = ImageIO.read(new File("AutoSolveUnpressed.png"));
         autoSolve = ImageIO.read(new File("AutoSolvePressed.png"));
         clearAnswersUn = ImageIO.read(new File("ClearAnswersUnpressed.png"));
         clearAnswers = ImageIO.read(new File("ClearAnswersPressed.png"));
         returnHomeUn = ImageIO.read(new File("ReturnHomeUnpressed.png"));
         returnHome = ImageIO.read(new File("ReturnHomePressed.png"));
      } 
      catch (IOException e){ }
   }
   ///////////////////////////////////////////// Constructor /////////////////////////////////////////////
   
   ///////////////////////////////////////////// GUI /////////////////////////////////////////////   
   public void paintComponent(Graphics g){ //paints all my stuff
      super.paintComponent(g);
      Graphics2D g2D = (Graphics2D)g; //outside code
      if(difficulty == 0){ //If you're at the title screen
         backgroundScreen = new Color(152, 252, 141);
         g.setColor(backgroundScreen); //draw the title screen stuff
         g.fillRect(0, 0, screenWidth, screenHeight);
         g.setColor(backgroundButtons);
         g.fillRect((screenWidth*3/4-480)/2 - 50, (screenHeight/2) - 200, 480 + 100, 420 + 100);
         g.drawImage(resize(beginUn, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2)-150, null);
         g.drawImage(resize(interUn, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2), null); 
         g.drawImage(resize(exUn, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2)+150, null);
         if(beginPressed) //There are a fair amount of these, they draw the pressed version of the button on top of the unpressed version
            g.drawImage(resize(begin, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2)-150, null);
         if(interPressed)
            g.drawImage(resize(inter, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2), null); 
         if(exPressed)
            g.drawImage(resize(ex, 480, 120), (screenWidth*3/4-480)/2, (screenHeight/2)+150, null);
         g.setFont(new Font("TimesNewRoman", Font.PLAIN, 80));
         g.setColor(Color.BLACK);
         g.drawString("Sudoku", (screenWidth*3/4-270)/2, 140);
         g2D.setStroke(new BasicStroke(7));  //setStroke() and setFont() are both outside code
         g2D.drawLine((screenWidth*3/4-270)/2 - 10, 140+20, (screenWidth*3/4-270)/2 + 280, 140+20);
         g.drawRect((screenWidth*3/4-480)/2 - 50, (screenHeight/2) - 200, 480 + 100, 420 + 100);
         g.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
         g.drawString("Created By: Christopher Taylor", 10, 30); //The one and only
      }
      else{ //If you're in the game
         linePositions[0] = 3;
         for(int i = 1; i < 10; i++){
            if(i%3 != 0)
               linePositions[i] = linePositions[i-1]+spaceHeight+1;
            else
               linePositions[i] = linePositions[i-1]+spaceHeight+3;
         }
         backgroundScreen = new Color(152, 252, 252);
         g.setColor(backgroundScreen); //draw the game stuff
         g.fillRect(0, 0, screenWidth*3/4, screenHeight);
         g.setColor(Color.BLACK);
         g2D.setStroke(new BasicStroke(3));
         g2D.drawLine(0, 2, linePositions[9], 2);
         g2D.drawLine(2, 0, 2, linePositions[9]);
         g2D.drawLine(0, linePositions[9], linePositions[9], linePositions[9]);
         g2D.drawLine(linePositions[9], 0, linePositions[9], linePositions[9]);
         g2D.setStroke(new BasicStroke(1));
         for(int i = 1; i < 9; i++){ //I decided to draw my own Sudoku board, that way it's pixel perfect
            if((i%3) != 0){
               g.drawLine(linePositions[i], 0, linePositions[i], linePositions[9]);
               g.drawLine(0, linePositions[i], linePositions[9], linePositions[i]);
            }
            else{
               g2D.setStroke(new BasicStroke(3));
               g.drawLine(linePositions[i], 0, linePositions[i], linePositions[9]);
               g.drawLine(0, linePositions[i], linePositions[9], linePositions[i]);
               g2D.setStroke(new BasicStroke(1));
            }
         }
               for(int i = 0; i < 9; i++){
                  for(int j = 0; j < 9; j++){
                     if((s.spaceAt(j, i).getVal() != 0)){ //If the space is a regular space
                        s.spaceAt(j, i).draw(g, linePositions[i]+1, linePositions[j]+1, s.spaceAt(j, i).getVal()); //draw it
                     }
                     else{ //If it's a PlayerSpace
                        if(showAnswers && s.playerSpaceAt(j, i).getVal() != 0 && s.playerSpaceAt(j, i).getVal() != s.answerAt(j, i).getVal()){ //And if you want to see the answers
                           System.out.println(s.playerSpaceAt(j, i).getVal());
                           s.playerSpaceAt(j, i).draw(g, linePositions[i]+1, linePositions[j]+1, Color.RED); //highlight all the spaces that are incorrect
                        }
                        else if(win() && s.playerSpaceAt(j, i).getVal() != 0) //If you've won the game
                           s.playerSpaceAt(j, i).draw(g, linePositions[i], linePositions[j], Color.GREEN); //paint the board in victory!
                        else //If you don't like cheating
                           s.playerSpaceAt(j, i).draw(g, linePositions[i]+1, linePositions[j]+1, Color.WHITE); //just draw the PlayerSpaces normally
                        for(int sc = 0; sc < 9; sc++) //draws the scribble numbers
                           s.playerSpaceAt(j, i).getScribble(sc).draw(g, linePositions[i], linePositions[j], (sc%3)*(spaceHeight/3)+spaceHeight/9, ((sc/3)%3)*spaceHeight/3+spaceHeight/3, s.playerSpaceAt(j, i).getScribbleNum(sc)); 
                     }
                  }
               }
            
         
         for(int i = 0; i < 9; i++){ //for each number on the right of the screen
            if(!scribbleNums){ //If the toggle is off
               g.setFont(new Font("TimesNewRoman", Font.PLAIN, 50)); //draw the numbers normally
               numbers[i].draw(g, boardHeight + 40, 10+((spaceHeight+6)*i), numbers[i].getVal(), spaceHeight/3+3, spaceHeight*2/3-6, Color.WHITE);
            }
            else{ //If the toggle is on
               g.setFont(new Font("TimesNewRoman", Font.PLAIN, 30)); //draw the numbers to show they are scribble numbers
               numbers[i].draw(g, boardHeight + 40, 10+((spaceHeight+6)*i), numbers[i].getVal(), (i%3)*spaceHeight/3+spaceHeight/9, ((i/3)%3)*spaceHeight/3+spaceHeight/3, Color.YELLOW);
            }
         }
         if(newNum.getVal() != 0 && newNum.getVal() >= 1 && newNum.getVal() <= 9 && !scribbleNums){ //If you have a number selected and toggle is off
            newNum.draw(g, newNum.getXPos(), newNum.getYPos(), newNum.getVal(), spaceHeight/3+3, spaceHeight*2/3-6, Color.WHITE); //draw it as it follows your mouse
         }
         else if(newNum.getVal() != 0 && newNum.getVal() >= 1 && newNum.getVal() <= 9 && scribbleNums){ //If toggle is on
            newNum.draw(g, newNum.getXPos(), newNum.getYPos(), newNum.getVal(), ((newNum.getVal()-1)%3)*spaceHeight/3+spaceHeight/9, (((newNum.getVal()-1)/3)%3)*spaceHeight/3+spaceHeight/3, Color.YELLOW); //draw it all scribbly
         }
         else ; //the number is zero, don't worry about drawing that
         g.drawImage(resize(toggleUn, 100, 100), screenWidth*3/4-150, 50, null);
         if(togglePressed)
            g.drawImage(resize(toggle, 100, 100), screenWidth*3/4-150, 50, null);
         g.setFont(new Font("TimesNewRoman", Font.PLAIN, 18));
         if(scribbleNums)
            g.drawString("Toggle is: on", screenWidth*3/4-150, 170);
         else
            g.drawString("Toggle is: off", screenWidth*3/4-150, 170);
         g.drawImage(resize(checkUn, 100, 100), screenWidth*3/4-150, screenHeight/4, null);
         if(checkPressed)
            g.drawImage(resize(check, 100, 100), screenWidth*3/4-150, screenHeight/4, null);
         if(showAnswers)
            g.drawString("Hints are: on", screenWidth*3/4-150, screenHeight/4 + 120);
         else
            g.drawString("Hints are: off", screenWidth*3/4-150, screenHeight/4 + 120);
         g.drawImage(resize(autoSolveUn, 100, 100), screenWidth*3/4-150, screenHeight*3/4, null);
         if(autoSolvePressed)
            g.drawImage(resize(autoSolve, 100, 100), screenWidth*3/4-150, screenHeight*3/4, null);
         g.drawImage(resize(clearAnswersUn, 100, 100), screenWidth*3/4-150, screenHeight/2, null);
         if(clearAnswersPressed)
            g.drawImage(resize(clearAnswers, 100, 100), screenWidth*3/4-150, screenHeight/2, null);
         if(win()){
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("TimesNewRoman", Font.PLAIN, 70));
            g.drawString("You won!", (boardHeight-300)/2, (boardHeight-100)/2 - 75);
            g.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(5));
            g.drawRect((boardHeight-400)/2-52, (boardHeight-100)/2-52, 504, 204);
            g.setColor(backgroundScreen);
            g.fillRect((boardHeight-400)/2-50, (boardHeight-100)/2-50, 500, 200);
            g.drawImage(resize(returnHomeUn, 400, 100), (boardHeight-400)/2, (boardHeight-100)/2, null);
               if(returnHomePressed)
                  g.drawImage(resize(returnHome, 400, 100), (boardHeight-400)/2, (boardHeight-100)/2, null);
         }
         if(!generated){ //If the board hasn't been generated yet
            g.setFont(new Font("TimesNewRoman", Font.PLAIN, 40)); //I'll hide the board so you don't see my algorithm working, it's top secret. Also it usually causes people ask, "is it supposed to be doing that...?"
            g.setColor(lightGray);
            g.fillRect(0, 0, boardHeight, boardHeight);
            g.setColor(Color.BLACK);
            g.drawString("Generating Board: Please Stand by.", 50, boardHeight/2);
         }
      }
      repaint(); 
   }
   ///////////////////////////////////////////// GUI /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Other Methods /////////////////////////////////////////////
   private static BufferedImage resize(BufferedImage img, int width, int height) {            //outside code
      Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);                   //     |
      BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  //     |
      Graphics2D g2D = resized.createGraphics();                                              //     |
      g2D.drawImage(tmp, 0, 0, null);                                                         //     |
      g2D.dispose();                                                                          //     |
      return resized;                                                                         //     |
   }                                                                                          //outside code
   public boolean win(){ //Checks winning condition based solely on the generated answer key. If the solution
                         //is not unique, then this method is not guaranteed to work
      for(int i = 0; i < 9; i++){
         for(int j = 0; j < 9; j++){
            if(s.spaceAt(i, j).getVal() != 0){
               if(s.spaceAt(i, j).getVal() != s.answerAt(i, j).getVal())
                  return false;
            }
            else{
               //System.out.println(s.playerSpaceAt(i, j).getVal());
               if(s.playerSpaceAt(i, j).getVal() != s.answerAt(i, j).getVal())
                  return false;
            }
         }
      }
      return true;
   }
   public boolean inSize(int x, int y, int xInit, int xFin, int yInit, int yFin){ //returns if you're inside a parameter-defined box
      return x >xInit && x < xFin && y > yInit && y < yFin;
   }
   public Space spaceOnBoard(int x, int y){ //converts screen position into the appropriate space
      return s.spaceAt((s.inBorders(x, y)/9)%9, s.inBorders(x, y)%9);
   }
   public PlayerSpace playerSpaceOnBoard(int x, int y){ //converts screen position into the appropriate player space
      return s.playerSpaceAt((s.inBorders(x, y)/9)%9, s.inBorders(x, y)%9);
   }
   ///////////////////////////////////////////// Other Methods /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Main /////////////////////////////////////////////
   public static void main(String [] args) //and now everything runs
   {    
      JFrame f = new JFrame();                           //outside code
      f.getContentPane().add(new Test());                //    |
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //    |
      f.setSize(screenWidth*3/4, screenHeight);                             //    |
      f.setVisible(true);                                //outside code
   }
   ///////////////////////////////////////////// Main /////////////////////////////////////////////
}