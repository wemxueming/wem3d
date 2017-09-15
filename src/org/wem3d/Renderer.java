package org.wem3d;
import org.lwjgl.opengl.*;
import org.wem3d.util.BufferUtil;
import org.wem3d.util.MathUtil;
import org.wem3d.util.OGL;

import java.util.Map;

public class Renderer
{
	private int modelProgram;
	private int modelProgram_cameraPos;
	private int modelProgram_albedoMap;
	private int modelProgram_metallicMap;
	private int modelProgram_roughnessMap;
	private int modelProgram_aoMap;
	private int modelProgram_normalMap;
	private int modelProgram_heightMap;

	private int frameProgram;

	private int modelBorderProgram;

	private int skyboxProgram;

	private int cameraUbo;
	private int transformUbo;
	private int lightsUbo;
	private int materialUbo;

	private int mainFrame;
	private int mainFrameTextureBuffer;
	private int mainFrameRenderBuffer;
	private int mainFrameVertexArray;
	private int mainFrameElementBuffer;
	private int mainFrameElementCount;

	private Scene scene;
	private Window window;
	private Camera camera;

	public Renderer(Scene scene, Window window, Camera camera)
	{
		this.scene = scene;
		this.window = window;
		this.camera = camera;
		initPrograms();
		initUbos();
		initFrames();
	}

	protected void initPrograms()
	{
		modelProgram = OGL.createProgram();
		int vert = OGL.createShader("src/org/wem3d/glsl/model.vert", GL20.GL_VERTEX_SHADER);
		int frag = OGL.createShader("src/org/wem3d/glsl/model.frag", GL20.GL_FRAGMENT_SHADER);
		OGL.buildProgram(modelProgram, vert, frag);
		modelProgram_cameraPos = GL20.glGetUniformLocation(modelProgram, "cameraPos");
		modelProgram_albedoMap = GL20.glGetUniformLocation(modelProgram, "albedoMap");
		modelProgram_metallicMap = GL20.glGetUniformLocation(modelProgram, "metallicMap");
		modelProgram_roughnessMap = GL20.glGetUniformLocation(modelProgram, "roughnessMap");
		modelProgram_aoMap = GL20.glGetUniformLocation(modelProgram, "aoMap");
		modelProgram_normalMap = GL20.glGetUniformLocation(modelProgram, "normalMap");
		modelProgram_heightMap = GL20.glGetUniformLocation(modelProgram, "heightMap");

		modelBorderProgram = OGL.createProgram();
		vert = OGL.createShader("src/org/wem3d/glsl/border.vert", GL20.GL_VERTEX_SHADER);
		frag = OGL.createShader("src/org/wem3d/glsl/border.frag", GL20.GL_FRAGMENT_SHADER);
		OGL.buildProgram(modelBorderProgram, vert, frag);

		skyboxProgram = OGL.createProgram();
		vert = OGL.createShader("src/org/wem3d/glsl/skybox.vert", GL20.GL_VERTEX_SHADER);
		frag = OGL.createShader("src/org/wem3d/glsl/skybox.frag", GL20.GL_FRAGMENT_SHADER);
		OGL.buildProgram(skyboxProgram, vert, frag);

		frameProgram = OGL.createProgram();
		vert = OGL.createShader("src/org/wem3d/glsl/frame.vert", GL20.GL_VERTEX_SHADER);
		frag = OGL.createShader("src/org/wem3d/glsl/frame.frag", GL20.GL_FRAGMENT_SHADER);
		OGL.buildProgram(frameProgram, vert, frag);
	}

	protected void initUbos()
	{
		cameraUbo = OGL.createUniformBuffer(64 * 2, 0, GL15.GL_DYNAMIC_DRAW);
		transformUbo = OGL.createUniformBuffer(64, 1, GL15.GL_DYNAMIC_DRAW);
		lightsUbo = OGL.createUniformBuffer(16 + 10 * 64, 2, GL15.GL_DYNAMIC_DRAW);
		materialUbo = OGL.createUniformBuffer(64, 3, GL15.GL_DYNAMIC_DRAW);
	}

	protected void initFrames()
	{
		float[] mainFrameVertexs = {
				-1,1,0,1,
				1,1,1,1,
				-1,-1,0,0,
				1,-1,1,0,
		};
		int[] mainFrameElements = {0,2,1,1,2,3};
		mainFrameVertexArray = OGL.createVertexArray();
		GL30.glBindVertexArray(mainFrameVertexArray);
		OGL.createArrayBuffer(BufferUtil.createF(mainFrameVertexs), GL15.GL_STATIC_DRAW, 0, 2,2);
		GL30.glBindVertexArray(0);
		mainFrameElementCount = mainFrameElements.length;
		mainFrameElementBuffer = OGL.createElementBuffer(BufferUtil.createI(mainFrameElements), GL15.GL_STATIC_DRAW);
		mainFrameTextureBuffer = OGL.createFrameTexture(false, false, window.getWidth(),window.getHeight());
		mainFrameRenderBuffer = OGL.createRenderBuffer(window.getWidth(),window.getHeight());
		mainFrame = OGL.createFrameBuffer(mainFrameTextureBuffer, mainFrameRenderBuffer);
	}

	public void update()
	{
		updateCameraUbo();
		updateLightsUbo();

		//写入帧缓冲
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainFrame);

