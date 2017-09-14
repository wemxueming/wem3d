package org.wem3d;

import org.lwjgl.BufferUtils;
import org.math3d.Vector3f;
import org.wem3d.util.Util;
import java.nio.FloatBuffer;
import java.util.Map;

public abstract class Light
{
    protected static FloatBuffer LIGHT_BUFFER = BufferUtils.createFloatBuffer(16);
    protected static Map<Float, Vector3f> ATTENUATION = Util.buildAttenuationMap();
    private static int ID = 0;
    private int id;
    protected Vector3f color;
    protected float brightness;
    protected boolean updated;

    public Light()
    {
        id = ID ++;
        color = new Vector3f(1);
        brightness = 10f;
        updated = true;
    }

    public int id()
    {
        return id;
    }

    public abstract FloatBuffer buffer();

    public boolean isUpdated()
    {
        return updated;
    }

    public Vector3f getColor()
    {
        return color;
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
    }

    public float getBrightness()
    {
        return brightness;
    }

    public void setBrightness(float brightness)
    {
        this.brightness = brightness;
    }
}
