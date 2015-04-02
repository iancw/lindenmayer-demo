/**
 * 
 */
package edu.gwu.graphics2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents a collection of points, and the ordered groups of 
 * indexes that define polygons.  In addition to these two primary data structures,
 * the class also maintains a list of polygons that have passed the culling test,
 * and a list of lines that can be displayed for help purposes (coordinate axes).
 * 
 * The class handles culling and model transformation in cullBackfacing... and 
 * multiplyAll methods.  Both of the cullBackfacing... methods will fill the
 * notCulled list with the RefPolygons that have passed the culling test.  These
 * polygons are then used to build new Models in multiplyAll and to return Lines
 * in toLines.
 * 
 * The multiplyAll method will transform all points according to the supplied
 * Matrix, will also transform axes, and will return a new Model with the 
 * transformed points, containing only the non-culled polygons.
 * 
 * @author ian
 *
 */
public class PolygonModel implements Model
{
	private List<Vector> points;
	private List<Coord> textureCoords;
	private List<RefPolygon> polygons;
	private List<Line> axes;
	private List<Line> normals;
	private Line lightVector;
	private String name;
	private boolean clockwise=true;
	private Material material;
	
	public PolygonModel(final String name, final List<Vector> pts, final List<RefPolygon> plys, List<Coord> textCoord, Material mat)
	{
		this(name, pts, plys, new LinkedList<Line>(), textCoord, mat);
	}
	
	public PolygonModel(final String name, final List<Vector> pts, final List<RefPolygon> plys, List<Line> axes, List<Coord> textures, Material mat)
	{
		this(name, pts, plys, axes, new LinkedList<Line>(), mat,						
				new Line(new Vector(0,0,0,0), new Vector(0,0,0,0)), textures);
	}
	
	public PolygonModel(final String name, final List<Vector> pts, final List<RefPolygon> plys, List<Line> axes, List<Line> nrmls, Material mat, Line lightVector, List<Coord> textures)
	{
		this.name = name;
		this.points = new ArrayList<Vector>(pts.size());
		this.textureCoords = textures;
		this.polygons = new ArrayList<RefPolygon>(plys.size());
		this.axes = new ArrayList<Line>(3);
		this.normals = new LinkedList<Line>();
		this.lightVector = lightVector;
		this.textureCoords = textures;
		
		this.points.addAll(pts);
		this.polygons.addAll(plys);
		this.axes.addAll(axes);
		this.normals.addAll(nrmls);
		this.material = mat;
	}
	
	Vector computeCentroid()
	{
		Vector average = new Vector(0, 0, 0);
		for(Vector v : points)
		{
			average = average.add(v);
		}
		average.scale(1/points.size());
		return average;
	}
	
	/**
	 * Does two-step inverse mapping to correlate each vertex coordinate
	 * with a coordinate in texture space.  Those coordinates are then
	 * interpolated during scan conversion.
	 */
	public void buildTextureCoords()
	{
		//buildTexturesPlanar();
		buildTexturesCylindrical();
	}
	
	private void buildTexturesPlanar()
	{
		double ymax, ymin, zmax, zmin;
		ymax = Double.MIN_VALUE;
		ymin = Double.MAX_VALUE;
		zmax = Double.MIN_VALUE;
		zmin = Double.MAX_VALUE;
		
		for(Vector vertex : points)
		{
			if(vertex.getY() < ymin){ ymin = vertex.getY(); }
			if(vertex.getY() > ymax){ ymax = vertex.getY(); }
			if(vertex.getZ() > zmax){ zmax = vertex.getZ(); }
			if(vertex.getZ() < zmin){ zmin = vertex.getZ(); }
		}
		double yspan = ymax-ymin;
		double zspan = zmax-zmin;
		textureCoords.clear();
		for(Vector vertex : points)
		{
			double u = (vertex.getY()-ymin)/yspan;
			double v = (vertex.getZ() - zmin) / zspan;
			
			textureCoords.add(new Coord((float)u, (float)v));
		}
	}
	
