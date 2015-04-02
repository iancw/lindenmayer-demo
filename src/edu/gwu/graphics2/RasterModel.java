package edu.gwu.graphics2;

import java.awt.Color;

public interface RasterModel 
{
	/**
	 * Pixel in screen space 
	 * @param x x coordinate (horizontal axis)
	 * @param y y coordinate (vertical axis)
	 * @return the color to draw on the image at the requested pixel
	 */
	public Color getColor(int x, int y);

}
