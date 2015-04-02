package edu.gwu.graphics2;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Handles the z buffer, and scaling z values into the appropriate range 
 * 
 * @author ian
 *
 */
public class ZBuffer {
	private final Logger logger = Logger.getLogger(ZBuffer.class.getName());
	private final Dimension dimension;
	private final float zMin;
	private final float zMax;
	private final float zRange;
	
	private final float buffer[][];
	private final Map<Integer, Integer> histogram;
	
	public ZBuffer(Dimension dim, float min_z, float max_z)
	{
		this.dimension = dim;
		this.zMin = min_z;
		this.zMax = max_z;
		this.zRange = max_z - min_z;
		buffer = new float[dimension.width][dimension.height];
		for(int i=0; i<dimension.width; i++)
		{
			Arrays.fill(buffer[i], Float.MAX_VALUE);
		}
		histogram = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Scales a z coordinate equally between the min and max values
	 * @param z z
	 * @return integer bin that z falls into
	 */
	public int scale(float z)
	{
		if(z < zMin)
		{
			logger.info("Capping z ("+z+") to zMin ("+zMin+")");
			z = zMin;
		}
		if(z > zMax)
		{
			logger.info("Capping z ("+z+") to zMax ("+zMax+")");
			z = zMax;
		}
		float test = (z - zMin)/zRange;
		if(test < 0 || test > 1)
		{
			logger.warning("Expected test to be [0,1], instead its "+test+" (for z="+z+" [zMin,zMax]=["+zMin+","+zMax+"]");
		}
		return (int)Math.round(test * Integer.MAX_VALUE);
	}
	
	/**
	 * 
	 * @param z z value to test
	 * @param x coordinate on the x axis
	 * @param y coordinate on the y axis
	 * @return true if z is less than the current buffer contents at the given coordinates
	 */
	public boolean test(float z, int x, int y)
	{
		if(x < 0 || x >= dimension.width || y < 0 || y >= dimension.height)
		{
			return false;
		}
		if(z < 0 || z > 1)
		{
			return false; //z is outside the clipping planes
		}
		float scaled = z;//scale(z);
		if(scaled < buffer[x][y])
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to set the z buffer value to the specified z.  If z is
	 * less than the current contents, the buffer will be updated
	 * @param z potential new z value
	 * @param x coordinate 
	 * @param y coordinate
	 * @return returns true if x, y is valid and z is less than the current contents,
	 * false otherwise.
	 */
	public boolean set(float z, int x, int y)
	{
		if(test(z, x, y))
		{
			float scaled = z;//scale(z);
			buffer[x][y] = scaled;
//			updateHisto(scaled);
			return true;
		}
		return false;
	}
	
	private void updateHisto(int z)
	{
		Integer value = histogram.get(z);
		if(value == null)
		{
			histogram.put(z, 1);
		}else
		{
			histogram.put(z, value + 1);
		}
	}
	
	public void printHistogram()
	{
		for(Integer i : histogram.keySet())
		{
			Integer value = histogram.get(i);
			if(value > 10)
				System.out.println("For z="+i+": "+value+" pixels");
		}
	}
}
