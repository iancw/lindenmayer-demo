package edu.gwu.graphics2;

import java.awt.Color;
import java.util.logging.Logger;

/**
 * Implements the phone illumination model, using the equation
 * 
 * I = kaIa + Ii( kd(L*N) + ks(R*V)^n)
 * 
 * @author ian
 *
 */
public class PhongIllumination implements IlluminationModel
{
	private Logger logger = Logger.getLogger(PhongIllumination.class.getName());

	@Override
	public Color invoke(Vector L, Vector H, Vector N, 
			Color incidentIntens, Color ambientIntens, Material m, Coord texCoord)
	{
		Vector ambient = m.getAmbient(texCoord); //ka
		Vector diffuse = m.getDiffuse(texCoord); //kd
		Vector spec = m.getSpecular(texCoord);  //ks
		
		float angle = L.dot(N);
		
		if(angle < 0){ 
			angle*=-1;
			angle=0;
			//logger.info("diffuse angle was negative, setting to 0");
		}
		Vector scaledDiffuse = diffuse.scale(angle);
		
		float specScale = N.dot(H);
		
		if(specScale < 0){
			specScale*=-1;
			specScale=0;
			//logger.info("specular angle was negative, setting to "+specScale);
		}
		float expAngle = (float)Math.pow(specScale, m.getSpecularExponent());
		
		Vector scaledSpec = spec.scale(expAngle);		
		
		Vector phongVect = scaledDiffuse.add(scaledSpec);
		
		if(expAngle > .01)
		{
//			logger.info("------------\n"
//			+"diffuse: "+diffuse+", specular: "+spec+"\n"
//			+"L: "+L+", H: "+H+", N: "+N+"\n"
//			+"diffuse angle: "+angle+"\n"
//			+"specular angle: "+specScale+"\n"
//			+"(N*H)^"+m.getSpecularExponent()+" = "+expAngle+"\n"
//			+phongVect.toString()+" = "+scaledDiffuse+" + "+scaledSpec);
		}
		
		Vector phongTerm = Vector.fromColor(incidentIntens).scale(phongVect);
		
		Vector result = ambient.scale(Vector.fromColor(ambientIntens)).add(phongTerm);
		if(result.getX() > 1 || result.getY() > 1 || result.getZ() > 1)
		{
			//System.out.println("Over saturated");
		}
		return result.toColor();
	}

}
