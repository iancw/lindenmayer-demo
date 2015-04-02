package edu.gwu.graphics2;

public class Coord {
	private float u;
	private float v;
	
	public Coord(float u, float v)
	{
		this.u = u;
		this.v = v;
	}
	
	public void set(double u, double v)
	{
		this.u=(float)u;
		this.v=(float)v;
	}
	
	public float getU(){ return u;}
	public float getV(){ return v; }
	
	public Coord add(Coord other)
	{
		return new Coord(this.u+other.u, this.v+other.v);
	}
	
	public Coord subtract(Coord other)
	{
		return new Coord(this.u - other.u, this.v-other.v);
	}
	
	public Coord divide(float scalar)
	{
		return new Coord(this.u/scalar, this.v/scalar);
	}
	
	public Coord computeDelta(Coord other, float scale)
	{
		return new Coord((other.u-this.u)/ scale, (other.v-this.v)/scale);
	}
	
	@Override
	public String toString()
	{
		return "("+u+", "+v+")";
	}
	
}
