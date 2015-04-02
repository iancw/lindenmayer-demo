package edu.gwu.graphics2.lindenmayer;

import java.util.LinkedList;

import edu.gwu.graphics2.Coord;
import edu.gwu.graphics2.LineModel;
import edu.gwu.graphics2.Material;
import edu.gwu.graphics2.PolygonModel;
import edu.gwu.graphics2.RefPolygon;
import edu.gwu.graphics2.Vector;

public class LineModelTurtleFactory implements TurtleFactory{

	private LineModel model = new LineModel();
	private PolygonModel polyModel;
	
	public LineModelTurtleFactory()
	{
		polyModel = new PolygonModel("Synthesized", 
				new LinkedList<Vector>(), new LinkedList<RefPolygon>(), 
				new LinkedList<Coord>(), Material.DEFAULT_MATERIAL);
	}
	
	@Override 
	public Turtle createTurtle(Turtle other)
	{
		if(other instanceof LineModelTurtle)
		{
			return new LineModelTurtle((LineModelTurtle)other);
		}else
		{
			return createTurtle(other.getX(), other.getY(), other.getZ(), other.getTheta(), other.getPhi());
		}
	}
	
	@Override
	public Turtle createTurtle(double x, double y, double z, double theta,
			double phi) {
		return new LineModelTurtle(x, y, z, theta, phi, model, polyModel);
	}
	
	public LineModel getModel()
	{
		return model;
	}
	
	public PolygonModel getPolygonModel()
	{
		return polyModel;
	}
}
