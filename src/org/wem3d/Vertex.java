package org.wem3d;

import org.math3d.Vector2f;
import org.math3d.Vector3f;

/**
 * Created by wem on 2017/5/14.
 */
public class Vertex
{
    protected Vector3f position;
    protected Vector2f texcoord;
    protected Vector3f normal;
    protected Vector3f tangent;
    protected Vector3f bitangent;

    public Vertex(Vector3f position, Vector2f texcoord, Vector3f normal)
    {
        this.position = position;
        this.texcoord = texcoord;
        this.normal = normal;
        tangent = new Vector3f();
        bitangent = new Vector3f();
    }

    public Vertex()
    {
        position = new Vector3f();
        texcoord = new Vector2f();
        normal = new Vector3f();
        tangent = new Vector3f();
	    bitangent = new Vector3f();
    }

    public float[] getElements()
    {
        return new float[]
        {
           position.x, position.y, position.z,
           texcoord.x, texcoord.y,
           normal.x, normal.y, normal.z,
		   tangent.x, tangent.y, tangent.z,
        };
    }

    public void setPosition(float x, float y, float z)
    {
        position.set(x, y, z);
    }

    public void setTexcoord(float u, float v)
    {
        texcoord.set(u, v);
    }

    public void setNormal(float x, float y, float z)
    {
        normal.set(x, y, z);
    }

    public void setTangent(float x, float y, float z)
    {
        tangent.set(x, y, z);
    }

	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public Vector2f getTexcoord()
	{
		return texcoord;
	}

	public void setTexcoord(Vector2f texcoord)
	{
		this.texcoord = texcoord;
	}

	public Vector3f getNormal()
	{
		return normal;
	}

	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}

	public Vector3f getTangent()
	{
		return tangent;
	}

	public void setTangent(Vector3f tangent)
	{
		this.tangent = tangent;
	}

	public Vector3f getBitangent()
	{
		return bitangent;
	}

	public void setBitangent(Vector3f bitangent)
	{
		this.bitangent = bitangent;
	}
}
