package org.wem3d;

import org.lwjgl.BufferUtils;
import org.math3d.Matrix4f;
import org.math3d.Vector3f;
import org.wem3d.util.BufferUtil;
import org.wem3d.util.MathUtil;

import java.nio.FloatBuffer;

public class Camera
{
    private static FloatBuffer cameraBuffer = BufferUtils.createFloatBuffer(32);
    private static Matrix4f projectionMatrix = new Matrix4f();
    private static Matrix4f viewMatrix = new Matrix4f();
    private int width;
    private int height;
    private float fov;
    private float near;
    private float far;
    private Vector3f transposePosition;
    private Vector3f position;
    private Vector3f rotate;

    public Camera(int width, int height, float fov, float near, float far, Vector3f position, Vector3f rotate)
    {
        this.width = width;
        this.height = height;
        this.fov = fov;
        this.near = near;
        this.far = far;
        this.position = position;
        this.rotate = rotate;
        transposePosition = new Vector3f();
    }

    public FloatBuffer projectionViewBuffer()
    {
        float aspectRatio = (float) width / (float) height;
        float yScale = MathUtil.coTangent(MathUtil.toRadians(fov / 2f));
        float xScale = yScale / aspectRatio;
        float frustumLength = far - near;
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((far + near) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near * far) / frustumLength);
        projectionMatrix.m33 = 0;

        viewMatrix.setIdentity();
        Matrix4f.rotate(MathUtil.toRadians(rotate.x), MathUtil.X_AXIS, viewMatrix, viewMatrix);
        Matrix4f.rotate(MathUtil.toRadians(rotate.y), MathUtil.Y_AXIS, viewMatrix, viewMatrix);
        Matrix4f.rotate(MathUtil.toRadians(rotate.z), MathUtil.Z_AXIS, viewMatrix, viewMatrix);
        Matrix4f.translate(position, viewMatrix, viewMatrix);

        BufferUtil.update(cameraBuffer, projectionMatrix, viewMatrix);
        return cameraBuffer;
    }

    public Vector3f transpose()
    {
		transposePosition.set(-position.x, -position.y, -position.z);
		return transposePosition;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public float getFov()
    {
        return fov;
    }

    public void setFov(float fov)
    {
        this.fov = fov;
    }

    public float getNear()
    {
        return near;
    }

    public void setNear(float near)
    {
        this.near = near;
    }

    public float getFar()
    {
        return far;
    }

    public void setFar(float far)
    {
        this.far = far;
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
}
