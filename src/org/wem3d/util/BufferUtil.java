package org.wem3d.util;

import org.lwjgl.BufferUtils;
import org.math3d.Matrix4f;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.math3d.Vector4f;
import org.wem3d.Vertex;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public final class BufferUtil
{
	private static IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
	private static FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(1);
	private static FloatBuffer vec2Buffer = BufferUtils.createFloatBuffer(2);
	private static FloatBuffer vec3Buffer = BufferUtils.createFloatBuffer(3);
	private static FloatBuffer vec4Buffer = BufferUtils.createFloatBuffer(4);

    public final static IntBuffer createI(int[] target)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(target.length);
        buffer.put(target);
        buffer.flip();
        return buffer;
    }

    public final static IntBuffer createI(List<Integer> target)
    {
    	IntBuffer buffer = BufferUtils.createIntBuffer(target.size());
    	for (int i : target)
	    {
	    	buffer.put(i);
	    }
	    buffer.flip();
    	return buffer;
    }

    public final static FloatBuffer createF(float[] target)
    {
    	FloatBuffer buffer = BufferUtils.createFloatBuffer(target.length);
    	for (float f : target)
	    {
	    	buffer.put(f);
	    }
	    buffer.flip();
    	return buffer;
    }

	public final static FloatBuffer createF(Vertex...vertexs)
	{
		int modelVertexLength = 11;
		float[] f = new float[vertexs.length * modelVertexLength];
		int index = 0;
		for (Vertex v : vertexs)
		{
			for (float element : v.getElements())
			{
				f[index] = element;
				index ++;
			}
		}
		FloatBuffer buffer = BufferUtils.createFloatBuffer(f.length);
		buffer.put(f);
		buffer.flip();
		return buffer;
	}

	public final static FloatBuffer createF(List<Vertex> vertexs)
	{
		int modelVertexLength = 11;
		float[] f = new float[vertexs.size() * modelVertexLength];
		int index = 0;
		for (Vertex v : vertexs)
		{
			for (float element : v.getElements())
			{
				f[index] = element;
				index ++;
			}
		}
		FloatBuffer buffer = BufferUtils.createFloatBuffer(f.length);
		buffer.put(f);
		buffer.flip();
		return buffer;
	}

    public final static ByteBuffer createB(BufferedImage target, boolean alphed)
    {
        int[] pixels = new int[target.getWidth() * target.getHeight()];
        target.getRGB(0, 0, target.getWidth(), target.getHeight(), pixels, 0, target.getWidth());
        ByteBuffer buffer;
        if (alphed)
        {
            buffer = BufferUtils.createByteBuffer(target.getWidth() * target.getHeight() * 4);
        }
        else
        {
            buffer = BufferUtils.createByteBuffer(target.getWidth() * target.getHeight() * 3);
        }

        for(int y = 0; y < target.getHeight(); y++)
        {
            for(int x = 0; x < target.getWidth(); x++)
            {
                int pixel = pixels[y * target.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                if (alphed)
                {
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }
        }
        buffer.flip();
        return buffer;
    }

    public final static void update(FloatBuffer buffer, Matrix4f...matrix4fs)
    {
    	buffer.clear();
    	for (Matrix4f matrix4f : matrix4fs)
	    {
	    	matrix4f.store(buffer);
	    }
	    buffer.flip();
    }

    public final static void update(IntBuffer buffer, int...ints)
    {
    	buffer.clear();
    	for (int i : ints)
	    {
	    	buffer.put(i);
	    }
	    buffer.flip();
    }

	public final static void update(FloatBuffer buffer, float...floats)
	{
		buffer.clear();
		for (float f : floats)
		{
			buffer.put(f);
		}
		buffer.flip();
	}

    public final static IntBuffer build(int i)
    {
    	intBuffer.clear();
    	intBuffer.put(i);
    	intBuffer.flip();
    	return intBuffer;
    }

    public final static FloatBuffer build(float f)
    {
    	floatBuffer.clear();
    	floatBuffer.put(f);
    	floatBuffer.flip();
    	return floatBuffer;
    }

    public final static IntBuffer build(boolean b)
    {
    	if (b)
    		return build(1);
    	else
    		return build(0);
    }

	public final static FloatBuffer build(Vector2f vec2)
	{
		vec2Buffer.clear();
		vec2Buffer.put(vec2.x);
		vec2Buffer.put(vec2.y);
		vec2Buffer.flip();
		return vec2Buffer;
	}

	public final static FloatBuffer build(Vector3f vec3)
	{
		vec3Buffer.clear();
		vec3Buffer.put(vec3.x);
		vec3Buffer.put(vec3.y);
		vec3Buffer.put(vec3.z);
		vec3Buffer.flip();
		return vec3Buffer;
	}

    public final static FloatBuffer build(Vector4f vec4)
    {
    	vec4Buffer.clear();
    	vec4Buffer.put(vec4.x);
    	vec4Buffer.put(vec4.y);
    	vec4Buffer.put(vec4.z);
    	vec4Buffer.put(vec4.w);
    	vec4Buffer.flip();
    	return vec4Buffer;
    }
}