		//清楚颜色，深度，模板,并开启深度测试
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		//剔除背面
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		//将模型写入模板缓存
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
		GL11.glStencilMask(0xFF);
		GL11.glStencilOp(GL11.GL_KEEP,GL11.GL_KEEP,GL11.GL_REPLACE);
		GL11.glEnable(GL11.GL_STENCIL_TEST);

		//绘制模型
		updateModelProgram();

		//关闭写入模板缓存和深度测试
		GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
		GL11.glStencilMask(0x00);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		//绘制模型边框
		updateModelBorderProgram();

		//关闭帧缓冲
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

		//关闭模板缓存
		GL11.glStencilMask(0xFF);
		GL11.glDisable(GL11.GL_STENCIL_TEST);

		//清除颜色和关闭深度测试
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		updateFrameProgram();

		//关闭背面剔除
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	protected void updateSkyBoxProgram()
	{
		for (Skybox skybox : scene.getSkyboxes())
		{
			GL11.glDepthMask(false);
			GL20.glUseProgram(skyboxProgram);
			OGL.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getCubeMap(), 0);
			GL30.glBindVertexArray(skybox.getVao());
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, skybox.getEbo());
			GL11.glDrawElements(GL11.GL_TRIANGLES, skybox.getCount(), GL11.GL_UNSIGNED_INT, 0);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			OGL.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getCubeMap());
			GL20.glUseProgram(0);
			GL11.glDepthMask(true);
		}
	}

	protected void updateFrameProgram()
	{
		GL20.glUseProgram(frameProgram);
		OGL.glBindTexture(GL11.GL_TEXTURE_2D, mainFrameTextureBuffer, 0);
		GL30.glBindVertexArray(mainFrameVertexArray);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mainFrameElementBuffer);
		GL11.glDrawElements(GL11.GL_TRIANGLES, mainFrameElementCount, GL11.GL_UNSIGNED_INT, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		OGL.glBindTexture(GL11.GL_TEXTURE_2D, mainFrameTextureBuffer);
		GL20.glUseProgram(0);
	}

	protected void updateCameraUbo()
	{
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUbo);
		GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, camera.projectionViewBuffer());
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}

	protected void updateLightsUbo()
	{
		Map<Integer, Light> lightMap = scene.getLightMap();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, lightsUbo);
		OGL.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, lightMap.size());
		int offset = 0;
		for (Light light : lightMap.values())
		{
			GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 16 + offset, light.buffer());
			offset += 64;
		}
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}

	protected void updateModelBorderProgram()
	{
		Map<Integer,Model> modelMap = scene.getModelMap();

		GL20.glUseProgram(modelBorderProgram);
		for (Model model : modelMap.values())
		{
			if (model.isActive())
			{
				GL30.glBindVertexArray(model.getVao());
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getEbo());

				for (Object3D object3D : model.getObject3DMap().values())
				{
					GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, transformUbo);
					GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, object3D.transformBuffer());
					GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);

					Mod mod = object3D.getMod();

					int offset = 0;
					for (Mesh mesh : mod.getMeshMap().values())
					{
						int count = mesh.getCount();
						GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, offset);
						offset += count * MathUtil.INT_BIT;
					}
				}

				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				GL30.glBindVertexArray(0);
			}
		}
		GL20.glUseProgram(0);
	}

	protected void updateModelProgram()
	{
		Map<Integer,Model> modelMap = scene.getModelMap();

		GL20.glUseProgram(modelProgram);
		OGL.glUniform3v(modelProgram_cameraPos, camera.transpose());
		GL20.glUniform1i(modelProgram_albedoMap, 0);
		GL20.glUniform1i(modelProgram_metallicMap, 1);
		GL20.glUniform1i(modelProgram_roughnessMap, 2);
		GL20.glUniform1i(modelProgram_aoMap, 3);
		GL20.glUniform1i(modelProgram_normalMap, 4);
		GL20.glUniform1i(modelProgram_heightMap, 5);

		for (Model model : modelMap.values())
		{
			if (model.isActive())
			{
				GL30.glBindVertexArray(model.getVao());
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getEbo());

				for (Object3D object3D : model.getObject3DMap().values())
				{
					GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, transformUbo);
					GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, object3D.transformBuffer());
					GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);

					Mod mod = object3D.getMod();

					int offset = 0;
					for (Mesh mesh : mod.getMeshMap().values())
					{
						int count = mesh.getCount();
						Material material = mesh.getMaterial();

						GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, materialUbo);
						GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, material.buffer());
						GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);

						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getAlbedoMap(), 0);
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getMetallicMap(), 1);
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getRoughnessMap(), 2);
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getAoMap(), 3);
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getNormalMap(), 4);
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getHeightMap(), 5);

						GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, offset);
						offset += count * MathUtil.INT_BIT;

						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getAlbedoMap());
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getMetallicMap());
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getRoughnessMap());
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getAoMap());
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getNormalMap());
						OGL.glBindTexture(GL11.GL_TEXTURE_2D, material.getHeightMap());
					}
				}

				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				GL30.glBindVertexArray(0);
			}
		}
		GL20.glUseProgram(0);
	}

	public void destroy()
	{
		OGL.destroy();
	}

}
