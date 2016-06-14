package ktr;

//import to create ArrayLists
import java.util.ArrayList;
//import for additional array functions
import java.util.Arrays;

//import Processing-specific libraries
import processing.core.*;
//import for key press/release functions
import processing.event.KeyEvent;
//import to implement music
import ddf.minim.*;
//import ddf.minim.signals.*;
//import ddf.minim.analysis.*;
//import ddf.minim.effects.*;
//import ddf.minim.AudioPlayer.*;

/**
 * Laser Mirrors
 * @version 1.2
 * @author Karl Toby Rosenberg
 * April 13, 2015
 * 
 * Original game concept and music composition also by Karl Toby Rosenberg
 * *********************
 * Please read "Instructions_Read_Me" for controls and detailed information/notes
 */
@SuppressWarnings("serial")
public class Mirror_Main extends PApplet
{
	/**
	 * main
	 * 
	 * allow the program to run as an application rather than as an applet
	 * @param String[] args
	 */
	public static void main(String[] args)
	{
		PApplet.main(new String[] {"--present", Mirror_Main.class.getName() });
	}
	/*
	 * Allow for background music
	 */
	AudioPlayer musicPlayer;
	//plays one of my musical compositions
	Minim minim;
	/*
	 * future implementation of sound effects
	 */
	//AudioPlayer sfxAbsorbPlayer;
	//Minim sfxAbsorb;
	
	//PI constant
	public static final double PI = Math.PI;
	
	//create main laser object
	Laser laser = new Laser();
	
	//set background size
	private final int sizeBackground = 1000;

	//initial time to measure appropriate delay time
	public long initialTime = System.currentTimeMillis();
	//another initial time for separate delay mechanism
	public long initialTime2 = System.currentTimeMillis();
	
	//sets whether game should pause/delay after movement out of bounds or after side of mirror hit
	public boolean delayOn = true;
	
	//ArrayList of mirrors
	private ArrayList<Mirror> mirrors = new ArrayList<Mirror>();
	//ArrayList of map spaces (The current version has a constant number of spaces, but perhaps in the future
	//the number of spaces could change, especially if there is a scrolling background. An ArrayList would make this easier,
	//though I may try to re-make and improve the game in a different language in the future.
	private ArrayList<Map_Space> spaces = new ArrayList<Map_Space>();
	
	/*
	 * initiate points system
	 */
	//create points object
	private Points points = new Points();

	//set point multiplier
	private int pointMultiplier = 1;
	//record highest point multiplier
	private int highestPointMultiplier = 1;
	//set point increment
	private int pointIncrement = 500;
	//set point decrement
	private int pointDecrement = -1000;
	//create total points counter
	private int totalPoints = 0;
	//record highest consecutive points value
	private int consecutivePoints = 0;
	//count number of consecutive points collected
	private int consecutivePointCollect = 0;

	/*
	 * implement HUD
	 */
	PFont pointDisplay;

	
	//detect which keys held
	boolean upHeld    = false;
	boolean downHeld  = false;
	boolean leftHeld  = false;
	boolean rightHeld = false;

	//declare background image
	PImage bg;
	//declare array to contain mirror animation frames
	PImage [] mirror_frames = new PImage[4];
	//set animation frame to 0
	public int mirror_current_frame = 0;
	
	/*
	 *placeholder to contain laser animation frames, current version uses Processing graphics
	 */
	//PImage laserImage;
	//PImage [] laser_frames = new PImage[3];
	
	/*
	 * read scores contained in save file
	 */
	//load strings in file, store in array
	String[] highScores = loadStrings("high_Score");
	//create ArrayList from array
	ArrayList<String> currentHighScores = new ArrayList<String>(Arrays.asList(highScores));
	//store current highest score in two variables
	String currentHighScore = currentHighScores.get(0);
	String highScoreDisplay = currentHighScore;
	
	//store game start time
	long timeStart = millis();
	//set game timer in seconds
	int  timer = 601;
	
