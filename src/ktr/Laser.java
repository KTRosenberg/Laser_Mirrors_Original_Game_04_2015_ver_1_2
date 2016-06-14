package ktr;

/**
 * Laser
 * 
 * the object that the player guides by reflecting it in particular directions
 * with the mirrors
 * 
 */
public class Laser
{
	//pi constant
	public static final double PI = Math.PI;
	//UP
	public static final int U = 'u';
	//DOWN
	public static final int D = 'd';
	//LEFT
	public static final int L = 'l';
	//RIGHT
	public static final int R = 'r';

	//pixels per movement
	private int speed;
	//movement in x (-1 left, 0 none, 1 right)
	private int xDirection;
	//movement in y (-1 down, 0 none, 1 up)
	private int yDirection;
	//x coordinate
	private int xPosition;
	//y coordinate
	private int yPosition;
	//direction name (U, D, L, R)
	private int movementDirectionName;
	//array stores coordinates of associated mirror (for collision detection)
	private int[] currentMirrorCenter = new int[2];
		//unused, could store reference to mirror
		//Mirror currentMirror;

	/**
	 * Laser constructor, default
	 */
	Laser()
	{
		//20 pixels per movement
		speed = 20;
		//x = 0 y = 1 movement, positioned at center bottom, moves upwards
		xDirection = 0;
		yDirection = 1;
		xPosition = 500;
		yPosition = 1000;
		movementDirectionName = U;
	}
	
	/**
	 * Laser constructor, overloaded
	 * data fields can be specified, not all speeds function in current build
	 * @param speed
	 * @param xDirection
	 * @param yDirection
	 * @param xPosition
	 * @param yPosition
	 * @param movementDirectionName
	 */
	Laser(int speed, int xDirection, int yDirection, int xPosition, int yPosition, int movementDirectionName)
	{
		this.speed = speed;
		this.xDirection = xDirection;
		this.yDirection = yDirection;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.movementDirectionName = movementDirectionName;
	}

	/**
	 * getXposition
	 * retrieve xPosition
	 * @return
	 */
	public int getXPosition()
	{
		return xPosition;
	}

	/**
	 * getYposition
	 * retrieve yPosition
	 * @return
	 */
	public int getYPosition()
	{
		return yPosition;
	}
	
	/**
	 * setNewCoordinates
	 * adjusts current coordinates by given x and y coordinate values
	 * @param xPosition
	 * @param yPosition
	 */
	public void setNewCoordinates(int xPosition, int yPosition)
	{
		this.xPosition += xPosition;
		this.yPosition += yPosition;
	}

	/**
	 * specifyCoordinates
	 * given x and y coordinates, immediately places laser at coordinates
	 * @param xPosition
	 * @param yPosition
	 */
	public void specifyCoordinates(int xPosition, int yPosition)
	{
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	/**
	 * setCurrentMirrorCenter
	 * associates laser with specific mirror, 
	 * assigns coordinates of mirror to currentMirrorCenter array
	 * @param xPosition
	 * @param yPosition
	 */
	public void setCurrentMirrorCenter(int xPosition, int yPosition)
	{
		currentMirrorCenter[0] = xPosition;
		currentMirrorCenter[1] = yPosition;
	}

	/**
	 * getSpeed
	 * retrieves speed (pixels per movement)
	 * @return
	 */
	public int getSpeed()
	{
		return speed;
	}

	/**
	 * setSpeed
	 * sets speed to specific value (use of method not yet recommended)
	 * @param speed
	 */
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	/**
	 * getXDirectionValue
	 * retrieves xDirection
	 * @return
	 */
	public int getXDirectionValue()
	{
		return xDirection;
	}

	/**
	 * getYDirectionValue
	 * retrieves yDirection
	 * @return
	 */
	public int getYDirectionValue()
	{
		return yDirection;
	}

	/**
	 * setDirectionValues
	 * sets x and y values for movement direction
	 * @param Dx
	 * @param Dy
	 */
	public void setDirectionValues(int Dx, int Dy)
	{
		xDirection = Dx;
		yDirection = Dy;
	}

	/**
	 * getLaserMovementDirectionName
	 * retrieves movementDirectionName U, D, L, R
	 * @return
	 */
	public int getLaserMovementDirectionName()
	{
		return this.movementDirectionName;
	}

	/**
	 * setLaserMovementDirectionName
	 * given (x, y), sets movement direction name
	 * @param dX
	 * @param dY
	 */
	public void setLaserMovementDirectionName(int dX, int dY)
	{
		//movement in x (-1 left, 0 none, 1 right)
		//movement in y (-1 down, 0 none, 1 up)
		if (dX > 0)
		{
			this.movementDirectionName = R;
		}
		else if (dX < 0)
		{
			this.movementDirectionName = L;
		}
		else if (dY > 0)
		{
			this.movementDirectionName = U;
		}
		else if (dY < 0)
		{
			this.movementDirectionName = D;
		}
	}

	/**
	 * moveLaser
	 * controls movement of laser, adjusts coordinates
	 * @param directionX
	 * @param directionY
	 */
	public void moveLaser(int directionX, int directionY)
	{
		//given x and y (Cartesian), setNewCoordinates((pixels/movement)*(direction in x), -(pixels/movement)*(direction in y)), y multiplied by -1 since coordinate system associates movement downward with positive values 
		this.setNewCoordinates(speed * directionX, -speed * directionY);
		//adjust movement direction name 
		this.setLaserMovementDirectionName(directionX, directionY);
	}
}
