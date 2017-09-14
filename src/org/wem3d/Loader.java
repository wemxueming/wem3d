package org.wem3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.wem3d.util.BufferUtil;
import org.wem3d.util.FileUtil;
import org.wem3d.util.OGL;
import org.wem3d.util.Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loader
{
	private static Map<String, Model> modelCacheMap = new HashMap<String, Model>();
	private static Map<String, Integer> textureCacheMap = new HashMap<String, Integer>();

	private static String planName = "plan";
	private static String cubeName = "cube";

	public Loader()
	{
	}

	public Model loadPlan()
	{
		Model model = modelCacheMap.get(planName);
		if (model == null)
		{
			Vertex v1 = new Vertex(new Vector3f(-0.5f, 0, -0.5f), new Vector2f(0, 0), new Vector3f(0, 1, 0));
			Vertex v2 = new Vertex(new Vector3f(0.5f, 0, -0.5f), new Vector2f(1, 0), new Vector3f(0, 1, 0));
			Vertex v3 = new Vertex(new Vector3f(-0.5f, 0, 0.5f), new Vector2f(0, 1), new Vector3f(0, 1, 0));
			Vertex v4 = new Vertex(new Vector3f(0.5f, 0, 0.5f), new Vector2f(1, 1), new Vector3f(0, 1, 0));

			int[] indices = new int[]{0,2,1,1,2,3};

			Util.buildVertexTangent(indices, v1, v2, v3, v4);

			FloatBuffer vertexBuffer = BufferUtil.createF(v1,v2,v3,v4);
			IntBuffer indiceBuffer = BufferUtil.createI(indices);
			Mod mod = new Mod();
			mod.add(new Mesh( "face", indices.length, new Material()));
			model = createModel(vertexBuffer, indiceBuffer, mod);
			modelCacheMap.put(planName, model);
		}
		return model;
	}

	public Model loadCube()
	{
		Model model = modelCacheMap.get(cubeName);
		if (model == null)
		{
			List<Vertex> vertexs = new ArrayList<Vertex>();
			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f),   new Vector2f(0, 0),  new Vector3f(0, 1, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, -0.5f),    new Vector2f(1, 0),  new Vector3f(0, 1, 0)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f),    new Vector2f(0, 1),  new Vector3f(0, 1, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, 0.5f),     new Vector2f(1, 1),  new Vector3f(0, 1, 0)));

			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f),    new Vector2f(0, 0),  new Vector3f(0, 0, 1)));
			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, 0.5f),     new Vector2f(1, 0),  new Vector3f(0, 0, 1)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f),   new Vector2f(0, 1),  new Vector3f(0, 0, 1)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, 0.5f),    new Vector2f(1, 1),  new Vector3f(0, 0, 1)));

			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f),   new Vector2f(0, 0),  new Vector3f(-1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f),    new Vector2f(1, 0),  new Vector3f(-1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f),  new Vector2f(0, 1),  new Vector3f(-1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f),   new Vector2f(1, 1),  new Vector3f(-1, 0, 0)));

			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, 0.5f),     new Vector2f(0, 0),  new Vector3f(1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, -0.5f),    new Vector2f(1, 0),  new Vector3f(1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, 0.5f),    new Vector2f(0, 1),  new Vector3f(1, 0, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, -0.5f),   new Vector2f(1, 1),  new Vector3f(1, 0, 0)));

			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f),   new Vector2f(0, 0),  new Vector3f(0, -1, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, 0.5f),    new Vector2f(1, 0),  new Vector3f(0, -1, 0)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f),  new Vector2f(0, 1),  new Vector3f(0, -1, 0)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, -0.5f),   new Vector2f(1, 1),  new Vector3f(0, -1, 0)));

			vertexs.add(new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f),  new Vector2f(0, 0),  new Vector3f(0, 0, -1)));
			vertexs.add(new Vertex(new Vector3f(0.5f, -0.5f, -0.5f),   new Vector2f(1, 0),  new Vector3f(0, 0, -1)));
			vertexs.add(new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f),   new Vector2f(0, 1),  new Vector3f(0, 0, -1)));
			vertexs.add(new Vertex(new Vector3f(0.5f, 0.5f, -0.5f),    new Vector2f(1, 1),  new Vector3f(0, 0, -1)));

			int[] indices = new int[]{
					0,2,1, 1,2,3,
					4,6,5, 5,6,7,
					8,10,9, 9,10,11,
					12,14,13, 13,14,15,
					16,18,17, 17,18,19,
					20,22,21, 21,22,23
			};

			Util.buildVertexTangent(indices, vertexs);

			FloatBuffer vertexBuffer = BufferUtil.createF(vertexs);
			IntBuffer indiceBuffer = BufferUtil.createI(indices);

			Mod mod = new Mod();
			mod.add(new Mesh("up", 6, new Material()));
			mod.add(new Mesh("front", 6, new Material()));
			mod.add(new Mesh("left", 6, new Material()));
			mod.add(new Mesh("right", 6, new Material()));
			mod.add(new Mesh("down", 6, new Material()));
			mod.add(new Mesh("back", 6, new Material()));
			model = createModel(vertexBuffer, indiceBuffer, mod);
			modelCacheMap.put(cubeName, model);
		}
		return model;
	}

	public Model loadOBJ(String src)
	{
		Model model = modelCacheMap.get(src);
		if (model == null)
		{
			List<Vertex> vertexs = new ArrayList<Vertex>();
			List<Vector2f> texcoords = new ArrayList<Vector2f>();
			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Integer> indices = new ArrayList<Integer>();
			List<Integer> indiceCountList = new ArrayList<Integer>();
			List<String> meshNameList = new ArrayList<String>();
			BufferedReader reader = null;
			String line;
			try
			{
				reader = new BufferedReader(new FileReader(src));
				int faceCount = 0;
				int vertexCount = 0;
				while ((line = reader.readLine()) != null)
				{
					//get position
					if (line.startsWith("v "))
					{
						Vector3f v = Util.formatVec3(line.replaceAll("v", "").trim());
						Vertex vertex = new Vertex(v, null, null);
						vertexs.add(vertex);
						vertexCount ++;
					}
					//get texcoord
					if (line.startsWith("vt "))
					{
						Vector2f v = Util.formatVec2(line.replaceAll("vt", "").trim());
						v.setY(1 - v.getY());
						texcoords.add(v);
					}
					//get normal
					if (line.startsWith("vn "))
					{
						Vector3f v = Util.formatVec3(line.replaceAll("vn", "").trim());
						normals.add(v);
					}
					// get face indice
					if (line.startsWith("f "))
					{
						Vector3f[] faces = Util.formatVec3s(line);
						for (Vector3f face : faces)
						{
							int vertexIndex = (int)face.x - 1;
							int texcoordIndex = (int)face.y - 1;
							int normalIndex = (int)face.z - 1;
							Vertex vertex = vertexs.get(vertexIndex);
							Vector2f texcoord = new Vector2f(0,0);
							if (texcoordIndex != -1)
							{
								texcoord = texcoords.get(texcoordIndex);
							}
							Vector3f normal = normals.get(normalIndex);
							if (vertex.getTexcoord() == null && vertex.getNormal() == null)
							{
								vertex.setTexcoord(texcoord);
								vertex.setNormal(normal);
								indices.add(vertexIndex);
							}
							else
							{
								if (vertex.getTexcoord().equals(texcoord) && vertex.getNormal().equals(normal))
								{
									indices.add(vertexIndex);
								}
								else
								{
									vertexCount ++;
									vertex = new Vertex(vertex.getPosition(), texcoord, normal);
									vertexs.add(vertex);
									indices.add(vertexCount - 1);
								}
							}
						}
						faceCount += faces.length;
					}

					if (line.startsWith("usemtl "))
					{
						if (faceCount != 0)
						{
							indiceCountList.add(faceCount);
						}
						String materialName = Util.formatString(line);
						meshNameList.add(materialName);
						faceCount = 0;
					}
				}
				reader.close();
				indiceCountList.add(faceCount);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			Util.buildVertexTangent(indices, vertexs);
			FloatBuffer vertexBuffer = BufferUtil.createF(vertexs);
			IntBuffer indiceBuffer = BufferUtil.createI(indices);

			if (indiceCountList.size() == 1 && meshNameList.size() == 0)
			{
				meshNameList.add("face");
			}

			Mod mod = new Mod();
			for (int i = 0; i < indiceCountList.size(); i++)
			{
				int count = indiceCountList.get(i);
				String name = meshNameList.get(i);
				mod.add(new Mesh( name, count, new Material()));
			}
			model = createModel(vertexBuffer, indiceBuffer, mod);
			modelCacheMap.put(src, model);
		}
		return model;
	}

	public int loadTexture(String src, boolean alphed)
	{
		Integer texture = textureCacheMap.get(src);
		if (textureCacheMap.get(src) == null)
		{
			texture = OGL.createTexture2D(FileUtil.readImage(src), alphed, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR);
			textureCacheMap.put(src, texture);
		}
		return texture;
	}

	public Model createModel(FloatBuffer vertexBuffer, IntBuffer indiceBuffer, Mod mod)
	{
		int vao = OGL.createVertexArray();
		GL30.glBindVertexArray(vao);
		int vbo = OGL.createArrayBuffer(vertexBuffer, GL15.GL_STATIC_DRAW, 0, 3,2,3,3);
		GL30.glBindVertexArray(0);
		int ebo = OGL.createElementBuffer(indiceBuffer, GL15.GL_STATIC_DRAW);
		return new Model(vao, ebo, mod);
	}
}
