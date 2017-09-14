package org.wem3d;
public class Mesh
{
	private String name;
	private int count;
	private Material material;
	private boolean active;

	public Mesh(String name, int count, Material material)
	{
		this.name = name;
		this.count = count;
		this.material = material;
		this.active = true;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
}
