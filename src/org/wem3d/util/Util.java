package org.wem3d.util;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.wem3d.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Util
{
	public final static long getTime()
	{
		return System.nanoTime() / 1000000;
	}

	public final static List<Integer> asList(int[] arrays)
	{
		List<Integer> list = new ArrayList<Integer>();
		for (int i : arrays)
		{
			list.add(i);
		}
		return list;
	}

	public final static float asFloat(boolean b)
	{
		if (b)
		{
			return 1f;
		}
		else
		{
			return 0f;
		}
	}

	public final static Map<String, Integer> buildKeyMap()
	{
		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		keyMap.put("w", 17);
		keyMap.put("s", 31);
		keyMap.put("a", 30);
		keyMap.put("d", 32);
		keyMap.put("esc", 1);
		keyMap.put("up", 200);
		keyMap.put("down", 208);
		keyMap.put("+", 13);
		keyMap.put("-", 12);
		return keyMap;
	}

	public final static Map<Float, Vector3f> buildAttenuationMap()
	{
		Map<Float, Vector3f> map = new HashMap<Float, Vector3f>();
		map.put(7f, new Vector3f(1.0f, 0.7f, 1.8f));
		map.put(13f, new Vector3f(1.0f, 0.35f, 0.44f));
		map.put(20f, new Vector3f(1.0f, 0.22f, 0.20f));
		map.put(32f, new Vector3f(1.0f, 0.14f, 0.07f));
		map.put(50f, new Vector3f(1.0f, 0.09f, 0.032f));
		map.put(65f, new Vector3f(1.0f, 0.07f, 0.017f));
		map.put(100f, new Vector3f(1.0f, 0.045f, 0.0075f));
		map.put(160f, new Vector3f(1.0f, 0.027f, 0.0028f));

		map.put(200f, new Vector3f(1.0f, 0.022f, 0.0019f));
		map.put(325f, new Vector3f(1.0f, 0.014f, 0.0007f));
		map.put(600f, new Vector3f(1.0f, 0.007f, 0.0002f));
		map.put(3250f, new Vector3f(1.0f, 0.0014f, 0.000007f));
		return map;
	}

	public final static int buildMipmap(int width, int height)
	{
		if (width >= 4096 || height >= 4096)
		{
			return 13;
		}
		if ((width < 4096 && width >= 2048) || (height < 4096 && height >= 2048))
		{
			return 12;
		}
		if ((width < 2048 && width >= 1024) || (height < 2048 && height >= 1024))
		{
			return 11;
		}
		if ((width < 1024 && width >= 512) || (height < 1024 && height >= 512))
		{
			return 10;
		}
		if ((width < 512 && width >= 256) || (height < 512 && height >= 256))
		{
			return 9;
		}
		if ((width < 256 && width >= 128) || (height < 256 && height >= 128))
		{
			return 8;
		}
		if ((width < 128 && width >= 64) || (height < 128 && height >= 64))
		{
			return 7;
		}
		if ((width < 64 && width >= 32) || (height < 64 && height >= 32))
		{
			return 6;
		}
		if ((width < 32 && width >= 16) || (height < 32 && height >= 16))
		{
			return 5;
		}
		if ((width < 16 && width >= 8) || (height < 16 && height >= 8))
		{
			return 4;
		}
		if ((width < 8 && width >= 4) || (height < 8 && height >= 4))
		{
			return 3;
		}
		if ((width < 4 && width >= 2) || (height < 4 && height >= 2))
		{
			return 2;
		}
		return 1;
	}

	public final static void buildVertexTangent(int[] indices, Vertex...vertexs)
	{
		for (int i = 0; i < indices.length; i+=3)
		{
			buildTangent(vertexs[indices[i]], vertexs[indices[i+1]], vertexs[indices[i+2]]);
		}
	}

	public final static void buildVertexTangent(int[] indices, List<Vertex> vertexs)
	{
		for (int i = 0; i < indices.length; i+=3)
		{
			buildTangent(vertexs.get(indices[i]), vertexs.get(indices[i+1]), vertexs.get(indices[i+2]));
		}
	}

	public final static void buildVertexTangent(List<Integer> indices, List<Vertex> vertexs)
	{
		for (int i = 0; i < indices.size(); i+=3)
		{
			buildTangent(vertexs.get(indices.get(i)), vertexs.get(indices.get(i+1)), vertexs.get(indices.get(i + 2)));
		}
	}

	public final static Vector3f buildTangent(Vertex vt0, Vertex vt1, Vertex vt2)
	{
		Vector3f v0 = vt0.getPosition();
		Vector3f v1 = vt1.getPosition();
		Vector3f v2 = vt2.getPosition();
		// Get texture coordinates of the polygon
		Vector2f uv0 = vt0.getTexcoord();
		Vector2f uv1 = vt1.getTexcoord();
		Vector2f uv2 = vt2.getTexcoord();
		// Get normals of the polygon
		Vector3f n0 = vt0.getNormal();
		Vector3f n1 = vt1.getNormal();
		Vector3f n2 = vt2.getNormal();
		// Calculate position difference
		Vector3f deltaPos1 = new Vector3f();//v1 - v0;
		Vector3f.sub(v1, v0, deltaPos1);
		Vector3f deltaPos2 = new Vector3f();//v2 - v0;
		Vector3f.sub(v2, v0, deltaPos2);
		// Calculate texture coordinate difference
		Vector2f deltaUV1 = new Vector2f();//new Vector2f(uv1.x - uv0.x, uv1.y - uv0.y);//uv1 - uv0;
		Vector2f.sub(uv1, uv0, deltaUV1);
		Vector2f deltaUV2 = new Vector2f();//new Vector2f(uv2.x - uv0.x, uv2.y - uv0.y);//uv2 - uv0;
		Vector2f.sub(uv2, uv0, deltaUV2);
		// Calculate buildTangent and bitangent
		float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV1.y * deltaUV2.x);

		Vector3f tangent = new Vector3f();
		tangent.x = f * (deltaUV2.y * deltaPos1.x - deltaUV1.y * deltaPos2.x);
		tangent.y = f * (deltaUV2.y * deltaPos1.y - deltaUV1.y * deltaPos2.y);
		tangent.z = f * (deltaUV2.y * deltaPos1.z - deltaUV1.y * deltaPos2.z);
		tangent.normalize();

		Vector3f bitangent = new Vector3f();
		bitangent.x = f * (-deltaUV2.x * deltaPos1.x + deltaUV1.x * deltaPos2.x);
		bitangent.y = f * (-deltaUV2.x * deltaPos1.y + deltaUV1.x * deltaPos2.y);
		bitangent.z = f * (-deltaUV2.x * deltaPos1.z + deltaUV1.x * deltaPos2.z);
		bitangent.normalize();

		vt0.setTangent(tangent);
		vt1.setTangent(tangent);
		vt2.setTangent(tangent);

		vt0.setBitangent(bitangent);
		vt1.setBitangent(bitangent);
		vt2.setBitangent(bitangent);

		return tangent;
	}

	public final static Vector3f formatVec3(String str)
	{
		String[] strs = str.split("\\s+");
		return new Vector3f(Float.parseFloat(strs[0]), Float.parseFloat(strs[1]), Float.parseFloat(strs[2]));
	}

	public final static Vector2f formatVec2(String str)
	{
		String[] strs = str.split("\\s+");
		return new Vector2f(Float.parseFloat(strs[0]), Float.parseFloat(strs[1]));
	}

	public final static int formatInt(String str)
	{
		String[] strs = str.split("\\s+");
		return Integer.parseInt(strs[1]);
	}

	public final static float formatFloat(String str)
	{
		String[] strs = str.split("\\s+");
		return Float.parseFloat(strs[1]);
	}

	public final static String formatString(String str)
	{
		String[] strs = str.split("\\s+");
		if (strs.length == 1)
		{
			return null;
		}
		return strs[1];
	}

	public final static Vector3f formatSlashVec3(String str)
	{
		String[] strs = str.split("/+");
		if (strs.length == 2)
		{
			return new Vector3f(Float.parseFloat(strs[0]), 0, Float.parseFloat(strs[1]));
		}
		else
		{
			return new Vector3f(Float.parseFloat(strs[0]), Float.parseFloat(strs[1]), Float.parseFloat(strs[2]));
		}
	}

	public final static Vector3f[] formatVec3s(String str)
	{
		String[] strs = str.split("\\s+");
		String strs1 = strs[1];
		String strs2 = strs[2];
		String strs3 = strs[3];
		return new Vector3f[]{
				formatSlashVec3(strs1),
				formatSlashVec3(strs2),
				formatSlashVec3(strs3),
		};
	}
}
