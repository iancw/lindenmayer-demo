package edu.gwu.graphics2.lindenmayer;

import java.util.Arrays;

import edu.gwu.graphics2.Line;
import edu.gwu.graphics2.LineModel;
import edu.gwu.graphics2.Matrix;
import edu.gwu.graphics2.PolygonModel;
import edu.gwu.graphics2.RefPolygon;
import edu.gwu.graphics2.Vector;

public class LineModelTurtle implements Turtle {
	private double x;
	private double y;
	private double z;
	private double theta;
	private double phi;
	private LineModel model;
	private int ref=0;
	private double multiplier = 1;
	private PolygonModel polyModel;
	
	public LineModelTurtle(LineModelTurtle other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.theta = other.theta;
		this.phi = other.phi;
		this.model = other.model;
		this.ref = other.ref;
		this.multiplier = other.multiplier;
		this.polyModel = other.polyModel;
	}
	
	public LineModelTurtle(
			double x, double y, double z, 
			double theta, double phi,
			LineModel model,
			PolygonModel poly)
	{
		this.x = x;
		this.y = y;
		this.z=z;
		this.theta = theta;
		this.phi = phi;
		this.model = model;
		this.polyModel = poly;
	}

	
	
	@Override
	public void moveForward(double length) {
		length = multiplier * length;
		double newx = x + length * Math.cos(theta) * Math.sin(phi);
		double newy = y + length * Math.sin(theta) * Math.sin(phi);
		double newz = z + length * Math.cos(phi);

		//to something
		Vector orig = new Vector((float)x, (float)y, (float)z);
		Vector newLine = new Vector((float)newx, (float)newy, (float)newz);
		Line line = new Line(orig, ref++, newLine, ref++);
		
		Matrix rotz = Matrix.fromRotationZ((float)phi);
		Matrix roty = Matrix.fromRotationY((float)theta);
		Matrix scale = Matrix.fromScale(new Vector((float)length*.1f, (float)length*.1f, (float)length*.1f));
		Matrix trans0 = Matrix.fromTranslationVector(orig);
		Matrix trans1 = Matrix.fromTranslationVector(newLine);
		
		Matrix mat0 = trans0.multiply(roty).multiply(rotz).multiply(scale);
		Matrix mat1 = trans1.multiply(roty).multiply(rotz).multiply(scale);
		
		Vector [] pointOrig = new Vector[4]; 
		Vector [] pointNext = new Vector[4]; 
		
		int i=0;
		Vector[] points = new Vector[]{new Vector(1, 0, 0), new Vector(0, 1, 0), new Vector(-1, 0, 0), new Vector(0, -1, 0)};
		for(Vector p : points)
		{
			pointOrig[i++] = mat0.multiply(p);
		}
		i=0;
		for(Vector p : points)
		{
			pointNext[i++] = mat1.multiply(p);
		}
		
		Vector vectLine = newLine.subtract(orig);
		Vector nmlZ = new Vector(-1*vectLine.getY(), vectLine.getX(), vectLine.getZ()).normalize().scale((float)length * .1f);
		Vector nmlY = new Vector(vectLine.getZ(), vectLine.getY(), -vectLine.getX()).normalize().scale((float)length*.1f);
		

//		Vector vectLine = newLine.subtract(orig);
//		Vector nmlZ = new Vector(-1 * vectLine.getY(), vectLine.getX(), 0);
//		Vector nmlY = vectLine.cross(nmlZ);
		pointOrig[0] = orig.add(nmlZ);
		pointOrig[1] = orig.subtract(nmlZ);
		pointOrig[2] = orig.add(nmlY);
		pointOrig[3] = orig.subtract(nmlY);
		
		pointNext[0] = newLine.add(nmlZ);
		pointNext[1] = newLine.subtract(nmlZ);
		pointNext[2] = newLine.add(nmlY);
		pointNext[3] = newLine.subtract(nmlY);
		
		Integer [] origRefs = new Integer[4];
		Integer [] nextRefs = new Integer[4];
		for(i=0; i<4; i++)
		{
			origRefs[i] = polyModel.appendVertex(pointOrig[i]);
			nextRefs[i] = polyModel.appendVertex(pointNext[i]);
		}
//		int pt1_idx = polyModel.appendVertex(pt1);
//		int pt2_idx = polyModel.appendVertex(pt2);
//		int pt3_idx = polyModel.appendVertex(pt3);
//		int pt4_idx = polyModel.appendVertex(pt4);
//		int pt1a_idx = polyModel.appendVertex(pt1a);
//		int pt2a_idx = polyModel.appendVertex(pt2a);
//		int pt3a_idx = polyModel.appendVertex(pt3a);
//		int pt4a_idx = polyModel.appendVertex(pt4a);
		

		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{origRefs[0], nextRefs[0], nextRefs[2], origRefs[2]}))); 
		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{origRefs[2], nextRefs[2], nextRefs[1], origRefs[1]}))); 
		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{origRefs[1], nextRefs[1], nextRefs[3], origRefs[3]}))); 
		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{origRefs[3], nextRefs[3], nextRefs[0], origRefs[0]})));
		
//		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{pt1_idx, pt1a_idx, pt3a_idx, pt3_idx})));
//		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{pt3_idx, pt3a_idx, pt2a_idx, pt2_idx})));
//		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{pt2_idx, pt2a_idx, pt4a_idx, pt4_idx})));
//		polyModel.appendPolygon(new RefPolygon(Arrays.asList(new Integer[]{pt4_idx, pt4a_idx, pt1a_idx, pt1_idx})));
		
		for(i=0; i<4; i++)
		{
			model.addLine(new Line(pointOrig[i], 0, pointNext[i], 0));
		}
		
		model.addLine(line);
		
		this.x = newx;
		this.y = newy;
		this.z = newz;
	}

	@Override
	public void floatForward(double length) {
		length = multiplier * length;
		
		double newx = x + length * Math.cos(theta) * Math.sin(phi);
		double newy = y + length * Math.sin(theta) * Math.sin(phi);
		double newz = z + length * Math.cos(phi);

		//to something
		
		this.x = newx;
		this.y = newy;
		this.z = newz;
	}

	@Override
	public void turnLeft(double b) {
		this.theta = this.theta + b;
	}

	@Override
	public void turnRight(double b) {
		this.theta = this.theta - b;		
	}
	
	@Override
	public void pitchUp(double p)
	{
		this.phi += p;
	}
	
	@Override
	public void pitchDown(double p)
	{
		this.phi -= p;
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
		return theta;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public double getPhi() {
		return phi;
	}

	@Override
	public void addMultiplier(double length) {
		multiplier *= length;
	}
	
}
