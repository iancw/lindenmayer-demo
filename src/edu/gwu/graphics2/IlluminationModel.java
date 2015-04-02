package edu.gwu.graphics2;

import java.awt.Color;

public interface IlluminationModel {
	
	
	Color invoke(
			Vector L, 
			Vector H, 
			Vector N, 
			Color incidentIntens, 
			Color ambientIntens, 
			Material m, 
			Coord texCoord);
}
