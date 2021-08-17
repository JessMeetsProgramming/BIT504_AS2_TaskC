import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;


import javax.swing.JPanel;
import javax.swing.Timer;

public class PongPanel extends JPanel implements KeyListener, ActionListener {
	
	private final static Color BACKGROUND_COLOUR = Color.BLACK;
	private final static int TIMER_DELAY = 5;
	private final static int BALL_MOVEMENT_SPEED = 2;
	private final static int POINTS_TO_WIN = 3;
	private final static int SCORE_TEXT_X = 100;
	private final static int SCORE_TEXT_Y = 100;
	private final static int SCORE_FONT_SIZE = 50;
	private final static String SCORE_FONT_FAMILY = "Serif";
	private final static int WINNER_TEXT_X = 200;
	private final static int WINNER_TEXT_Y = 200;
	private final static int WINNER_FONT_SIZE = 40;
	private final static String WINNER_FONT_FAMILY = "Serif";
	private final static String WINNER_TEXT = "WIN!";
	private final static String RESTART_GAME = "Hit Enter to Restart";
	
	int player1Score = 0;
	int player2Score = 0;
	boolean gameInitialised = false;
	Player gameWinner;
	Ball ball;
	GameState gameState = GameState.Initialising;
	Paddle paddle1, paddle2;
	
	
	public PongPanel() {
		setBackground(BACKGROUND_COLOUR);
		Timer timer = new Timer(TIMER_DELAY, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
	}
	
	
	
	public void createObjects() {
		ball = new Ball(getWidth(), getHeight());
		paddle1 = new Paddle(Player.One, getWidth(), getHeight());
        paddle2 = new Paddle(Player.Two, getWidth(), getHeight());
	}
	
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
         paintDottedLine(g);
         if(gameState != GameState.Initialising) {
             paintSprite(g, ball);
             paintSprite(g, paddle1);
             paintSprite(g, paddle2);
             paintScores(g);
             displayWin(g);
         }
				
	}
	
	private void paintDottedLine(Graphics g) {
		//initialise Graphics class and call create method to draw dotted line
		Graphics2D g2D = (Graphics2D) g.create();
		//declaring variable of type Stroke and assigning values
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
		//initialising the variables above using the graphics class - create
		g2D.setStroke(dashed);
		g2D.setPaint(Color.WHITE);
		g2D.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		g2D.dispose();
		
		
		
	}
	
	private void paintSprite(Graphics g, Sprite sprite) {
		g.setColor(sprite.getColour());
	    g.fillRect(sprite.getXPosition(), sprite.getYPosition(), sprite.getWidth(), sprite.getHeight());
	}
	
	private void paintScores(Graphics g) {
		Font scoreFont = new Font(SCORE_FONT_FAMILY, Font.BOLD, SCORE_FONT_SIZE);
        String leftScore = Integer.toString(player1Score);
        String rightScore = Integer.toString(player2Score);
        g.setFont(scoreFont);
        g.drawString(leftScore, SCORE_TEXT_X, SCORE_TEXT_Y);
        g.drawString(rightScore, getWidth()-SCORE_TEXT_X, SCORE_TEXT_Y);
	}
	
	private void displayWin(Graphics g) {
		//if there is a game winner
		if(gameWinner != null) {
		   //set font variables for graphics
           Font winnerFont = new Font(WINNER_FONT_FAMILY, Font.BOLD, WINNER_FONT_SIZE);
           //initialise the graphics to be displayed
           g.setFont(winnerFont);
           //declare variable to get x position of text placement
           int xPosition = getWidth() / 2;
           //if player one wins the game
           if(gameWinner == Player.One) {
        	   //x position will be on the left hand side of the screen
               xPosition -= WINNER_TEXT_X;
             // if player two wins the game
           } else if(gameWinner == Player.Two) {
        	   //x position will be on the right hand side of the screen
               xPosition += WINNER_TEXT_X;
           }
           // draw the winner graphics to the screen
           g.drawString(WINNER_TEXT, xPosition, WINNER_TEXT_Y);
           
           // draw the restart graphics to the screen if gamestate is gameover
           if (gameState == GameState.GameOver) {
        	   g.drawString(RESTART_GAME, (getWidth() / 4), (getHeight() - 100));
           }
       }
	}
	
	private void update() {
		
		switch(gameState) {
        case Initialising: {
            createObjects();		//create ball and paddle objects
           gameState = GameState.Playing;	//set current gamestate to playing
           ball.setXVelocity(BALL_MOVEMENT_SPEED);	//set ball movement speed
           ball.setYVelocity(BALL_MOVEMENT_SPEED);	//set ball movement speed
           break;
        }
        case Playing: {
        	moveObject(ball);   // Move ball
        	moveObject(paddle1);   //move paddles
            moveObject(paddle2);
            checkWallBounce();            // Check for wall bounce
            checkPaddleBounce();			//check for paddle bounce
            checkWin();				//check if a player has won the game
            break;
       }
       case GameOver: {
           break;
       }
   }
	}
	
	private void checkPaddleBounce() {
		if(ball.getXVelocity() < 0 && ball.getRectangle().intersects(paddle1.getRectangle())) {
	          ball.setXVelocity(BALL_MOVEMENT_SPEED);
	      }
	      if(ball.getXVelocity() > 0 && ball.getRectangle().intersects(paddle2.getRectangle())) {
	          ball.setXVelocity(-BALL_MOVEMENT_SPEED);
	      }
	}
	
	private void resetBall() {
		ball.resetToInitialPosition();
	}
	
	private void checkWallBounce() {
		 if(ball.getXPosition() <= 0) {
	           // Hit left side of screen
	           ball.setXVelocity(-ball.getXVelocity());
	           addScore(Player.Two);
	           resetBall();
	       } else if(ball.getXPosition() >= getWidth() - ball.getWidth()) {
	           // Hit right side of screen
	           ball.setXVelocity(-ball.getXVelocity());
	           addScore(Player.One);
	           resetBall();
	       }
	       if(ball.getYPosition() <= 0 || ball.getYPosition() >= getHeight() - ball.getHeight()) {
	           // Hit top or bottom of screen
	           ball.setYVelocity(-ball.getYVelocity());
	       }
	}
	
	private void moveObject(Sprite obj) {
		obj.setXPosition(obj.getXPosition() + obj.getXVelocity(),getWidth());
	      obj.setYPosition(obj.getYPosition() + obj.getYVelocity(),getHeight());
	}
	
	private void addScore(Player player) {
		if(player == Player.One) {
            player1Score++;
        } else if(player == Player.Two) {
            player2Score++;
        }
	}
	
	private void checkWin() {
		if(player1Score >= POINTS_TO_WIN) {
            gameWinner = Player.One;
            gameState = GameState.GameOver;
        } else if(player2Score >= POINTS_TO_WIN) {
            gameWinner = Player.Two;
            gameState = GameState.GameOver;
        }
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		 if(e.getKeyCode() == KeyEvent.VK_UP) {
             paddle2.setYVelocity(-1);
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
             paddle2.setYVelocity(1);
         }
		 if(e.getKeyCode() == KeyEvent.VK_W) {
             paddle1.setYVelocity(-1);
         } else if(e.getKeyCode() == KeyEvent.VK_S) {
             paddle1.setYVelocity(1);
         }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2.setYVelocity(0);
        }
		 if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
             paddle1.setYVelocity(0);
         }
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// call to update method
		update();
		repaint();
		
	}
	
	
	
}
