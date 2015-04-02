package edu.gwu.graphics2;

import java.awt.Color;

public class Light {
	private final Vector position;
	private final Color rgb;
	
	public Light()
	{
		this(new Vector(0,0,0,0), Color.white);
	}
	
	public Light(Vector position, Color rgb)
	{
		this.position = position;
		this.rgb = rgb;
	}
	
	public Vector getColorVector()
	{
		return new Vector(rgb.getRed()/255f, rgb.getGreen()/255f, rgb.getBlue()/255f);
	}
	
	public Color getColor()
	{
		return rgb;
	}
	
	public Vector getPosition()
	{
		return position;
	}
	
}