	/*
	 * main setup
	 */
	public void setup()
	{
		//use if statement to reset high scores manually
		/*
		if(false)
		{
			String[] reset = {"0"};
			saveStrings("high_Score", reset);
		}
		*/

		//set HUD font
		pointDisplay = createFont("Cambria",22,true);
		
		//finish implementation of background music
		minim = new Minim(this);
		//load original musical composition to play in the background
		musicPlayer = minim.loadFile("Floating_Point_ver_3_6_2.mp3", 8192);
		//play file
		musicPlayer.play();
		//instruct program to loop music playback
		musicPlayer.loop();


		/*
		 * UNUSED sound effect implementation
		sfxAbsorb = new Minim(this);
		sfxAbsorbPlayer = sfxAbsorb.loadFile("Absorb_1.mp3", 1024);
		*/
		

		//set frame rate
		frameRate(30);
		
		//load background image
		bg = loadImage("ver_1.png");
		//load mirror frames, store in array
		mirror_frames[0] = loadImage("Mirror_default.png");
		mirror_frames[1] = loadImage("Mirror_leftward_slant.png");
		mirror_frames[2] = loadImage("Mirror_rightward_slant.png");
		mirror_frames[3] = loadImage("Mirror_vertical.png");

		//spawn initial points marker
		points.spawn();
		//set window size
		size(sizeBackground, sizeBackground);
		// background(255, 248, 220);
		
		/* add grid blocks to arrayList (row at a time)
		 */
		for (int row = 0; row < sizeBackground; row += sizeBackground / 5)
		{
			for (int space = 0; space < sizeBackground; space += sizeBackground / 5)
			{
				//add new Map_Space object to ArrayList, 
				//uses constructor with specific coordinates (ArrayList despite constant spaces to allow for variable number of spaces)
				spaces.add(new Map_Space(space, row));
			}
		}
		
		/* add mirrors to arrayList (by alternating grid slots)
		 */
		for (int yM = 0; yM < 3; yM += 1)
		{
			for (int xM = 0; xM < 3; xM += 1)
			{
				//adjust counters to represent center coordinates
				int xcoord = xM * 400 + 100;
				int ycoord = yM * 400 + 100;
				//skip 23rd grid slot
				if(xcoord == 500 && ycoord == 900)
				{
					continue;
				}
				mirrors.add(new Mirror(xcoord, ycoord));
				//testing: prints x and y coordinates of each mirror object
				//System.out.println("X: " + xcoord + "\t" + "Y: " + ycoord);
			}
		}
	}

