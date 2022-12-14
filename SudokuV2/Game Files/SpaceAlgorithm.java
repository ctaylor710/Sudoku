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

public class SpaceAlgorithm implements Game{

   ///////////////////////////////////////////// Variables /////////////////////////////////////////////
   private int numRemoved; //used when generating the board
   private final int BOX = 3; //length of sub-boxes
   private final int LENGTH = 9; //length of each row and col
   private Space[][] spaces; //spaces that cannot be changed
   private Space[][] answerKey; //stores full spaces array before it's changed
   private PlayerSpace[][] playerSpaces; //spaces that can be changed
   private int[][] possibleNumbers; //keeps track of numbers not used yet by each space
   private int choice = 0; //used for selecting a value from possibleNumbers
   private int solvedRow = 0; //keeps track of the row the solve method is on
   private int solvedCol = 0; //keeps track of the column the solve method is on
   private int solvedNum = 1; //keeps track of the number the solve method is on
   private Space[][] solvedArray; //keeps track of the modified array in the solve method
   private boolean attemptFailed = true; //returns whether the solve method overflowed or not
   private ArrayList<TreeSet> unusedSpace = new ArrayList<TreeSet>(); //holds values of which numbers don't work for every space
   ///////////////////////////////////////////// Variables /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Constructor /////////////////////////////////////////////
   public SpaceAlgorithm(int nR){
      spaces = new Space[LENGTH][LENGTH];
      playerSpaces = new PlayerSpace[LENGTH][LENGTH];
      answerKey = new Space[LENGTH][LENGTH];
      solvedArray = new Space[LENGTH][LENGTH];
      for(int i = 0; i < LENGTH; i++){
         for(int j = 0; j < LENGTH; j++){
            spaces[i][j] = new Space(new Number(0));
            playerSpaces[i][j] = new PlayerSpace(new Number(0));
            answerKey[i][j] = new Space(new Number(0));
            solvedArray[i][j] = new Space(new Number(0));
         }
      }
      unusedSpace = new ArrayList<TreeSet>(LENGTH*LENGTH);
      possibleNumbers = new int[LENGTH*LENGTH][LENGTH];
      for(int i = 0; i < LENGTH*LENGTH; i++){
         TreeSet<Integer> ts = new TreeSet<Integer>();
         unusedSpace.add(ts);
         for(int j = 0; j < LENGTH; j++){     //fills possibleNumbers with the numbers 1-9 in a random order
            int k = (int)(Math.random()*9)+1; //this significantly cuts down on time, instead of constantly generating
            if(!alreadyUsed(i, k))            //random numbers for spaces you do it once
               possibleNumbers[i][j] = k;
            else
               j--;
         }
      }
      numRemoved = nR;
   }
   ///////////////////////////////////////////// Constructor /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Some Simple Methods /////////////////////////////////////////////
   public void draw(Graphics g, int x, int y, int val){ ; } //inherited method, has no use here
   public boolean alreadyUsed(int row, int val){ //returns if that number has already been used in a specific space
      for(int i = 0; i < LENGTH; i++){
         if(possibleNumbers[row][i] == val)
            return true;
      }
      return false;
   }
   public Space spaceAt(int row, int col){ //returns a specific space from the space array
      return spaces[row][col];
   }
   public Space answerAt(int row, int col){ //returns a specific space from the answer key
      return answerKey[row][col];
   }
   public PlayerSpace playerSpaceAt(int row, int col){ //returns a specific spaces from the playerSpace array
      return playerSpaces[row][col];
   }
   public int inBorders(int xVal, int yVal){ //used in the test class to check which space the mouse is in
      return ((xVal/spaceHeight))+9*(yVal/spaceHeight);
   }
   public Space[][] getSpaces(){ return spaces; } //returns the space array
   public Space[][] getAnswer(){ return answerKey; } //returns the answer key
   public void setSpaces(Space[][] arr){ spaces = arr; } //used for auto solving
   public int getSpaceVal(int row, int col){ //returns the value of a specific space
      return spaces[row][col].getVal();
   }
   public void setNumRemoved(int nR){numRemoved = nR;} //allows me to change numRemoved in other classes
   public boolean unusedInRow(Space[][] arr, int r, int num){ //checks if a number has not been used in a row yet
      for(int c = 0; c < LENGTH; c++){
         if(arr[r][c].getVal() == num)
            return false;
      }
      return true;
   }
   public boolean unusedInCol(Space[][] arr, int c, int num){ //checks if a number has not been used in a column yet
      for(int r = 0; r < LENGTH; r++){
         if(arr[r][c].getVal() == num)
            return false;
      }
      return true;
   }
   public boolean unusedInBox(Space[][] arr, int row, int col, int num){ //checks if a number has not been used in a box yet
      int rowBegin = row-row%BOX; //these find upper-left corner of the box
      int colBegin = col-col%BOX;
      for(int r = rowBegin; r < rowBegin+BOX; r++){
         for(int c = colBegin; c < colBegin+BOX; c++){
            if(arr[r][c].getVal() == num){
               return false;
            }
         }
      }
      return true;
   }
   ///////////////////////////////////////////// Some Simple Methods /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Test A Number /////////////////////////////////////////////
   public int genNum(int row, int col, int remain){ //selects a random number for the current space
      return possibleNumbers[9*row+col][choice];
   }
   public int possibleInt(int row, int col, int remain, int i){ //returns the number from genNum if it hasn't been used yet
      if(remain<=0){ //if there are no remaining numbers at the space
         unusedSpace.get(i).clear(); //make all numbers usable again and return that there aren't any left
         remain = 9;
         return -1;
      }
      int num = genNum(row, col, remain);
      if(choice < 8) //runs through the possibleNumbers array for all values, 1-9
         choice++;
      else
         choice = 0;
      if(!unusedSpace.get(i).contains(num)){ //if this number is unused
         unusedSpace.get(i).add(num); //return the now used number
         return num;
      }
      return possibleInt(row, col, remain-1, i); //if the number has already been tried, recursively try another number until one works (or until none do)
   }
   ///////////////////////////////////////////// Test A Number /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Main Algorithm /////////////////////////////////////////////
   public boolean addSpaces(int row, int col){ //creates the sudoku board
      if(row == 9 && col == 0){ //if you filled in the last number on the board
         for(int i = 0; i < LENGTH; i++){ //create an answer key
            for(int j = 0; j < LENGTH; j++){
               answerKey[i][j].setVal(spaces[i][j].getVal());
            }
         }
         for(int i = 0; i < LENGTH * LENGTH; i++) //return everything back to its original condition
            unusedSpace.get(i).clear();
         for(int i = 0; i < LENGTH*LENGTH; i++){
            for(int j = 0; j < LENGTH; j++){
               possibleNumbers[i][j] = 0;
            }
         }
         for(int i = 0; i < LENGTH*LENGTH; i++){
            for(int j = 0; j < LENGTH; j++){
               int k = (int)(Math.random()*9)+1;
               if(!alreadyUsed(i, k))
                  possibleNumbers[i][j] = k;
               else
                  j--;
            }
         }
         return true; //it worked, hooray!
      }
      int num = possibleInt(row, col, 9, 9*row+col); //set num to a possible number
      if(num == -1){ //if there are no more positive numbers
         if(col > 0){ //return the space to 0 and move back a space
            spaces[row][col].setVal(0);
            return addSpaces(row, col-1);
         }
         else{ //moves back to previous row if you're at the beginning of the row
            spaces[row][col].setVal(0);
            return addSpaces(row-1, LENGTH-1);
         }
      }
      if(unusedInRow(spaces, row, num) && unusedInCol(spaces, col, num) && unusedInBox(spaces, row, col, num)){ //if the number doesn't conflict with any others
         spaces[row][col].setVal(num); //use this number in the space
         if(col < 8) //move on to the next space
            return addSpaces(row, col+1);
         else
            return addSpaces(row+1, 0);
      }
      else //otherwise try again with a different number
         return addSpaces(row, col);
      
   }
   ///////////////////////////////////////////// Main Algorithm /////////////////////////////////////////////
   
