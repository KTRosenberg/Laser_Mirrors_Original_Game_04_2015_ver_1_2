package ktr;

/**
 * 
 * Mirror
 * 
 * the mirror object whose angle the player controls
 * to change the direction of the laser object
 * 
 * @author Karl Toby Rosenberg
 *
 */
public class Mirror extends Map_Space
{
	//currently unused
	/*
	//x coordinate
	private int xPosition;
	//y coordinate
	private int yPosition;
	*/
	
	//direction align, affects all mirrors created from class
	private static int directionAlign = '-';
	//possible alignments:
	// \, /, |
	
	//unused, non image-graphic information 
	//public final static float HEIGHT = 12.5F;
	//public final static float WIDTH = 50;
	
	/**
	 * Mirror constructor, default
	 * empty, directionAlign remains '-' by default
	 */
	Mirror()
	{
	}
	
	/**
	 * Mirror constructor, overloaded 1
	 * input center x and y coordinates, directionAlign remains '-' by default
	 * @param x
	 * @param y
	 */
	Mirror(int x, int y)
	{
		/* calls Map_Space (super constructor) to set x and y coordinates
		 * equivalent to:
		 * xPosition = x;
		 * yPosition = y;
		 */
		super(x, y);
		
		directionAlign = '-';
		//for testing System.out.println(xPosition + "\t" + yPosition);
	}
	
	/**
	 * Mirror constructor, overloaded 2
	 * input center x and y coordinates, set directionAlign
	 * @param x
	 * @param y
	 * @param directionAlign
	 */
	Mirror(int x, int y, int directionAlign)
	{
		/* calls Map_Space (super constructor) to set x and y coordinates
		 * equivalent to:
		 * xPosition = x;
		 * yPosition = y;
		 */
		super(x, y);
		//set directionAlign
		Mirror.directionAlign = directionAlign;
	}
	
	/**
	 * getDirectionAlign
	 * retrieve current directionAlign value
	 * @return
	 */
	public static int getDirectionAlign()
	{
		return directionAlign;
	}
	
	/**
	 * setDirectionAlign
	 * adjusts directionAlign
	 * @param Align
	 */
	public static void setDirectionAlign(int Align)
	{
		directionAlign = Align;
	}
	
	/**
	 * getCurrentMirror
	 * returns current Mirror object
	 * @return
	 */
	public Mirror getCurrentMirror()
	{
		return this;
	}
}