	/*
	 * main draw
	 */
	public void draw()
	{
		//check whether points multiplier should be adjusted
		//multiplier increases by factors of 2, 
		//number of point markers necessary to collect increases
		if(consecutivePointCollect >= (5*pointMultiplier))
		{
			//adjust multipliers and increments
			pointMultiplier *= 2;
			pointIncrement *= 2;
			highestPointMultiplier *= 2;
			initialTime2 = System.currentTimeMillis();
			
		}

		//if current system time exceeds initial time set by 2000, end delay 
		//(game begins with a delay of approximately 2 seconds)
		if(System.currentTimeMillis() - initialTime  >= 2000 && delayOn)
		{
			delayOn = false;
		}
		
		//set image mode to corner, reload background image at origin
		imageMode(CORNER);
		image(bg, 0, 0);
		
		//display points, HUD
		textFont(pointDisplay, 22);
		fill(0, 0, 225, 180);
		
		
		//adjust count-down timer if approximately 1000 have elapsed
		if(millis() - timeStart >= 1000)
		{
			//reset second timer to current time
			timeStart = millis();
			//decrement 1(second) from count-down timer
			timer -= 1;
			//end game sequence 1: time has elapsed
			if(timer < 0)
			{
				//pause music player
				musicPlayer.pause();
				//store current time
				long currentTime = System.currentTimeMillis();
				
				while (true)
				{
					//pause game (in while loop), use updateScores method to adjust high score save file if necessary
					updateScores(currentHighScores, currentHighScore, totalPoints);
					
					//if end sequence has lasted approximately 2 seconds, exit program
					if(System.currentTimeMillis() - currentTime >= 2000)
					{
						//close music player
						musicPlayer.close();
						System.exit(0);
					}
				}
			}
		}
		//display timer
		text(Integer.toString(timer), 485, 25);
		
		//author display
		text("TOBY ROSENBERG", 810, 980);
		
		//high score update check
		//if current total points exceed high score on record, set high score display to current points
		if(totalPoints > Integer.parseInt(highScoreDisplay))
		{
			highScoreDisplay = Integer.toString(totalPoints);
		}
		//high score display
		text("HIGH SCORE: " + highScoreDisplay, 210, 980);
		//point multiplier display
		text("X" + pointMultiplier, 10, 960);
		//current total points display
		text("POINTS: " + totalPoints, 10, 980);
		
		//detect key controls
		keyControls();
		
		/*
		 * display each grid space
		 */
		drawMapGrid();

		/*
		 * mirror logic
		 */
		placeMirrors();
		
		/*
		 * points
		 */
		ellipseMode(CENTER);
		stroke(255, 255, 255);
		strokeWeight(1);
		fill(0, 0, 200);
		//draw point marker at current point marker coordinates (use Points-specific getters)
		ellipse(points.getXPosition(), points.getYPosition(), 25, 25);
		fill(0, 0, 200);

		/*
		 * laser logic
		 */
			rectMode(CENTER);
			stroke(240, 0, 0);
			strokeWeight(1);
			fill(255, 0, 0, 200);

		//if laser out-of-bounds, reset laser coordinates, pause game temporarily (set initial time)
		if (laser.getXPosition() < -25 || laser.getYPosition() < -25 || laser.getXPosition() > 1025 || laser.getYPosition() >1025)
		{
			laserReset(laser);
			delayOn = true;
			initialTime = System.currentTimeMillis();
		}
		
		//if delay is off, proceed with laser movement and collision logic
		if(!delayOn)
		{
			//draw laser graphics at current coordinates (use getters in Laser class)
			rect(laser.getXPosition(), laser.getYPosition(), 10, 10);
			fill(255, 255, 255, 200);
			rect(laser.getXPosition(), laser.getYPosition(), 5, 5);
			fill(100, 0, 0);
	
			//check collision between laser and mirrors (checkDirectCollision method) 
			if (checkDirectCollision(mirrors, laser))
			{
				//if collision has occurred, determine new laser direction based on previous movement direction and mirror angle
				determineReflectionDirection();
			}
			//else move laser in current direction
			else
			{
				//method moves laser given x and y direction values (uses current values unchanged)
				laser.moveLaser(laser.getXDirectionValue(), laser.getYDirectionValue());
			}
			//check collision between laser and points markers
			checkDirectCollision(points, laser);
		}
		
		//end game sequence 2: quit by pressing mouse and holding shift
		if (mousePressed && keyCode == SHIFT)
		{
			//pause music player
			musicPlayer.pause();
			//store current time
			initialTime = System.currentTimeMillis();
			
			//adjust high score record if necessary
			updateScores(currentHighScores, currentHighScore, Integer.parseInt(highScoreDisplay));
			
			while (true)
			{	//if end game sequence has lasted approximately 2000 seconds, exit program
				if(System.currentTimeMillis() - initialTime >= 2000)
				{
					//close music player
					musicPlayer.close();
					System.exit(0);
				}
			}
		}
		
	}
	
	////////////////////////METHODS////////////////////////
	
	/**
	 * drawMapGrid
	 * draws each map grid space
	 */
	public void drawMapGrid()
	{
		stroke(0);
		strokeWeight(0);
		fill(0, 0, 0, 0);
		//corner coordinates rather than center coordinates (laser and mirror use center coordinates)
		rectMode(CORNER);

		//iterates through spaces ArrayList spaces containing map spaces references
		for (int o = 0; o < 25; o++)
		{
			//select current map space object
			Map_Space currentSpace = spaces.get(o);
			
			//draw square outline at each location, 
			//uses getters to extract X and Y positions for each object
			rect(currentSpace.getXPosition(), currentSpace.getYPosition(), 200, 200);
		}
	}
	
	/**
	 * placeMirrors
	 */
	public void placeMirrors()
	{
		//center coordinates
		rectMode(CENTER);
		stroke(0, 0, 255);
		strokeWeight(1);
		fill(0, 0, 255, 50);
		//center coordinates for image
		imageMode(CENTER);
		
		//iterate through mirror_frames ArrayList containing mirror references
		for (int mir = 0; mir < 8; mir++)
		{	
			//select current mirror object
			Mirror currentMirror = mirrors.get(mir);
			
			//display image of mirror given current animation frame, 
			//getters retrieve X and Y position for each mirror
			image(mirror_frames[mirror_current_frame],
					currentMirror.getXPosition(), currentMirror.getYPosition());
		}
	}

