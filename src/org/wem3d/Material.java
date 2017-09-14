package org.wem3d;
import org.lwjgl.BufferUtils;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.wem3d.util.Util;

import java.nio.FloatBuffer;

public class Material
{
	private static FloatBuffer materialBuffer = BufferUtils.createFloatBuffer(16);

	private Vector3f albedo;
	private float metallic;
	private float roughness;
	private float ao;
	private Vector2f wrap;
	private int albedoMap;
	private int metallicMap;
	private int roughnessMap;
	private int aoMap;
	private int normalMap;
	private int heightMap;
	private float heightScale;

	public Material(Vector3f albedo, float metallic, float roughness, float ao)
	{
		this.albedo = albedo;
		this.metallic = metallic;
		this.roughness = roughness;
		this.ao = ao;
		wrap = new Vector2f(1,1);
		heightScale = 0.1f;
	}

	public Material()
	{
		this(new Vector3f(1,0,0), 0.5f,0.5f,1f);
	}

	public FloatBuffer buffer()
	{
		materialBuffer.clear();

		materialBuffer.put(albedo.x);
		materialBuffer.put(albedo.y);
		materialBuffer.put(albedo.z);
		materialBuffer.put(metallic);

		materialBuffer.put(roughness);
		materialBuffer.put(ao);
		materialBuffer.put(wrap.x);
		materialBuffer.put(wrap.y);

		materialBuffer.put(Util.asFloat(albedoMap > 0));
		materialBuffer.put(Util.asFloat(metallicMap > 0));
		materialBuffer.put(Util.asFloat(roughnessMap > 0));
		materialBuffer.put(Util.asFloat(aoMap > 0));

		materialBuffer.put(Util.asFloat(normalMap > 0));
		materialBuffer.put(Util.asFloat(heightMap > 0));
		materialBuffer.put(heightScale);
		materialBuffer.put(0f);

		materialBuffer.flip();
		return materialBuffer;
	}


	public Vector3f getAlbedo()
	{
		return albedo;
	}

	public void setAlbedo(Vector3f albedo)
	{
		this.albedo = albedo;
	}

	public float getMetallic()
	{
		return metallic;
	}

	public void setMetallic(float metallic)
	{
		this.metallic = metallic;
	}

	public float getRoughness()
	{
		return roughness;
	}

	public void setRoughness(float roughness)
	{
		this.roughness = roughness;
	}

	public float getAo()
	{
		return ao;
	}

	public void setAo(float ao)
	{
		this.ao = ao;
	}

	public Vector2f getWrap()
	{
		return wrap;
	}

	public void setWrap(Vector2f wrap)
	{
		this.wrap = wrap;
	}

	public int getAlbedoMap()
	{
		return albedoMap;
	}

	public void setAlbedoMap(int albedoMap)
	{
		this.albedoMap = albedoMap;
	}

	public int getMetallicMap()
	{
		return metallicMap;
	}

	public void setMetallicMap(int metallicMap)
	{
		this.metallicMap = metallicMap;
	}

	public int getRoughnessMap()
	{
		return roughnessMap;
	}

	public void setRoughnessMap(int roughnessMap)
	{
		this.roughnessMap = roughnessMap;
	}

	public int getAoMap()
	{
		return aoMap;
	}

	public void setAoMap(int aoMap)
	{
		this.aoMap = aoMap;
	}

	public int getNormalMap()
	{
		return normalMap;
	}

	public void setNormalMap(int normalMap)
	{
		this.normalMap = normalMap;
	}

	public int getHeightMap()
	{
		return heightMap;
	}

	public void setHeightMap(int heightMap)
	{
		this.heightMap = heightMap;
	}

	public float getHeightScale()
	{
		return heightScale;
	}

	public void setHeightScale(float heightScale)
	{
		this.heightScale = heightScale;
	}
}
