import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial") //Used to disable warnings about serialversionID
public class GameMain extends JPanel implements MouseListener{
	//Constants for game 
	// number of ROWS by COLS cell constants 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	/*declare game object variables*/
	// the game board 
	private Board board;
	
	//I created the enum similar to the player one and added the four states to this.
	private GameState currentState; 
	
	//Who is the current player
	private Player currentPlayer; 
	// for displaying game status message
	private JLabel statusBar;       
	

	/** Constructor to setup the UI and game components on the panel */
	public GameMain() {   
		
		//**As GameMain extends JPanel and implements MouseListener I have used 'super' to invoke (this) by referencing the parent class GameMain.
		//*The usage of super was decided as it had already been invoked for the paintComponent method and saved me time rather than having to add the 
		//*event listener and then put the mouseClicked method into that as well or adding a non used event. Information on using super was found at
		//*https://www.javatpoint.com/super-keyword
		super.addMouseListener(this);

		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("         ");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  
		
		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
		
		
		board = new Board(); //This will set the game board by calling a new instance of class 'Board'

		
		initGame(); //This will call and initialise the game variables to be used in the new game

	}
	
	public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				//create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
				
				//This sets a new instance of JFrame with the content pane. The content pane is where the child elements are inserted
	            frame.setContentPane(new GameMain());
	            
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Calling the JFrame operation of Exit on Close 
	           
				frame.pack(); //Causes the window to be resized to fit the elements             
				frame.setLocationRelativeTo(null); //Sets the location of the main panel. When set to Null it will be centred
				frame.setVisible(true); //When true the frame window will be visible
	         }
		 });
	}
	
	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
		//fill background and set colour to white
		super.paintComponent(g);
		setBackground(Color.WHITE);
		//ask the game board to paint itself
		board.paint(g);
		
		//set status bar message
		if (currentState == GameState.Playing) {          
			statusBar.setForeground(Color.BLACK);          
			if (currentPlayer == Player.Cross) {   
			
				statusBar.setText("It is X's turn"); //Using setText to display if it is X's turn in the status bar
				
			} else {    
				
				statusBar.setText("It is O's turn"); //Rather than writing another if statement the else will display it being O's turn as it it the only other option
			} 
			//Sets the status bar messages depending on the gamestate
			} else if (currentState == GameState.Draw) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("It's a Draw! Click to play again.");       
			} else if (currentState == GameState.Cross_won) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("'X' Won! Click to play again.");       
			} else if (currentState == GameState.Nought_won) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("'O' Won! Click to play again.");       
			}
		}
	
	  /** Initialise the game-board contents and the current status of GameState and Player) */
		public void initGame() {
			for (int row = 0; row < ROWS; ++row) {          
				for (int col = 0; col < COLS; ++col) {  
					//Initialises the game and sets the cells to empty
					board.cells[row][col].content = Player.Empty;           
				}
			}
			//Sets the current state to playing and the current player to cross 
			currentState = GameState.Playing;
			 currentPlayer = Player.Cross;
		}
		
		/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
		 * If they have the GameState is set to won for that player
		 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
		 *   
		 */
		public void updateGame(Player thePlayer, int row, int col) {
			//check for win after play
			if(board.hasWon(thePlayer, row, col)) {
				
				//To update the game I have use a nested if/else statement to see first if hasWon is true, then who is 'thePlayer' and assign who has won.
				//If no winner then an else statement checks for the isDraw then sets the currentState to draw.
				if(thePlayer == Player.Cross) {
					currentState = GameState.Cross_won;
				} else {
					currentState = GameState.Nought_won;
				}
				
			} else 
				if (board.isDraw ()) {
					
					currentState = GameState.Draw; //If not winner this will call the GameState draw enum and set the state to Draw
			}
		}
		
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
		public void mouseClicked(MouseEvent e) {  
		    //Using getX and getY we can find out where the mouse was clicked and store in variables            
			int mouseX = e.getX();             
			int mouseY = e.getY();             
	        //Now I know where the mouse was clicked I can take the cell and dividing by the cell size we can find the position to draw the player
			int rowSelected = mouseY / CELL_SIZE;             
			int colSelected = mouseX / CELL_SIZE;               			
			if (currentState == GameState.Playing) {                
				if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
					//The above checks to see if the selected cell is empty. If true then it is the current players move 
					board.cells[rowSelected][colSelected].content = currentPlayer; 
					//Once the move is made the game updates                  
					updateGame(currentPlayer, rowSelected, colSelected); 
					// Switch player
					if(currentPlayer == Player.Cross) {
						currentPlayer = Player.Nought;
					} else {
						currentPlayer = Player.Cross;
					}
				}
//				            
			} else {        
				// game over and restart              
				initGame();            
			}   
			repaint(); //Calling this awt component we only need to repaint a small area/component rather than the whole program       
//	           
		}	
	
	@Override
	public void mousePressed(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated,event not used
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated, event not used
		
	}
}