	/**
	 * checkDirectCollision
	 * compares current coordinates of laser with coordinates of each mirror,
	 * if collision occurred, associate laser coordinates with mirror coordinates
	 * @param mirrors
	 * @param laser
	 * @return
	 */
	public boolean checkDirectCollision(ArrayList<Mirror> mirrors, Laser laser)
	{
		//retrieve laser x position, store in temporary variable
		int lX = laser.getXPosition();
		//retrieve laser y position, store in temporary variable
		int lY = laser.getYPosition();
		//declare variables for mirror x and y positions
		int mX;
		int mY;
		
		//iterates through ArrayList containing mirrors
		for (int element = 0; element < mirrors.size(); element++)
		{
			//store current mirror XPosition in mX
			mX = mirrors.get(element).getXPosition();
			//store current mirror YPosition in mY
			mY = mirrors.get(element).getYPosition();

			//if laserX and laserY equal to mirrorX and mirrorY
			if (lX == mX && lY == mY)
			{
				//associate laser coordinates with mirror coordinates for additional comparison
				laser.setCurrentMirrorCenter(mX, mY);
				//return true since collision occurred
				return true;
			}
		}
		//return false if no collision occurred  
		return false;
	}
	
	/**
	 * checkDirectCollision
	 * compares coordinates of laser and point marker
	 * @param points
	 * @param laser
	 * @return
	 */
	public boolean checkDirectCollision(Points points, Laser laser)
	{
		//store laser X position
		int lX = laser.getXPosition();
		//store laser Y position
		int lY = laser.getYPosition();
		//retrieve X position of point marker
		int pX = points.getXPosition();
		//retrieve Y position of point marker
		int pY = points.getYPosition();
		
		//if laser and points coordinates approximate each other, update point marker
		if (Math.abs(lX - pX) < 25 && Math.abs(lY - pY) < 25)
		{
			//fill(136, 136, 250, 5);
			//rect(pX, pY, 30, 30);
			//136, 136, 250
			
			//set points marker to collected
			points.isCollected(true);
			//add to total points
			totalPoints += pointIncrement;
			//add to number of consecutive points
			consecutivePoints += pointIncrement;
			//increment number of point markers collected consecutively
			consecutivePointCollect += 1;
			//return true since collision occurred
			return true;
		}
		//return false if no collision occurred
		return false;
	}
	
	/**
	 * checkObstacleDirectCollision
	 * UNUSED, when complete, would compare coordinates of laser with obstacle objects
	 * @param obstacle
	 * @param laser
	 * @return
	 */
	public boolean checkObstacleDirectCollision(ArrayList<Map_Space> obstacle, Laser laser)
	{
		return false;
	}

