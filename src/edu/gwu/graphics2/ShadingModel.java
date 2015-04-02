package edu.gwu.graphics2;

import java.awt.Color;

public interface ShadingModel {
	
	Color getColor(Vector normal);
	Color shadePixel(Vector color, Vector normal, Coord textureCoord);
	void setPolygon(Polygon pol);
	void setMaterial(Material mat);
}
