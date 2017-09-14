package org.wem3d;
import java.util.HashMap;
import java.util.Map;

public class Model
{
	private int vao;
	private int ebo;
	private Mod defaultMod;
	private Map<String, Mod> modMap;
	private Map<Integer, Object3D> object3DMap;
	private boolean active;

	public Model(int vao, int ebo, Mod defaultMod)
	{
		this.vao = vao;
		this.ebo = ebo;
		this.defaultMod = defaultMod;
		this.active = true;
		modMap = new HashMap<String, Mod>();
		object3DMap = new HashMap<Integer, Object3D>();
	}

	public void add(String name, Mod mod)
	{
		modMap.put(name, mod);
	}

	public int getVao()
	{
		return vao;
	}

	public void setVao(int vao)
	{
		this.vao = vao;
	}

	public int getEbo()
	{
		return ebo;
	}

	public void setEbo(int ebo)
	{
		this.ebo = ebo;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public Mod getDefaultMod()
	{
		return defaultMod;
	}

	public void setDefaultMod(Mod defaultMod)
	{
		this.defaultMod = defaultMod;
	}

	public Map<String, Mod> getModMap()
	{
		return modMap;
	}

	public Map<Integer, Object3D> getObject3DMap()
	{
		return object3DMap;
	}

	public void setObject3DMap(Map<Integer, Object3D> object3DMap)
	{
		this.object3DMap = object3DMap;
	}
}