	/**
	 * determineReflectionDirection
	 * If checkDirectCollision (between laser and mirror) returns true,
	 * determine new laser movement/direction
	 */
	public void determineReflectionDirection()
	{
		//store current laser movement direction in variable, incidentDirection
		int incidentDirection = laser.getLaserMovementDirectionName();
		
		/*
		 * test each direction/mirror combination
		 * FORMAT:
		 * 
		 * determine initial laser movement direction,
		 * compare orientation of mirror,
		 * setLaserMovementDirectionName(x, y) setter updates direction name (1 for right/up, -1 for left, down, 0 for no movement)
		 * moveLaser setter(x, y) alters movement direction 
		 * setDirectionValues(0, -1) updates internal laser values
		 * 
		 */
		
		//if laser moves upward
		if (incidentDirection == Laser.U)
		{
			// -, \, |, /
			
			//if mirror (-)
			if (Mirror.getDirectionAlign() == '-')
			{
				// reflect downwards
				laser.setLaserMovementDirectionName(0, -1);
				laser.moveLaser(0, -1);
				laser.setDirectionValues(0, -1);
				
			}
			//if mirror (\)
			else if (Mirror.getDirectionAlign() == '\\')
			{
				// reflect leftwards
				laser.setLaserMovementDirectionName(-1, 0);
				laser.moveLaser(-1, 0);
				laser.setDirectionValues(-1, 0);
			}
			//if mirror (|)
			else if (Mirror.getDirectionAlign() == '|')
			{
				// hits non-reflective side of mirror, laser absorbed
				laser.moveLaser(0, 0);
				//System.out.println("GAME OVER");
				
				//reset laser coordinates
				laserReset(laser);
			}
			//if mirror (/)
			else if (Mirror.getDirectionAlign() == '/')
			{
				// reflect rightwards
				laser.setLaserMovementDirectionName(1, 0);
				laser.moveLaser(1, 0);
				laser.setDirectionValues(1, 0);
			}
		}
		//if laser moves downwards
		else if (incidentDirection == Laser.D)
		{
			if (Mirror.getDirectionAlign() == '-')
			{
				// reflect upwards
				laser.setLaserMovementDirectionName(0, 1);
				laser.moveLaser(0, 1);
				laser.setDirectionValues(0, 1);
			}
			else if (Mirror.getDirectionAlign() == '/')
			{
				// reflect leftwards
				laser.setLaserMovementDirectionName(-1, 0);
				laser.moveLaser(-1, 0);
				laser.setDirectionValues(-1, 0);
			} 
			else if (Mirror.getDirectionAlign() == '|')
			{
				// hits non-reflective side of mirror, absorbed
				laser.moveLaser(0, 0);
				//System.out.println("GAME OVER");

				//reset laser coordinates
				laserReset(laser);
			}
			else if (Mirror.getDirectionAlign() == '\\')
			{
				// reflect rightwards
				laser.setLaserMovementDirectionName(1, 0);
				laser.moveLaser(1, 0);
				laser.setDirectionValues(1, 0);
			}
		}
		//if laser moves leftwards
		else if (incidentDirection == Laser.L)
		{
			if (Mirror.getDirectionAlign() == '/')
			{
				// reflect downwards
				laser.setLaserMovementDirectionName(0, -1);
				laser.moveLaser(0, -1);
				laser.setDirectionValues(0, -1);
			}
			else if (Mirror.getDirectionAlign() == '\\')
			{
				// reflect upwards
				laser.setLaserMovementDirectionName(0, 1);
				laser.moveLaser(0, 1);
				laser.setDirectionValues(0, 1);
			}
			else if (Mirror.getDirectionAlign() == '-')
			{
				// hits non-reflective side of mirror, absorbed
				laser.moveLaser(0, 0);
				//System.out.println("GAME OVER");

				//reset laser coordinates
				laserReset(laser);
			}
			else if (Mirror.getDirectionAlign() == '|')
			{
				// reflect rightwards
				laser.setLaserMovementDirectionName(1, 0);
				laser.moveLaser(1, 0);
				laser.setDirectionValues(1, 0);
			}
		}
		//if laser moves rightwards
		else if (incidentDirection == Laser.R)
		{
			if (Mirror.getDirectionAlign() == '\\')
			{
				// reflect downwards
				laser.setLaserMovementDirectionName(0, -1);
				laser.moveLaser(0, -1);
				laser.setDirectionValues(0, -1);
			}
			else if (Mirror.getDirectionAlign() == '/')
			{
				// reflect upwards
				laser.setLaserMovementDirectionName(0, 1);
				laser.moveLaser(0, 1);
				laser.setDirectionValues(0, 1);
			}
			else if (Mirror.getDirectionAlign() == '-')
			{
				// hits non-reflective side of mirror, absorbed
				laser.moveLaser(0, 0);
				//System.out.println("GAME OVER");

				//reset laser coordinates
				laserReset(laser);
			}
			else if (Mirror.getDirectionAlign() == '|')
			{
				// reflect leftwards
				laser.setLaserMovementDirectionName(-1, 0);
				laser.moveLaser(-1, 0);
				laser.setDirectionValues(-1, 0);
			}
		}
	}
	
