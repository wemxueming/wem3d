package org.wem3d;
import java.util.HashMap;
import java.util.Map;

public class Mod
{
	private int meshId = 0;

	private Map<Integer, Mesh> meshMap;
	private Map<String, Integer> meshNameMap;

	public Mod()
	{
		meshMap = new HashMap<Integer, Mesh>();
		meshNameMap = new HashMap<String, Integer>();
	}

	public void add(Mesh mesh)
	{
		meshMap.put(meshId, mesh);
		meshNameMap.put(mesh.getName(), meshId);
		meshId ++;
	}

	public Mesh get(int index)
	{
		return meshMap.get(index);
	}

	public Mesh get(String name)
	{
		return meshMap.get(meshNameMap.get(name));
	}

	public Map<Integer, Mesh> getMeshMap()
	{
		return meshMap;
	}
}
