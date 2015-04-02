package edu.gwu.graphics2.lindenmayer;

import java.awt.Graphics2D;

/**
 * Only a 2d implementation
 * 
 * @author ian
 *
 */
public class SwingTurtle implements Turtle {
	
	private double x;
	private double y;
	private double angle;
	private Graphics2D graphics;
	
	public SwingTurtle(double x, double y, double angle, Graphics2D g)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.graphics = g;
	}

	@Override
	public void moveForward(double length) {
		double newx = x + length * Math.cos(angle);
		double newy = y + length * Math.sin(angle);
		graphics.drawLine((int)x, (int)y, (int)newx, (int)newy);
		this.x = newx;
		this.y = newy;
	}

	@Override
	public void floatForward(double length) {
		x = x + length * Math.cos(angle);
		y = y + length * Math.sin(angle);
	}

	@Override
	public void turnLeft(double b) {
		this.angle = this.angle + b;
	}

	@Override
	public void turnRight(double b) {
		this.angle = this.angle - b;		
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getTheta() {
		return angle;
	}

	@Override
	public void pitchUp(double angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pitchDown(double angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getPhi() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addMultiplier(double length) {
		// TODO Auto-generated method stub
		
	}
	
}
