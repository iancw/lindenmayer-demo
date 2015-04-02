package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

public class RasterData implements RasterModel
{
	private final Color[][] colors;
	
	public RasterData(Dimension dim, Color def)
	{
		this(dim.width, dim.height, def);
	}
	
	public RasterData(int width, int height, Color def)
	{
		colors = new Color[width][height];
		for(int i=0; i<width; i++)
		{
			Arrays.fill(colors[i], def);
		}
	}
	
	public void setPixel(int x, int y, Color val)
	{
		if(x<0 || x >= colors.length || y < 0 || y >= colors[x].length)
		{
			return;
		}
		colors[x][y] = val;
	}

	@Override
	public Color getColor(int x, int y) {
		return colors[x][y];
	}

}
