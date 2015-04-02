package edu.gwu.graphics2;

import java.util.List;
import java.util.logging.Logger;

/**
 * Contains a RefPolygon and the points required to make it act like
 * a full polygon
 * @author ian
 *
 */
public class Polygon 
{
	private final RefPolygon refs;
	private final List<Vector> points;
	private final boolean clockwise;
	private final PolygonModel model;
	
	public Polygon(RefPolygon refs, PolygonModel mod)
	{
		this.refs = refs;
		this.points = mod.getPoints();
		this.clockwise = mod.isClockwise();
		this.model = mod;
	}
	
	public Material getMaterial()
	{
		return model.getMaterial();
	}
	
	public Vector getVertexNormal(Integer vertexReference)
	{
		if(vertexReference < 0)
		{
			//not found
			Logger.getLogger(getClass().getName()).warning("Could not find the vertex "+vertexReference+" for normal comutation");
		}
		List<Polygon> adjacent = model.getAdjacentPolygons(vertexReference);
		//Average all normals
		Vector sum = new Vector(0,0,0,0);
		for(Polygon adj : adjacent)
		{
			Vector normal = adj.getWorldSpaceNormal(); 
			sum = sum.add(normal);
		}
		return sum.scale(1/sum.magnitude());
	}
	
	public Coord getTextureCoordinate(Integer vertexReference)
	{
		if(vertexReference < 0)
		{
			//not found
			Logger.getLogger(getClass().getName()).warning("Could not find the vertex "+vertexReference+" for normal comutation");
		}
		return model.getTextureCoordinate(vertexReference);
	}
	
	public List<Line> toLines()
	{
		return refs.toLines(points);
	}
	
	public Vector getNormal()
	{
		return refs.computeNormal(points, clockwise);
	}
	
	
	public Vector getWorldSpaceNormal()
	{
		return refs.getWorldSpaceNormal();
	}

}