	private void buildTexturesCylindrical()
	{
		//T(u, v) is in 0-1 space
		//First you map each T' coordinate into world/object space (O mapping)
		//Then you map each texture coordinate T(u,v) into an
		//intermediate space T' (S mapping)
		
		//For each point, compute the corresponding u,v
		//and store it in the textureCoords array
		Vector centroid = computeCentroid();
		Matrix origin = Matrix.fromTranslationVector(centroid.scale(-1));
		PolygonModel translated = this.multiplyAll(origin);
		
		double maxZ= Double.MIN_VALUE;
		double minZ = Double.MAX_VALUE;
		
		for(Vector v : translated.points)
		{
			//map to cylinder...
			//(u,v) = (theta / 2 pi, z)
			double theta = (Math.atan2(v.getY(), v.getX()) + Math.PI) / (Math.PI*2);
			double h = v.getZ();
			double u = (theta / Math.PI*2);
			if(u < 0 || u > 1)
			{
				System.out.println("U is out of range at "+u+"...");
			}
			if(h > maxZ)
			{
				maxZ = h;
			}
			if(h < minZ)
			{
				minZ = h;
			}
			textureCoords.add(new Coord((float)u, (float)h));
		}
		
		//Go back through and normalize all Z's to 0-1 range
		ArrayList<Coord> newCoords = new ArrayList<Coord>(textureCoords.size());
		for(Coord c : textureCoords)
		{
			double oldV = c.getV();
			double newV = (c.getV() - minZ) / (maxZ-minZ);
			if(newV < 0 || newV > 1)
			{
				System.out.println("V is out of bounds: "+newV);
			}
			newCoords.add(new Coord( c.getU(), (float)newV));
		}
		textureCoords = newCoords;		
	}
	
	public void setClockwise(boolean clockwise)
	{
		this.clockwise = clockwise;
	}
	
