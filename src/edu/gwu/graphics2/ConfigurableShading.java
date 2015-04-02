package edu.gwu.graphics2;

import java.awt.Color;
import java.util.logging.Logger;

public class ConfigurableShading implements ShadingModel
{
	private static Logger logger = Logger.getLogger(ConfigurableShading.class.getName());
	private final IlluminationModel illum;
	private Vector L;
	private Vector H;
	private Light light;
	private Vector camera;
	
	private Polygon polyCtx;
	private Material matCtx;
	private ShadingMode mode;
	
	public static enum ShadingMode
	{
		Constant, Gourard, Phong;
	}
	
	public ConfigurableShading(Light light, Vector camera, IlluminationModel il)
	{
		this.light = light;
		this.camera = camera;
		this.illum = il;
		this.mode = ShadingMode.Constant;
		updateVectors();
	}
	
	public void setMode(ShadingMode m)
	{
		this.mode = m;
	}
	
	public ShadingMode getMode()
	{
		return mode;
	}
	
	public void setLight(Light l)
	{
		this.light = l;
		updateVectors();
	}
	
	public void setCameraPosition(Vector camera)
	{
		this.camera = camera;
		updateVectors();
	}
	
	private void updateVectors()
	{
		this.L = light.getPosition().normalize();
		this.H = light.getPosition().add(camera).scale(.5f).normalize();
		
		//logger.info("L="+L+", H="+H);
	}
	
	@Override
	public Color getColor(Vector normal) {
		Color polyFill = illum.invoke(L, H, normal.normalize(), light.getColor(), light.getColor(), matCtx, null);
		return polyFill;
	}

	@Override
	public Color shadePixel(Vector color, Vector normal, Coord texCoord) {
		if(mode == ShadingMode.Constant)
		{
			return getColor(polyCtx, matCtx);	
		}
		if(mode == ShadingMode.Gourard)
		{
			return color.toColor();
		}
		if(mode == ShadingMode.Phong)
		{
			return illum.invoke(L, H, normal.normalize(), light.getColor(), light.getColor(), matCtx, texCoord);
		}
		return getColor(polyCtx, matCtx);
	}

	/**
	 * Used for constant shading
	 * 
	 * @param poly polygon to find the color for
	 * @param mat material that applies to that polygon
	 * @return
	 */
	public Color getColor(Polygon poly, Material mat)
	{
		Vector normal = poly.getWorldSpaceNormal().normalize();
		Color polyFill = illum.invoke(L, H, normal, light.getColor(), light.getColor(), mat, null);
		//logger.info("Produced color "+polyFill.toString()+" for normal "+normal);
		return polyFill;
	}

	@Override
	public void setPolygon(Polygon pol) {
		polyCtx = pol;
	}


	@Override
	public void setMaterial(Material mat) {
		matCtx = mat;
	}
}
