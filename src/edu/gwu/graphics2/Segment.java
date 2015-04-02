package edu.gwu.graphics2;



public class Segment {
	private final Entry left;
	private final Entry right;
	
	public Segment(Entry left, Entry right)
	{
		if(left.x > right.x)
		{
			throw new IllegalArgumentException("Left entry should be left or right Entry");
		}
		this.left = left;
		this.right = right;
	}
	
	Vector leftNormal()
	{
		return left.normal;
	}
	
	Coord leftTextureCoord()
	{
		return left.texture;
	}
	
	Coord rightTextureCoord()
	{
		return right.texture;
	}
	
	Vector rightNormal()
	{
		return right.normal;
	}
	
	Vector leftColor()
	{
		return left.color;
	}
	
	Vector rightColor()
	{
		return right.color;
	}
	
	float leftYMax()
	{
		return left.yMax;
	}
	
	float rightYMax()
	{
		return right.yMax;
	}
	
	float zLeft(int y)
	{
		return left.getZ(y);
	}
	
	float zRight(int y)
	{
		return right.getZ(y);
	}
//	
//	float zDelta()
//	{
//		return  (right.z - left.z) / (right.x - left.x);
//	}
	
	float xLeft()
	{
		return left.x;
	}
	
	float xRight()
	{
		return right.x;
	}
}