	/**
	 * laserReset
	 * resets laser coordinates if laser moves out-of-bounds or hits non-reflective side of mirror
	 * resets point multipliers
	 * @param laser
	 */
	public void laserReset(Laser laser)
	{
		//draw disappearance frame
		fill(0, 0, 0, 5);
		rect(laser.getXPosition(), laser.getYPosition(), 15, 15);
		
		//specifyCoordinates(x, y) resets laser coordinates 
		this.laser.specifyCoordinates(500, 1000);
		//setLaserMovementDirectionName(x, y) resets laser direction name to 'U'
		this.laser.setLaserMovementDirectionName(0, 1);
		//setDirectionValues(x, y) resets laser movement upwards
		this.laser.setDirectionValues(0, 1);
		//deduct points only if totalPoints greater than 0
		if (totalPoints + pointDecrement < 0)
		{
			totalPoints = 0;
		}
		else
		{
			totalPoints += pointDecrement;
		}
		
		//reset pointMultiplier to default
		pointMultiplier = 1;
		//reset pointIncrement to default
		pointIncrement = 500;
		//reset consecutive points total and consecutive points collected to 0
		consecutivePoints = 0;
		consecutivePointCollect = 0;
	}
	
	/**
	 * keyControls
	 * reads player keyboard input and interprets commands
	 */
	public void keyControls()
	{
		//NOTE: not short-circuiting current frame check seems to improve consistency of controls
		
		//if current mirror frame different, controls: (exclusive up/down) and neither left nor right, (-)
		if(mirror_current_frame != 0 & (upHeld ^ downHeld) && !(leftHeld || rightHeld))
		{
			mirror_current_frame = 0;
			Mirror.setDirectionAlign('-');
			//System.out.println(mirror_current_frame);
		}
		
		//if current mirror frame different, controls: (exclusive left/right) and neither up nor left, (|)
		if(mirror_current_frame != 3 & (leftHeld ^ rightHeld) && !(upHeld || downHeld))
		{
			mirror_current_frame = 3;
			Mirror.setDirectionAlign('|');
			//System.out.println(mirror_current_frame);
		}

		//if current mirror frame different, controls: (up and right) or (down and left), (\)
		if(mirror_current_frame != 1 & ((upHeld & rightHeld) || (downHeld & leftHeld)))
		{
			mirror_current_frame = 1;
			Mirror.setDirectionAlign('\\');
			//System.out.println(mirror_current_frame);
		}
		
		//if current mirror frame different, controls: (down and right) or (up and left), (/)
		if(mirror_current_frame != 2 & ((downHeld & rightHeld) || (upHeld & leftHeld)) )
		{
			mirror_current_frame = 2;
			Mirror.setDirectionAlign('/');
			//System.out.println(mirror_current_frame);
		}
	}


	/**
	 * keyPressed
	 * overrides keyPressed function, sets boolean values to true for each key pressed
	 */
	public void keyPressed(KeyEvent e)
	{
		//if coded key pressed, set respective direction held variable to true
		if(key == CODED)
		{
			if(keyCode == UP)
			{
				upHeld = true;
			}
			else if(keyCode == DOWN)
			{
				downHeld = true;
			}
			else if(keyCode == LEFT)
			{
				leftHeld = true;
			}
			else if(keyCode == RIGHT)
			{
				rightHeld = true;
			}
		}
	}
	
	/**
	 * keyReleased
	 * overrides keyReleased function, sets boolean values to false for each key released
	 */
	public void keyReleased(KeyEvent e)
	{
		//if coded key pressed, set respective direction held variable to false
		if(key == CODED)
		{
			if(keyCode == UP)
			{
				upHeld = false;
			}
			else if(keyCode == DOWN)
			{
				downHeld = false;
			}
			else if(keyCode == LEFT)
			{
				leftHeld = false;
			}
			else if(keyCode == RIGHT)
			{
				rightHeld = false;
			}
		}
	}
	
	/**
	 * updateScores
	 * checks for new high score, updates high score when game ends
	 * @param previousScoresList
	 * @param previousTopScore
	 * @param presentGameScore
	 */
	public void updateScores(ArrayList<String> previousScoresList, String previousTopScore, int presentGameScore)
	{
		//if score in current game (at any point) exceeds previous top score, update high score file
		if((presentGameScore) > Integer.parseInt(previousTopScore))
		{
			//add new high score to position 0 of ArrayList of scores
			previousScoresList.add(0, Integer.toString(presentGameScore));
			//declare array of scores, size equal to size of ArrayList
			String[] newHighScores = new String[previousScoresList.size()];
			//set array equal to ArrayList(converted to array)
			newHighScores = previousScoresList.toArray(newHighScores);
			//save scores with Processing saveStrings method
			saveStrings("high_Score", newHighScores);
		}
	}

}
