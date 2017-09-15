package org.wem3d;

import java.util.HashMap;
import java.util.Map;

public class Scene
{
	private Map<Integer, Model> modelMap;
	private Map<Integer, Light> lightMap;
	private Map<Integer, Control> controlMap;
	private Skybox[] skyboxes;

	public Scene()
	{
		modelMap = new HashMap<Integer, Model>();
		lightMap = new HashMap<Integer, Light>();
		controlMap = new HashMap<Integer, Control>();
		skyboxes = new Skybox[1];
	}

	public void add(Skybox skybox)
	{
		skyboxes[0] = skybox;
	}

	public void add(Model model)
	{
		modelMap.put(model.getVao(), model);
	}

	public void add(Light light)
	{
		lightMap.put(light.id(), light);
	}

	public void add(Control control)
	{
		controlMap.put(control.id(), control);
	}

	public Map<Integer, Model> getModelMap()
	{
		return modelMap;
	}

	public void setModelMap(Map<Integer, Model> modelMap)
	{
		this.modelMap = modelMap;
	}

	public Map<Integer, Light> getLightMap()
	{
		return lightMap;
	}

	public void setLightMap(Map<Integer, Light> lightMap)
	{
		this.lightMap = lightMap;
	}

	public Map<Integer, Control> getControlMap()
	{
		return controlMap;
	}

	public void setControlMap(Map<Integer, Control> controlMap)
	{
		this.controlMap = controlMap;
	}

	public Skybox[] getSkyboxes()
	{
		return skyboxes;
	}

	public void setSkyboxes(Skybox[] skyboxes)
	{
		this.skyboxes = skyboxes;
	}
}