	public boolean isClockwise()
	{
		return clockwise;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMaterial(Material mat)
	{
		this.material = mat;
	}
	
	public float findMinZ()
	{
		float min = Float.MAX_VALUE;
		for(Vector point : points)
		{
			if(point.getZ() < min)
			{
				min = point.getZ();
			}
		}
		return min;
	}
	
	public float findMaxZ()
	{
		float max = Float.MIN_VALUE;
		for(Vector point : points)
		{
			if(point.getZ() > max)
			{
				max= point.getZ();
			}
		}
		return max;
	}
	
	
	/**
	 * Culls all back facing polygons based on the assumption that the perspective
	 * transformation has been applied, and all normals can be tested against
	 * the Z axis
	 */
	public PolygonModel cullBackfacingPerspective()
	{
		List<RefPolygon> notCulled = new ArrayList<RefPolygon>();
		for(RefPolygon poly : polygons)
		{
			Vector np = poly.computeNormal(points, clockwise);
			Vector np_reverse = poly.computeNormal(points, !clockwise);
			if(np.getZ() > 0)
			{
				notCulled.add(poly);
			}
		}
		return new PolygonModel(name, points, notCulled, axes, normals, material, lightVector, this.textureCoords);
	}
	
	/**
	 * Culls all back facing polygons based on the assumption that the view
	 * transformation has been applied.  In this case the camera has been 
	 * effectively transformed to the origin, so any point on the polygon
	 * can be used to define the line of sight vector. 
	 */
	public PolygonModel cullBackfacing(Vector camera)
	{
		List<RefPolygon> notCulled = new ArrayList<RefPolygon>();
		for(RefPolygon poly : polygons)
		{
			Vector np = poly.computeNormal(points, clockwise);
			Vector np_rev = poly.computeNormal(points, !clockwise);
			Vector mid = poly.getCenter(points); 
			Vector n = camera.subtract(mid); //from mid to camera
			float dotprod = np.dot(n);
			float dotprod_rev = np_rev.dot(n);
			if(dotprod > 0)
			{
					notCulled.add(poly);
			}
		}
		return new PolygonModel(name, points, notCulled, axes, normals, material, lightVector, this.textureCoords);
	}
	
	/**
	 * Multiplies all points in the Model by the supplied Matrix.  Also multiplies
	 * the axes.  Returns a new Model with the transformed data points.
	 * @param m
	 * @return
	 */
	public PolygonModel multiplyAll(Matrix m)
	{
		List<Vector> newPoints = new ArrayList<Vector>(points.size());
		
		for(int i=0; i<points.size(); i++)
		{
			Vector v = points.get(i);
			Vector newv = m.multiply(v);
			newPoints.add(i, newv.toCartesian());
		}
		
		PolygonModel newM = new PolygonModel(name, newPoints, Collections.unmodifiableList(polygons), this.textureCoords, this.material);
		if(axes.size() > 0)
		{
			for(Line axis : axes)
			{
				newM.axes.add(new Line(m.multiply(axis.getStart()).toCartesian(), m.multiply(axis.getEnd()).toCartesian()));
			}
		}
		if(normals.size() > 0)
		{
			for(Line normal : normals)
			{
				newM.normals.add(m.multiply(normal));
			}
		}
		newM.lightVector = m.multiply(lightVector);
		newM.material = this.material;
		newM.textureCoords = this.textureCoords;
		return newM;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addAxes()
	{
		axes.add(new Line(new Vector(0,0,0), new Vector(100, 0, 0)));
		axes.add(new Line(new Vector(0,0,0), new Vector(0, 100, 0)));
		axes.add(new Line(new Vector(0,0,0), new Vector(0, 0, 100)));
	}
	
	public void addNormals()
	{
		for(RefPolygon polygon : polygons)
		{
			Vector n = polygon.computeNormal(points, clockwise);
			polygon.setWorldNormal(n);
			//These are just for visualization
			//n.scale(10);
			//Vector c = polygon.getCenter(points);
			//normals.add(new Line(c, c.add(n)));
		}
	}
	
	public List<Polygon> getAdjacentPolygons(Integer ref)
	{
		List<Polygon> retPolys = new LinkedList<Polygon>();
		for(RefPolygon refPoly : polygons)
		{
			if(refPoly.contains(ref))
			{
				retPolys.add(new Polygon(refPoly, this));
			}
		}
		return retPolys;
	}
	
	public Coord getTextureCoordinate(Integer ref)
	{
		//Subtract 1 because reference are 1 based, but
		//the array is 0 based
		return textureCoords.get(ref - 1);
	}

	public int getNumPoints()
	{
		return points.size();
	}
	
	public int getNumPolygons()
	{
		return polygons.size();
	}
	
	public List<Vector> getPoints()
	{
		return Collections.unmodifiableList(points);
	}
	
	public List<RefPolygon> getPolygons()
	{
		return Collections.unmodifiableList(polygons);
	}
	
	public List<Line> getAxes()
	{
		return Collections.unmodifiableList(axes);
	}
	
	public void setLightVector(Vector lightPos)
	{
		
		this.lightVector = new Line(lightPos.normalize().scale(20), new Vector(0,0,0, 1));
	}
	
	public Line getLightVector()
	{
		return lightVector;
	}
	
	public List<Line> getNormals()
	{
		return Collections.unmodifiableList(normals);
	}
	
	public int appendVertex(Vector v)
	{
		this.points.add(v);
		return points.size();
	}
	
	public void appendPolygon(RefPolygon poly)
	{
		this.polygons.add(poly);
	}
	
	/**
	 * Converts the Model to a group of Lines for our simplistic rendering
	 * method
	 * @return
	 */
	public List<Line> toLines()
	{
		List<Line> lines = new ArrayList<Line>();
		for(RefPolygon poly : polygons)
		{
			lines.addAll(poly.toLines(Collections.unmodifiableList(points)));
		}
		return lines;
	}

}
