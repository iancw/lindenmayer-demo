package edu.gwu.graphics2.lindenmayer;

public interface Turtle {

	/**
	 * Move foward and draw a line
	 * @param length length to move forward
	 */
	public void moveForward(double length);
	
	/**
	 * Does not draw a line
	 * @param length
	 */
	public void floatForward(double length);
	
	/**
	 * Adds a length multiplier which will adjust
	 * any passed in length
	 * @param length
	 */
	public void addMultiplier(double length);
	
	public void turnLeft(double angle);
	
	public void turnRight(double angle);
	
	public void pitchUp(double angle);
	
	public void pitchDown(double angle);
	
	public double getX();
	
	public double getY();
	
	public double getZ();
	
	public double getTheta();
	
	public double getPhi();
}