   ///////////////////////////////////////////// More Simple Methods /////////////////////////////////////////////
   public int remainingInRow(int row){ //returns how many numbers haven't been removed in the row
      int count = 0;
      for(int i = 0; i < LENGTH; i++){
         if(spaces[row][i].getVal() != 0)
            count++;
      }
      return count;
   }
   public int remainingInCol(int col){ //returns how many numbers haven't been removed in the column
      int count = 0;
      for(int i = 0; i < LENGTH; i++){
         if(spaces[i][col].getVal() != 0)
            count++;
      }
      return count;
   }
   public int remainingInBox(int row, int col){ //returns how many numbers haven't been removed in the box
      int rowBegin = row-row%BOX;
      int colBegin = col-col%BOX;
      int count = 0;
      for(int i = rowBegin; i < rowBegin+BOX; i++){
         for(int j = colBegin; j < colBegin+BOX; j++){
            if(spaces[i][j].getVal() != 0)
               count++;
         }
      }
      return count;
   }
   public boolean removable(int row, int col, int amount){ //returns if you can remove the number or not
      if(remainingInRow(row) >= amount && remainingInCol(col) >= amount && remainingInBox(row, col) >= amount) //checks if there are still a certain amount of numbers in the row, column,
         return true;                                                                                          //and box of the space. The amount left is determined by difficulty
      else
         return false;
   }
   ///////////////////////////////////////////// More Simple Methods /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Difficulty Settings /////////////////////////////////////////////
   public void removeSpaces(int difficulty){ //randomly removes spaces from the filled in Sudoku board
      for(int row = 0; row < 3; row++){ //begins by setting a maximum amount of spaces for each box
         for(int col = 0; col < 3; col++){
            while(remainingInBox(row*3, col*3) > difficulty+3){ //once again, amount remaining is determined by difficulty
               int i = (int)(Math.random()*3)+(3*row);
               int j = (int)(Math.random()*3)+(3*col);
               if(spaces[i][j].getVal() != 0){
                  playerSpaces[i][j] = new PlayerSpace(spaces[i][j].getNum());
                  spaces[i][j].setVal(0);
               }
            }
         }
      }
      for(int i = 0; i < numRemoved-(9*(9-(difficulty+3))); i++){ //removes remaining spaces until a numRemoved amount have been removed
         int row = (int)(Math.random()*9);
         int col = (int)(Math.random()*9);
         if(spaces[row][col].getVal() != 0 && removable(row, col, difficulty)){ //if the space can be removed
            playerSpaces[row][col] = new PlayerSpace(spaces[row][col].getNum()); //do it
            spaces[row][col].setVal(0);
         }
         else //otherwise try again somewhere else
            i--;
      }
   }
   ///////////////////////////////////////////// Difficulty Settings /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Solving Methods /////////////////////////////////////////////
   public int previousRow(Space[][] arr, int row, int col){ //returns the previous empty space's row on the board
      if(arr[row][col].getPlayed()) //if the space is playable
         return row; //return it's row position
      else{  //otherwise recursively go backwards until you find a space
         if(col > 0)
            return previousRow(arr, row, col-1);
         else
            return previousRow(arr, row-1, LENGTH-1);
      }
   }
   public int previousCol(Space[][] arr, int row, int col){ //returns the previous empty space's column on the board
      if(arr[row][col].getPlayed())
         return col;
      else{
         if(col > 0)
            return previousCol(arr, row, col-1);
         else
            return previousCol(arr, row-1, LENGTH-1);
      }
   }
   public int nextRow(Space[][] arr, int row, int col){ //returns the next empty space's row on the board
      if(row == 9 && col == 0) //if you reach the end of the board and it's not a playable space
         return row; //return that you've reached the end
      if(arr[row][col].getPlayed())
         return row;
      else{
         if(col+1 < 9)
            return nextRow(arr, row, col+1);
         else
            return nextRow(arr, row+1, 0);
      }
   }
   public int nextCol(Space[][] arr, int row, int col){ //returns the next empty space's column on the board
      if(row == 9 && col == 0)
         return col;
      if(arr[row][col].getPlayed())
         return col;
      else{
         if(col+1 < 9)
            return nextCol(arr, row, col+1);
         else
            return nextCol(arr, row+1, 0);
      }
   }
   public boolean solve(Space[][] arr, int row, int col, int num){ //solves the given Sudoku board
      solvedRow = row; //update the variables with each pass through
      solvedCol = col;
      solvedNum = num;
      solvedArray = arr;
      if(row == 9 && col == 0){ //if you reached the end
         attemptFailed = false; //the attempt did not fail this time!
         solvedRow = 0; //returns values back to normal
         solvedCol = 0;
         solvedNum = 9; //this is solveReverse
         return true;
      }
      if(num == 10){ //if you've tried all the numbers, 1-9, in the space
         if(col > 0){ //recursively go back the the last playable space and continue counting up from where you left off
            arr[row][col].setVal(0);
            return solve(arr, previousRow(arr, row, col-1), previousCol(arr, row, col-1), arr[previousRow(arr, row, col-1)][previousCol(arr, row, col-1)].getVal()+1);
         }
         else{
            arr[row][col].setVal(0);
            return solve(arr, previousRow(arr, row-1, LENGTH-1), previousCol(arr, row-1, LENGTH-1), arr[previousRow(arr, row-1, LENGTH-1)][previousCol(arr, row-1, LENGTH-1)].getVal()+1);
         }
      }
      if(arr[row][col].getPlayed() && unusedInRow(arr, row, num) && unusedInCol(arr, col, num) && unusedInBox(arr, row, col, num)){ //if a number works
         arr[row][col].setVal(num); //use it
         if(col < 8){ //and recursively go to the next playable space
            return solve(arr, nextRow(arr, row, col+1), nextCol(arr, row, col+1), 1);
         }
         else
            return solve(arr, nextRow(arr, row+1, 0), nextCol(arr, row+1, 0), 1);
      }
      else{
         if(row == 0 && col == 0 && !arr[0][0].getPlayed())//if its didn't work because the program started on a non-playable number
            return solve(arr, nextRow(arr, row, col+1), nextCol(arr, row, col+1), 1);//find the first playable space
         return solve(arr, row, col, num+1); //otherwise try the next number
      }
      
   }
   public boolean solveReverse(Space[][] arr, int row, int col, int num){ //this is identical to the solve method, only it
      solvedRow = row;                                                    //checks each space going from 9-1 instead of 1-9. Used to check uniqueness.
      solvedCol = col;
      solvedNum = num;
      solvedArray = arr;
      if(row == 9 && col == 0){
         attemptFailed = false;
         return true;
      }
      if(num == 0){
         if(col > 0){
            arr[row][col].setVal(0);
            return solveReverse(arr, previousRow(arr, row, col-1), previousCol(arr, row, col-1), arr[previousRow(arr, row, col-1)][previousCol(arr, row, col-1)].getVal()-1);
         }
         else{
            arr[row][col].setVal(0);
            return solveReverse(arr, previousRow(arr, row-1, LENGTH-1), previousCol(arr, row-1, LENGTH-1), arr[previousRow(arr, row-1, LENGTH-1)][previousCol(arr, row-1, LENGTH-1)].getVal()-1);
         }
      }
      if(arr[row][col].getPlayed() && unusedInRow(arr, row, num) && unusedInCol(arr, col, num) && unusedInBox(arr, row, col, num)){
         arr[row][col].setVal(num);
         if(col < 8){
            return solveReverse(arr, nextRow(arr, row, col+1), nextCol(arr, row, col+1), 9);
         }
         else
            return solveReverse(arr, nextRow(arr, row+1, 0), nextCol(arr, row+1, 0), 9);
      }
      else{
         if(row == 0 && col == 0 && !arr[0][0].getPlayed())
            return solveReverse(arr, nextRow(arr, row, col+1), nextCol(arr, row, col+1), 9);
         return solveReverse(arr, row, col, num-1);
      }
   }
   ///////////////////////////////////////////// Solving Methods /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Unique Solution Check /////////////////////////////////////////////
   public boolean isUnique(Space[][] arr, int row, int col){ //returns whether or not the generated board has only 1 solution. This is not
      Space[][] temp = new Space[LENGTH][LENGTH];            //a fullproof method but it is very close to fully reliable.
      for(int i = 0; i < LENGTH; i++){ //sets the temp array to the array parameter
         for(int j = 0; j < LENGTH; j++){
            temp[i][j] = new Space(new Number(arr[i][j].getVal()));
            if(temp[i][j].getVal() == 0) //makes note of which spaces are playable
               temp[i][j].setPlayed(true);
         }
      }
      solvedArray = temp; //now we have an array to solve, which won't modify the original array
      while(attemptFailed){                                       //A little explanation on this section. Sudoku solving algorithms are not at all efficient. A common error when running this
         try{                                                     //method was that I would get a StackOverflowError because the method would recursively call itself too many times. The try catch stops the program
            solve(solvedArray, solvedRow, solvedCol, solvedNum);  //from breaking when the thread overflows, and then the while loop continues the solving algorithm, with the "solved variables allowing me to start where I left off.
         }catch( StackOverflowError error ){ attemptFailed = true; }
      }
      attemptFailed = true; //now we do the same thing as above, but we solve with the solveReverse method. 
      Space[][] tempReverse = new Space[LENGTH][LENGTH];
      for(int i = 0; i < LENGTH; i++){
         for(int j = 0; j < LENGTH; j++){
            tempReverse[i][j] = new Space(new Number(arr[i][j].getVal()));
            if(tempReverse[i][j].getVal() == 0)
               tempReverse[i][j].setPlayed(true);
         }
      }
      solvedArray = tempReverse;
      while(attemptFailed){
         try{
            solveReverse(solvedArray, solvedRow, solvedCol, solvedNum);
         }catch( StackOverflowError error ){ attemptFailed = true; }
      }
      for(int i = 0; i < LENGTH; i++){                                                                                            //I found that the most common feature that created a Sudoku board with multiple solutions was symmetry. What I mean
         for(int j = 0; j < LENGTH; j++){                                                                                         //by this is a group of numbers that has rotational symmetry about a row or column. By solving the board with both methods,
            if(!(temp[i][j].getVal() == tempReverse[i][j].getVal() && tempReverse[i][j].getVal() == answerKey[i][j].getVal()))    //I can detect any symmetry by comparing both solved arrays and the answer key to see if they are all the same.
               return false;
         }
      }
      return true;
   }
   ///////////////////////////////////////////// Unique Solution Check /////////////////////////////////////////////
   
   ///////////////////////////////////////////// Debug Method (?) /////////////////////////////////////////////
   public String toString(){ //prints the unsolved array
      for(int i = 0; i < LENGTH; i++){
         System.out.print("[");
         for(Space s : spaces[i])
            System.out.print(s.getVal());
         System.out.println("]");
      }
      return "";
   }
   ///////////////////////////////////////////// Debug Method (?) /////////////////////////////////////////////          
}