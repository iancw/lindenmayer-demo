package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BufferedImageTexture {
	
	private final BufferedImage image;
	
	public BufferedImageTexture(BufferedImage img)
	{
		this.image = img;
	}
	
	/**
	 * 
	 * @param coord texture coordinate in [0,1] space
	 * @return corresponding color from the image
	 */
	public Color getColor(Coord coord)
	{
		int x = new Double((coord.getU() * image.getWidth())).intValue();
		int y = new Double((coord.getV() * image.getHeight())).intValue();
		if(x == image.getWidth()){ x -=1;}
		if(y == image.getHeight()){ y -= 1;}
		int rgb = image.getRGB(x, y);
		return new Color(rgb);
	}
	
}
