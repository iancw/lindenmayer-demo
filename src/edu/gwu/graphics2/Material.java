package edu.gwu.graphics2;

import java.awt.Color;

public class Material {
	
	public static Material DEFAULT_MATERIAL = new Material(
			new Vector(0.3f, 0.3f, 0.3f), 
			new Vector(0.3f, 0.3f, 0.3f), 
			new Vector(0.4f, 0.4f, 0.4f),
			null,
			5);
	
	private final Vector diff;
	private final Vector spec;
	private final Vector amb;
	private final float n;
	//This can be null
	private final BufferedImageTexture tex;
	
	/**
	 * 
	 * @param amb
	 * @param diff
	 * @param spec
	 * @param tex  may be null
	 * @param n
	 */
	public Material(Vector amb, Vector diff, Vector spec, BufferedImageTexture tex, float n)
	{
		this.diff = diff;
		this.spec = spec;
		this.amb = amb;
		this.n=n;
		this.tex = tex;
	}
	
	private Vector findColor(Vector term, Coord textureCoord)
	{
		if(textureCoord != null && tex != null)
		{
			Color c = this.tex.getColor(textureCoord);
			Vector v = Vector.fromColor(c);
			Vector scaled =  v.scale(spec);
			if(scaled.getX() > 1 || scaled.getY() > 1 || scaled.getZ() > 1)
			{
				System.out.println("Over saturated");
			}
			return scaled;
		}
		return term;
	}
	
	/**
	 * 
	 * @param textureCoord may be null
	 * @return
	 */
	public Vector getDiffuse(Coord textureCoord)
	{
		return findColor(diff, textureCoord);
	}
	
	public Vector getSpecular(Coord textureCoord)
	{
		return findColor(spec, textureCoord);
	}
	
	public Vector getAmbient(Coord textureCoord)
	{
		return findColor(amb, textureCoord);
	}
	
	public float getSpecularExponent()
	{
		return n;
	}
}
