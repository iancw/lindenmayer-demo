package edu.gwu.graphics2;

import edu.gwu.graphics2.ConfigurableShading.ShadingMode;

public class ConfigurableShaderFactory implements ShaderFactory
{
	private final IlluminationModel ill;
	private final ShadingMode mode;
	
	public ConfigurableShaderFactory(IlluminationModel il, ShadingMode mode)
	{
		this.ill = il;
		this.mode = mode;
	}
	
	@Override
	public ShadingModel create(Light light, Vector camera) 
	{
		ConfigurableShading shade = new ConfigurableShading(light, camera, ill);
		shade.setMode(mode);
		return shade;
	}

}
