package org.wem3d;
public class Skybox
{
	private int vao;
	private int ebo;
	private int count;
	private int cubeMap;

	public Skybox(int vao, int ebo, int count, int cubeMap)
	{
		this.vao = vao;
		this.ebo = ebo;
		this.count = count;
		this.cubeMap = cubeMap;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
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

	public int getCubeMap()
	{
		return cubeMap;
	}

	public void setCubeMap(int cubeMap)
	{
		this.cubeMap = cubeMap;
	}
}
