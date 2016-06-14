package ktr;

/**
 * Map_Space
 * 
 * represents a basic map space on the grid
 */
public class Map_Space
{
	//x coordinate
	private int xPosition;
	//y coordinate
	private int yPosition;
	
	/**
	 * Map_Space constructor, default
	 * sets x and y positions to 0
	 */
	Map_Space()
	{
		xPosition = 0;
		yPosition = 0;
	}
	
	/**
	 * Map_Space constructor, overloaded
	 * sets x and y positions to specified values
	 * @param x
	 * @param y
	 */
	Map_Space(int x, int y)
	{
		xPosition = x;
		yPosition = y;
	}
	
	/**
	 * getXPosition
	 * retrieves xPosition
	 * @return
	 */
	public int getXPosition()
	{
		return xPosition;
	}
	
	/**
	 * getYPosition
	 * retrieves yPosition
	 * @return
	 */
	public int getYPosition()
	{
		return yPosition;
	}
}
