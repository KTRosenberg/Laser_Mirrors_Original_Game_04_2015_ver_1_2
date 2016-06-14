package ktr;

//class currently unused, would be used for moving obstacles and enemies
public class Obstacle extends Laser
{
	//currently unused
	//x coordinate
	private int xPosition;
	//y coordinate
	private int yPosition;
	
	/**
	 * Obstacle constructor, default
	 * PLACEHOLDER, obstacles would likely include temporary laser barriers
	 * originating from the center (unreachable) spaces, would add additional challenge
	 * other obstacles could block mirrors or temporarily reverse controls
	 */
	Obstacle()
	{
		xPosition = 300;
		yPosition = 300;
	}
	
	/**
	 * Obstacle constructor, overloaded
	 * sets obstacle at specific coordinates
	 * @param xPosition
	 * @param yPosition
	 */
	Obstacle(int xPosition, int yPosition)
	{
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
}
