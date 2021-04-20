import java.awt.*;

public class Board {
	// grid line width
	public static final int GRID_WIDTH = 6;
	// grid line half width
	public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2;
	
	//2D array of ROWS-by-COLS Cell instances
	Cell [][] cells;
	
	/** Constructor to create the game board */
	public Board() {
	 
		cells = new Cell[GameMain.ROWS][GameMain.COLS]; //Setup the cell array by calling the constants of ROW and COLS from GameMain and putting into the array brackets
		
		//The for loop starts with zero then increments while row is less than ROWS which is hardcoded to 3. Once row is set another loop runs for col
		//until col is no longer < than COLS. Once both are done a new instance of Cell array is set.
		for (int row = 0; row < GameMain.ROWS; ++row) {
			for (int col = 0; col < GameMain.COLS; ++col) {
				cells[row][col] = new Cell(row, col);
				}
			}
		}
	

	 /** Return true if it is a draw (i.e., no more EMPTY cells) */ 
	public boolean isDraw() {
		
		//Uses the same loop as the initialise board but adds an if clause to see if the cell content is empty by calling the Player enum 'Empty'
		for (int row = 0; row < GameMain.ROWS; ++row) {
	         for (int col = 0; col < GameMain.COLS; ++col) {
	            if (cells[row][col].content == Player.Empty) {
	               return false; //a space has been found on the board so it will return false and the fame keeps going
	            }
	         }
	      }
	      return true; //If no empty spaces were found and the game didn't produce a winner then it returns true as the game is a draw
	   }
	
	
	/** Return true if the current player "thePlayer" has won after making their move  */
	public boolean hasWon(Player thePlayer, int playerRow, int playerCol) {
		 // check if player has 3-in-that-row. This will take first row (0) then next row (1) then last row (2)
		if(cells[playerRow][0].content == thePlayer && cells[playerRow][1].content == thePlayer && cells[playerRow][2].content == thePlayer )
			return true;
		
		//When just changing playerRow to playerCol nothing will happen when you have 3 in a row. By moving the number after cells we check for the first cell
		//then call the column associated with it.
		if(cells[0][playerCol].content == thePlayer && cells[1][playerCol].content == thePlayer && cells[2][playerCol].content == thePlayer )
			return true;
		 
		
		 // 3-in-the-diagonal top left to bottom right
		else if( cells[0][0].content == thePlayer && cells[1][1].content == thePlayer && cells[2][2].content == thePlayer)
			return true;
		 
		// 3-in-the-diagonal bottom left to top right by reversing the cell numbers. Because we are not dealing with just a row or column I have had to specify 
		//both the row and column cell as we are spanning all.
		else if( cells[2][0].content == thePlayer && cells[1][1].content == thePlayer && cells[0][2].content == thePlayer)
					return true;
		
		return false;
	}
	
	/**
	 * Draws the grid (rows then columns) using constant sizes, then call on the
	 * Cells to paint themselves into the grid
	 */
	public void paint(Graphics g) {
		//draw the grid
		g.setColor(Color.black);
		for (int row = 1; row < GameMain.ROWS; ++row) {          
			g.fillRoundRect(0, GameMain.CELL_SIZE * row - GRID_WIDHT_HALF,                
					GameMain.CANVAS_WIDTH - 1, GRID_WIDTH,                
					GRID_WIDTH, GRID_WIDTH);       
			}
		for (int col = 1; col < GameMain.COLS; ++col) {          
			g.fillRoundRect(GameMain.CELL_SIZE * col - GRID_WIDHT_HALF, 0,                
					GRID_WIDTH, GameMain.CANVAS_HEIGHT - 1,                
					GRID_WIDTH, GRID_WIDTH);
		}
		
		//Draw the cells
		for (int row = 0; row < GameMain.ROWS; ++row) {          
			for (int col = 0; col < GameMain.COLS; ++col) {  
				cells[row][col].paint(g);
			}
		}
	}
}
