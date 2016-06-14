package ktr;

//import Random to create random number generators
import java.util.Random;

/**
 * Points
 * 
 * If the laser object collides with the points objects, points are awarded to the player
 * @author Karl Toby Rosenberg
 *
 */
public class Points extends Laser
{
	//currently unused
	/*
	//x coordinate
	private int xPosition;
	//y coordinate
	private int yPosition;
	*/
	
	//array stores x and y coordinates for each space at which a point marker can spawn
	private int [][] pointsLocations  = {{300, 100}, {700, 100}, 
									{100, 300}, {500, 300}, {900, 300}, 
										 {300, 500}, {700, 500}, 
									   {100, 700}, {900, 700}, 
									   {300, 900 }, {500, 900}, {700, 900}};

	/**
	 * Points constructor, default
	 */
	Points()
	{
		//points marker out-of-bounds by default
		specifyCoordinates(1200, 0);
	}
	
	/**
	 * Points constructor, overloaded
	 * sets points marker at specified coordinates
	 * @param x
	 * @param y
	 */
	Points(int x, int y)
	{
		//inherits specifyCoordinates method from Laser
		specifyCoordinates(x, y);
	}
	
	/**
	 * isCollected
	 * checks whether laser has collided with the point marker, if so, calls reSpawn
	 * @param isCollected
	 */
	public void isCollected(boolean isCollected)
	{
		if(isCollected)
		{
			//reSpawn point marker at another possible location
			reSpawn();
		}
	}
	
	/**
	 * spawn
	 * calls reSpawn method (identical except for name, only called before game)
	 */
	public void spawn()
	{
		reSpawn();
	}
	
	/**
	 * reSpawn
	 * re-spawns point marker at possible location (using random number generation)
	 */
	private void reSpawn()
	{
		//create new Random object
		Random r = new Random();
		//store random integer [0, 12) corresponding to indices of pointsLocations array
		int choice = r.nextInt(12);
		//position point marker at new coordinates
		specifyCoordinates(pointsLocations[choice][0], pointsLocations[choice][1]);
	}
}
