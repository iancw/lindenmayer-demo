/**
 * 
 */
package edu.gwu.graphics2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that stores references to points as descriptions of a polygon. It
 * relies on an external source of the point array, which the Model class
 * typically provides.  Separating the two is convenient because the points
 * can be transformed, polygon index references remain consistent.
 * 
 * The trickiest part of this class is the fact that it stores indexes exactly
 * as they are in .d files, which means they begin at 1.  Every time an index
 * needs to be fetch from a point array, it has to be converted to a base-0
 * index.
 * 
 * The two major responsibilities of this class are to compute its normal 
 * (in #computeNormal), and to return a list of lines for rendering 
 * (in {@link #toLines(List)}).
 * 
 * @author ian
 *
 */
public class RefPolygon
{
	private final List<Integer> pointIndex;
	private Vector worldNormal;
	
	public RefPolygon(List<Integer> refs)
	{
		this.pointIndex = new ArrayList<Integer>(refs.size());
		this.pointIndex.addAll(refs);
	}
	
	public void setWorldNormal(Vector nml)
	{
		this.worldNormal = nml;
	}
	
	public Vector getPoint(int idx, List<Vector> points)
	{
		return points.get(pointIndex.get(idx)-1);
	}
	
	public boolean contains(Integer reference)
	{
		return pointIndex.contains(reference);
	}
	
	public List<Integer> getIndices()
	{
		return Collections.unmodifiableList(pointIndex);
	}
	
	public int getNumIndicies()
	{
		return pointIndex.size();
	}
	
	public Vector getCenter(List<Vector> points)
	{
		float sumX=0;
		float sumY=0;
		float sumZ=0;
		for(Integer i : pointIndex)
		{
			Vector p = points.get(i-1);
			sumX+=p.getX();
			sumY+=p.getY();
			sumZ+=p.getZ();
		}
		return new Vector(sumX/pointIndex.size(), sumY/pointIndex.size(), sumZ/pointIndex.size());
	}
	
	public Vector getWorldSpaceNormal()
	{
		return worldNormal;
	}
	
	public Vector computeNormal(List<Vector> points, boolean clockwise)
	{
		if(pointIndex.size() >= 3)
		{
			Vector p0 = points.get(pointIndex.get(0)-1);
			Vector p1 = points.get(pointIndex.get(1)-1);
			Vector p2 = points.get(pointIndex.get(2)-1);
			
			Vector v1 = p0.subtract(p1); // p1 -> p0
			Vector v2 = p2.subtract(p1); // p1 -> p2
			if(clockwise)
			{
				return v1.cross(v2).normalize();
			}else
			{
				return v2.cross(v1).normalize();
			}
		}else
		{
			return new Vector(0, 0, 0);
		}
	}
	
	public List<Line> toLines(List<Vector> points)
	{
		List<Line> lines = new ArrayList<Line>();
		Integer prev = null;
		for(Integer idx : pointIndex)
		{
			//Skip the first one
			if(prev != null)
			{
				//Subtract 1 because .d indices start at 1
				lines.add(new Line(points.get(prev-1), prev, points.get(idx-1), idx));				
			}
			prev = idx;
		}
		//Close the polygon
		if(pointIndex.size() > 0)
		{
			lines.add(new Line(
					points.get(prev-1), 
					prev, points.get(pointIndex.get(0)-1), pointIndex.get(0)));
		}
		return lines;
	}
	
	@Override
	public String toString()
	{
		return "{"+pointIndex.toString()+"}";
	}
}
