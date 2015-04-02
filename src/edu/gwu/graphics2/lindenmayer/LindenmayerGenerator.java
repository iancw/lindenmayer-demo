package edu.gwu.graphics2.lindenmayer;

import java.util.Stack;

public class LindenmayerGenerator {

	private final Stack<Turtle> turtleStack = new Stack<Turtle>();
	
	private final TurtleFactory factory;
	private Turtle currentTurtle;
	private double b;
	private double d;
	private double startX;
	private double startY;
	private double startZ;
	private double startTheta;
	private double startPhi;
	
	public LindenmayerGenerator(TurtleFactory fact, 
			double length, double angle, 
			double x, double y, double z, 
			double theta, double phi)
	{
		this.factory = fact;
		this.d = length;
		this.b = angle;
		this.startX = x;
		this.startY = y;
		this.startZ = z;
		this.startTheta = theta;
		this.startPhi = phi;
	}
	
	public void generate(String lString)
	{
		currentTurtle = this.factory.createTurtle(startX, startY, startZ, startTheta, startPhi);
		for(int i=0; i<lString.length(); i++)
		{
			char c = lString.charAt(i);
				
			switch(c)
			{
			case '[':
				turtleStack.push(currentTurtle);
				currentTurtle = factory.createTurtle(currentTurtle);
				break;
			case ']':
				currentTurtle = turtleStack.pop();
				break;
			case '+':
				currentTurtle.turnRight(b);
				break;
			case '-':
				currentTurtle.turnLeft(b);
				break;
			case '&':
				currentTurtle.pitchUp(b);
				break;
			case '^':
				currentTurtle.pitchDown(b);
				break;
			case 'F':
				currentTurtle.moveForward(d);
				break;
			case '\'':
				StringBuffer multiplier = new StringBuffer();
				boolean done = false;
				for(i=i+1;i<lString.length() && !done; i++)
				{
					c = lString.charAt(i);
					switch(c)
					{
						case '(': //entering digits
							break;
						case ')'://done finding digits
							done = true;
							break;
						default:
							multiplier.append(c);
					}
					if(done)
					{
						double newmult = Double.parseDouble(multiplier.toString());
						currentTurtle.addMultiplier(newmult);
						i--;
					}
				}
				
				break;
			}
		}
	}

}
