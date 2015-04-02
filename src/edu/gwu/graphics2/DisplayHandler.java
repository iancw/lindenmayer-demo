/**
 * 
 */
package edu.gwu.graphics2;

import java.util.List;


/**
 * This is an interface that allows multiple types of OpenGL visualization.
 * While I was debugging I used this, but eventually settled on DisplayModelHandler.
 * I took out the alternatives because I think they're confusing and not worth much.
 * Theoretically this could use full OpenGL calls and could compare, for example,
 * my shading with OpenGL's built in shading.
 * 
 * @author ian
 *
 */
public interface DisplayHandler
{
	void renderModels(List<Model> m, Light light, Vector worldSpaceCamera, ShaderFactory shader);

}
