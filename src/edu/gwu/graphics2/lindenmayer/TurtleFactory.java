package edu.gwu.graphics2.lindenmayer;

public interface TurtleFactory {
	
	public Turtle createTurtle(double x, double y, double z, double theta, double phi);
	
	public Turtle createTurtle(Turtle other);
	
}
