/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.Dimension;

/**
 * 
 * @author ian
 *
 */
public class Line
{
	private final Vector start;
	private final Vector end;
	private final Integer startRef;
	private final Integer endRef;
	
	public Line(Vector start, Integer startRef, Vector end, Integer endRef)
	{
		this.start = start;
		this.end = end;
		this.startRef = startRef;
		this.endRef = endRef;
	}
	
	public Line(Vector start, Vector end)
	{
		this.start = start;
		this.end = end;
		this.startRef = -1;
		this.endRef = -1;
	}
	
	public Vector getTop()
	{
		if(start.getY() > end.getY())
		{
			return start;
		}
		return end;
	}
	
	public Vector getBottom()
	{
		if(end.getY() < start.getY())
		{
			return end;
		}
		return start;
	}
	
	public Integer getBottomReference()
	{
		if(getBottom().equals(end))
		{
			return endRef;
		}
		return startRef;
	}
	
	public Integer getTopReference()
	{
		if(getTop().equals(start)){
			return startRef;
		}
		return endRef;
	}
	
	public Vector getStart()
	{
		return start;
	}
	
	public Vector getEnd()
	{
		return end;
	}
	
	public Integer getStartReference()
	{
		return this.startRef;
	}
	
	public Integer getEndReference()
	{
		return this.endRef;
	}
	
	public Line flip()
	{
		return new Line(end, endRef, start, startRef);
	}
	
	public float inverseSlope()
	{
		return (end.getX() - start.getX()) / (end.getY() - start.getY());
	}
	
	public float slopeZOverY()
	{
		return (end.getZ() - start.getZ()) / (end.getY() - start.getY());
	}
	
	public float slopeZOverX()
	{
		return (end.getZ() - start.getZ()) / (end.getX() - start.getX());
	}
	
	public float slope()
	{
		return (end.getY() - start.getY()) /(end.getX() - start.getX());
	}
	
	public boolean isHorizontal()
	{
		return Math.abs(start.getY() - end.getY()) < 1e-5;
	}
	
	public boolean isWithin(Dimension dim)
	{
		return Math.max(start.getY(), end.getY()) > 0
		&& Math.min(start.getY(), end.getY()) < dim.height;
		//&& Math.max(start.getX(), end.getX()) > 0
		//&& Math.min(start.getX(), end.getX()) < dim.width;
	}
	
	@Override
	public String toString()
	{
		return "("+start.toString() +")->("+end.toString()+")";
	}
}
