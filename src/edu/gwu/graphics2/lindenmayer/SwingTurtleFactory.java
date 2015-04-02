package edu.gwu.graphics2.lindenmayer;

import java.awt.Graphics2D;

public class SwingTurtleFactory implements TurtleFactory{

	private Graphics2D graphics;
	
	public SwingTurtleFactory(Graphics2D graphics)
	{
		this.graphics = graphics;
	}
	
	
	public Turtle createTurtle(double x, double y, double angle) {
		return new SwingTurtle(x, y, angle, graphics);
	}


	@Override
	public Turtle createTurtle(double x, double y, double z, double theta,
			double phi) {
		return new SwingTurtle(x, y, theta, graphics);
	}


	@Override
	public Turtle createTurtle(Turtle other) {
		// TODO Auto-generated method stub
		return null;
	}

}
