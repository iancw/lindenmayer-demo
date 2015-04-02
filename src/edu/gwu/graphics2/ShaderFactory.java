package edu.gwu.graphics2;

public interface ShaderFactory {
	ShadingModel create(Light light, Vector camera);
	
}
