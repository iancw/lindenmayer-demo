package edu.gwu.graphics2;

import java.awt.Color;


public class Entry implements Comparable<Entry>
{
	final float yMax; 
	float x; //x coord of bottom end point
	float xMax; //to keep us from stepping over out limit
	final float slope; //delta x / delta y
	final int y;
	float z;
	float zDelta;
	Vector normal;
	final Vector normalDelta;
	private final Line line;
	Vector color;
	Vector colorDelta;
	Coord texture;
	Coord textureDelta;
	Coord endTexture;
	
	public Entry(Line line, 
			Vector startNormal, Vector endNormal, 
			Color startColor, Color endColor,
			Coord startTexture, Coord endTexture)
	{
		this.line = line;
		this.normal = startNormal;
		
		this.texture = startTexture;
		this.endTexture = endTexture;
		this.color= Vector.fromColor(startColor);
		float deltaY = line.getEnd().getY() - line.getStart().getY();
		this.colorDelta = Vector.fromColor(endColor).subtract(color).scale(1/deltaY);
		this.normalDelta= endNormal.subtract(startNormal).scale(1/deltaY);
		this.textureDelta = startTexture.computeDelta(endTexture, deltaY);
		
		x = line.getStart().getX();
		xMax = line.getEnd().getX();
		z = line.getStart().getZ();
		zDelta = (line.getEnd().getZ() - line.getStart().getZ())/deltaY;
		//because we've ignored horizontal lines, the slope should not be infinite
		slope = line.inverseSlope();
		yMax = line.getEnd().getY();
		int _y = (int)Math.floor(line.getStart().getY());
		//These checks clip x and y to view bounds
		if(_y < 0)
		{
			_y = 0;
		}
		y = _y;
		if(y > yMax)
		{
			throw new IllegalArgumentException("Line needs to be flipped");
		}
	}
	
	float getZ(int y)
	{
//		float z1 = line.getEnd().getZ();
//		float z2 = line.getStart().getZ();
//		float y1 = line.getEnd().getY();
//		float y2 = line.getStart().getY();
//		return z1 - (z1-z2) * (y1-y)/(y1-y2);
		return z;
	}
	
	void incrementX()
	{
		x += slope;
		if(x < xMax && slope < 0)
		{
			x = xMax;
		}
		if(x > xMax && slope > 0)
		{
			x = xMax;
		}
	}
	
	void incrementTextureCoordinate()
	{
		this.texture = this.texture.add(textureDelta);
		double adjU = texture.getU();
		double adjV = texture.getV();
		//test that incrementing u and v didn't go over the bounds
		//So right U and right V aren't necessarily greater
		//than left U and left V.  It all depends on whether 
		//the deltas are negative or not
		//If delta U is decreasing, then rightU should
		//be smaller than left U				
		if(textureDelta.getU() < 0 && texture.getU() < endTexture.getU()
				|| textureDelta.getU() > 0 && texture.getU() > endTexture.getU())
		{
			adjU = endTexture.getU();
		}
		if(textureDelta.getV() < 0 && texture.getV() < endTexture.getV()
				|| textureDelta.getV() > 0 && texture.getV() > endTexture.getV())
		{
			adjV = endTexture.getV();
		}
		texture.set(adjU, adjV);
	}
	
	void incrementNormal()
	{
		this.normal = this.normal.add(normalDelta);
	}
	
	void incrementColor()
	{
		this.color = this.color.add(colorDelta);
	}
	
	void incrementZ()
	{
		//z += deltaZ;
		z += zDelta;
	}
	
	
	@Override
	public String toString()
	{
		return "1/m: "+slope+", y_max: "+yMax+", x: "+x;
	}
	
	@Override
	public int compareTo(Entry o) {
		if( x < o.x) return -1;
		if(x > o.x) return 1;
		return 0;
	}
}