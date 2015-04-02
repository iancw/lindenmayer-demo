/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.Color;

/**
 * This represents a vector in 3 or 4 dimensions.
 * 
 * @author ian
 *
 */
public class Vector
{
	
	public static Vector parseVector(String str)
	{
		String[] parts = str.split(",");
		return new Vector(
				Float.parseFloat(parts[0]), 
				Float.parseFloat(parts[1]), 
				Float.parseFloat(parts[2]), 
				0);//If this isn't a zero, weird things dissapear
	}
	
	/**
	 * Returns a new Vector with the 4th (w) component zero.
	 * @param v
	 * @return
	 */
	public static Vector zeroW(Vector v)
	{
		return new Vector(v.x, v.y, v.z, 0);
	}
	
	public static Vector fromColor(Color c)
	{
		return new Vector(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 0);
	}
	
	private final float x;
	private final float y;
	private final float z;
	private final float w;
	
	public Vector(Vector v)
	{
		this.x = v.x;
		this.y=v.y;
		this.z=v.z;
		this.w=v.w;
	}
	
	public Vector(float[] values)
	{
		if(values.length > 0)
		{
			x=values[0];
			if(values.length > 1)
			{
				y = values[1];
				if(values.length > 2)
				{
					z = values[2];
					if(values.length > 3)
					{
						w = values[3];
					}else
					{
						w = 0;
					}
				}else
				{
					z = w = 0;
				}
			}else
			{
				y = z = w = 0;
			}
		}else
		{
			x = y = z = w = 0;
		}
		
	}
	
	public Vector(float x, float y)
	{
		this(x, y, 0, 0);
	}
	
	public Vector(float x, float y, float z)
	{
		this(x, y, z, 0);
	}
	
	public Vector(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector capTo(float max)
	{
		return new Vector(x > max ? max : x,
			y > max ? max : y,
			z > max ? max : z,
			w > max ? max : w);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public float getW()
	{
		return w;
	}
	
	public Vector toCartesian()
	{
		return new Vector(x/w, y/w, z/w, 1);
	}
	
	public Vector normalize()
	{
		float mag = this.magnitude();
		return new Vector(x/mag, y/mag, z/mag, w/mag);
	}
	
	public Vector subtract(Vector other)
	{
		return new Vector(this.x-other.x, this.y-other.y, this.z-other.z, this.w-other.w);
	}
	
	public Vector add(Vector other)
	{
		return new Vector(this.x+other.x, this.y+other.y, this.z+other.z, this.w+other.w);
	}
	
	public Vector scale(float value)
	{
		return new Vector(this.x*value, this.y*value, this.z*value, this.w*value);
	}
	
	public Vector scale(Vector vect)
	{
		return new Vector(this.x*vect.x, this.y*vect.y, this.z*vect.z, this.w*vect.w);
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
	public float dot(Vector other)
	{
		return this.x*other.x + this.y*other.y + this.z*other.z + this.w*other.w;
	}
	
	public Vector cross(Vector other)
	{
		return new Vector(
				this.y*other.z-this.z*other.y, 
				this.z*other.x-this.x*other.z, 
				this.x*other.y -this.y*other.x);
	}
	
	public Color toColor()
	{
		try
		{
			return new Color(x, y, z, 1f);
		}catch(IllegalArgumentException e)
		{
			return new Color(0,0,0, 1);
		}
	}
	
	@Override
	public String toString()
	{
		return "<"+x+", "+y+", "+z+", "+w+">";
	}
}
