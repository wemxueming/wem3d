package org.wem3d;
import org.lwjgl.BufferUtils;
import org.math3d.Matrix4f;
import org.math3d.Vector3f;
import org.wem3d.util.BufferUtil;
import org.wem3d.util.MathUtil;

import java.nio.FloatBuffer;

public class Object3D
{
	private static FloatBuffer transformBuffer = BufferUtils.createFloatBuffer(16);
	private static Matrix4f transformMatrix = new Matrix4f();
	private static int ID = 0;
	private int id;

	private Vector3f position;
	private Vector3f rotate;
	private Vector3f scale;
	private boolean active;
	private Model model;
	private Mod mod;

	public Object3D(Vector3f position, Vector3f rotate, Vector3f scale, boolean active)
	{
		this.position = position;
		this.rotate = rotate;
		this.scale = scale;
		this.active = active;
		id = ID ++;
	}

	public Object3D()
	{
		this(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), true);
	}

	public FloatBuffer transformBuffer()
	{
		transformMatrix.setIdentity();
		Matrix4f.scale(scale, transformMatrix, transformMatrix);
		Matrix4f.translate(position, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.z), MathUtil.Z_AXIS, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.y), MathUtil.Y_AXIS, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.x), MathUtil.X_AXIS, transformMatrix, transformMatrix);
		BufferUtil.update(transformBuffer, transformMatrix);
		return transformBuffer;
	}

	public int id()
	{
		return id;
	}

	public Model getModel()
	{
		return model;
	}

	public void setModel(Model model)
	{
		this.model = model;
		model.getObject3DMap().put(id(), this);
		mod = model.getDefaultMod();
	}

	public Mod getMod()
	{
		return mod;
	}

	public void setMod(String name)
	{
		this.mod = model.getModMap().get(name);
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public Vector3f getRotate()
	{
		return rotate;
	}

	public void setRotate(Vector3f rotate)
	{
		this.rotate = rotate;
	}

	public Vector3f getScale()
	{
		return scale;
	}

	public void setScale(Vector3f scale)
	{
		this.scale = scale;
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
