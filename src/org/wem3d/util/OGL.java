package org.wem3d.util;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.math3d.Vector4f;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public final class OGL
{
	private static Map<Integer, Integer> bufferMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> frameBufferMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> programMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> renderBufferMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> textureMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> vaoMap = new HashMap<Integer, Integer>();

	private static IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
	private static FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(1);
	private static FloatBuffer vec2Buffer = BufferUtils.createFloatBuffer(2);
	private static FloatBuffer vec3Buffer = BufferUtils.createFloatBuffer(3);
	private static FloatBuffer vec4Buffer = BufferUtils.createFloatBuffer(4);

	//program fun
	//==========================================================================================================
	public final static int createProgram()
	{
		int program = GL20.glCreateProgram();
		programMap.put(program, program);
		return program;
	}

	public final static int createShader(String src, int type)
	{
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, readShaderFile(src));
		GL20.glCompileShader(shader);
		return shader;
	}

	public final static void buildProgram(int program, int...shaders)
	{
		for (int shader : shaders)
		{
			GL20.glAttachShader(program, shader);
		}
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		for (int shader : shaders)
		{
			GL20.glDetachShader(program, shader);
			GL20.glDeleteShader(shader);
		}
	}

	public final static void glUniform1f(int location, boolean b)
	{
		if (b)
			GL20.glUniform1i(location, 1);
		else
			GL20.glUniform1i(location, 0);
	}

	public final static void glUniform2v(int location, Vector2f vec2)
	{
		GL20.glUniform2f(location, vec2.x, vec2.y);
	}

	public final static void glUniform3v(int location, Vector3f vec3)
	{
		GL20.glUniform3f(location, vec3.x, vec3.y, vec3.z);
	}

	public final static void glUniform4v(int location, Vector4f vec4)
	{
		GL20.glUniform4f(location, vec4.x, vec4.y, vec4.z, vec4.w);
	}
	//==========================================================================================================

	//buffer fun
	//==========================================================================================================
	public final static int createBuffer()
	{
		int buffer = GL15.glGenBuffers();
		bufferMap.put(buffer, buffer);
		return buffer;
	}

	public final static int createElementBuffer(IntBuffer buf, int mode)
	{
		int buffer = createBuffer();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, mode);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		return buffer;
	}

	public final static int createArrayBuffer(FloatBuffer buf, int mode, int start, int...formats)
	{
		int stride = MathUtil.toTotal(formats) * MathUtil.FLOAT_BIT;
		int buffer = createBuffer();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, mode);
		glVertexAttribPointer(formats, stride, start);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return buffer;
	}

	public final static int createUniformBuffer(FloatBuffer buf, int binding, int mode)
	{
		int buffer = createBuffer();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, buffer);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, buf, mode);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, binding, buffer);
		return buffer;
	}

	public final static int createUniformBuffer(int size, int binding, int mode)
	{
		int buffer = createBuffer();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, buffer);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, size, mode);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, binding, buffer);
		return buffer;
	}

	public final static void glBufferSubData(int target, int location, int i)
	{
		intBuffer.clear();
		intBuffer.put(i);
		intBuffer.flip();
		GL15.glBufferSubData(target, location, intBuffer);
	}

	public final static void glBufferSubData(int target, int location, float f)
	{
		floatBuffer.clear();
		floatBuffer.put(f);
		floatBuffer.flip();
		GL15.glBufferSubData(target, location, floatBuffer);
	}

	public final static void glBufferSubData(int target, int location, boolean b)
	{
		if (b)
			glBufferSubData(target, location, 1);
		else
			glBufferSubData(target, location, 0);
	}

	public final static void glBufferSubData(int target, int location, Vector2f vec2)
	{
		vec2Buffer.clear();
		vec2Buffer.put(vec2.x);
		vec2Buffer.put(vec2.y);
		vec2Buffer.flip();
		GL15.glBufferSubData(target, location, vec2Buffer);
	}

	public final static void glBufferSubData(int target, int location, Vector3f vec3)
	{
		vec3Buffer.clear();
		vec3Buffer.put(vec3.x);
		vec3Buffer.put(vec3.y);
		vec3Buffer.put(vec3.z);
		vec3Buffer.flip();
		GL15.glBufferSubData(target, location, vec3Buffer);
	}

	public final static void glBufferSubData(int target, int location, Vector4f vec4)
	{
		vec4Buffer.clear();
		vec4Buffer.put(vec4.x);
		vec4Buffer.put(vec4.y);
		vec4Buffer.put(vec4.z);
		vec4Buffer.put(vec4.w);
		vec4Buffer.flip();
		GL15.glBufferSubData(target, location, vec4Buffer);
	}
	//==========================================================================================================

	//texture fun
	//==========================================================================================================
	public final static int createTexture()
	{
		int texture = GL11.glGenTextures();
		textureMap.put(texture, texture);
		return texture;
	}

	public final static int createFrameTexture(boolean depth, boolean stencil, int screenWidth, int screenHeight)
	{
		int attachmentType = 0;
		if(!depth && !stencil)
		{
			attachmentType = GL11.GL_RGB;
		}
		else if(depth && !stencil)
		{
			attachmentType = GL11.GL_DEPTH_COMPONENT;
		}
		else if(!depth && stencil)
		{
			attachmentType = GL11.GL_STENCIL_INDEX;
		}
		int textureColorbuffer = createTexture();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureColorbuffer);
		if(!depth && !stencil)
		{
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, attachmentType, screenWidth, screenHeight, 0, attachmentType, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		}
		else // Using both a stencil and depth test, needs special format arguments
		{
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH24_STENCIL8, screenWidth, screenHeight, 0, GL30.GL_DEPTH_STENCIL, GL30.GL_UNSIGNED_INT_24_8, (java.nio.ByteBuffer) null);
		}
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureColorbuffer;
	}

	public final static int createTexture2D(BufferedImage image, boolean alphed, int wrapS, int wrapT, int minFilter, int magFilter)
	{
		int texture = createTexture();
		int colorType = GL11.GL_RGB;
		if (alphed)
		{
			colorType = GL11.GL_RGBA;
		}
		ByteBuffer buffer = BufferUtil.createB(image, alphed);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapS);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, colorType, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return texture;
	}

	public final static void glBindTexture(int target, int texture, int level)
	{
		if (texture > 0)
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + level);
			GL11.glBindTexture(target, texture);
		}
	}

	public final static void glBindTexture(int target, int texture)
	{
		if (texture > 0)
		{
			GL11.glBindTexture(target, 0);
		}
	}
	//==========================================================================================================

	//renderBuffer fun
	//==========================================================================================================
	public final static int createRenderBuffer()
	{
		int renderBuffer = GL30.glGenRenderbuffers();
		renderBufferMap.put(renderBuffer, renderBuffer);
		return renderBuffer;
	}

	public final static int createRenderBuffer(int screenWidth, int screenHeight)
	{
		int renderBuffer = createRenderBuffer();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, screenWidth, screenHeight); // Use a single renderbuffer object for both a depth AND stencil buffer.
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		return renderBuffer;
	}
	//==========================================================================================================

	//vertexArray fun
	//==========================================================================================================
	public final static int createVertexArray()
	{
		int vao = GL30.glGenVertexArrays();
		vaoMap.put(vao, vao);
		return vao;
	}

	public final static void glVertexAttribPointer(int[] formats, int stride, int start)
	{
		int offset = 0;
		int length = formats.length + start;
		for (int i = start; i < length; i++)
		{
			int size = formats[i - start];
			GL20.glEnableVertexAttribArray(i);
			GL20.glVertexAttribPointer(i, size, GL11.GL_FLOAT, false, stride, offset);
			offset += size * MathUtil.FLOAT_BIT;
		}
	}

	public final static void glVertexAttribDivisor(int[] formats, int divisor, int start)
	{
		int length = formats.length + start;
		for (int i = start; i < length; i++)
		{
			GL33.glVertexAttribDivisor(i, divisor);
		}
	}
	//==========================================================================================================

	//frameBuffer fun
	//==========================================================================================================
	public final static int createFrameBuffer()
	{
		int frameBuffer = GL30.glGenFramebuffers();
		frameBufferMap.put(frameBuffer, frameBuffer);
		return frameBuffer;
	}

	public final static int createFrameBuffer(int frameColorTexture, int frameRenderBuffer)
	{
		int frame = createFrameBuffer();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frame);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, frameColorTexture, 0);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, frameRenderBuffer); // Now actually attach it
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		return frame;
	}
	//==========================================================================================================

	public final static CharSequence readShaderFile(String src)
	{
		BufferedReader reader = null;
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try
		{
			reader = new BufferedReader(new FileReader(src));
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("#include "))
				{
					String subFileSrc = line.split("\\s+")[1].trim();
					CharSequence subFile = FileUtil.readString(subFileSrc);
					stringBuilder.append(subFile).append("\n");
				}
				else
				{
					stringBuilder.append(line).append("\n");
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return stringBuilder;
	}

	public final static void destroy()
	{
		for (int buffer : bufferMap.values())
		{
			GL15.glDeleteBuffers(buffer);
		}
		for (int frameBuffer : frameBufferMap.values())
		{
			GL30.glDeleteFramebuffers(frameBuffer);
		}
		for (int program : programMap.values())
		{
			GL20.glDeleteProgram(program);
		}
		for (int renderBuffer : renderBufferMap.values())
		{
			GL30.glDeleteRenderbuffers(renderBuffer);
		}
		for (int texture : textureMap.values())
		{
			GL11.glDeleteTextures(texture);
		}
		for(int vao : vaoMap.values())
		{
			GL30.glDeleteVertexArrays(vao);
		}
	}

}
