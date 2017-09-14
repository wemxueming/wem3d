package org.wem3d.light;

import org.math3d.Vector3f;
import org.wem3d.util.MathUtil;
import java.nio.FloatBuffer;

public class SpotLight extends PointLight
{
    private Vector3f direction;
    private float cutoff;
    private float outcutoff;

    public SpotLight(Vector3f position, Vector3f direction, float distance, float cutoff, float outcutoff)
    {
        super(position, distance);
        this.direction = direction;
        this.cutoff = cutoff;
        this.outcutoff = outcutoff;
    }

    public SpotLight()
    {
        this(new Vector3f(0,5, 0), new Vector3f(0,-1,0), 7f, MathUtil.toSpot(45f), MathUtil.toSpot(50f));
    }

    @Override
    public FloatBuffer buffer()
    {
        LIGHT_BUFFER.clear();
        LIGHT_BUFFER.put(color.x);
        LIGHT_BUFFER.put(color.y);
        LIGHT_BUFFER.put(color.z);
        LIGHT_BUFFER.put(brightness);

        LIGHT_BUFFER.put(position.x);
        LIGHT_BUFFER.put(position.y);
        LIGHT_BUFFER.put(position.z);
        LIGHT_BUFFER.put(cutoff);

        LIGHT_BUFFER.put(direction.x);
        LIGHT_BUFFER.put(direction.y);
        LIGHT_BUFFER.put(direction.z);
        LIGHT_BUFFER.put(outcutoff);

        LIGHT_BUFFER.put(attenuation.x);
        LIGHT_BUFFER.put(attenuation.y);
        LIGHT_BUFFER.put(attenuation.z);
        LIGHT_BUFFER.put(0f);
        LIGHT_BUFFER.flip();
        updated = false;
        return LIGHT_BUFFER;
    }

    public Vector3f getDirection()
    {
        return direction;
    }

    public void setDirection(Vector3f direction)
    {
        this.direction = direction;
    }

    public float getCutoff()
    {
        return cutoff;
    }

    public void setCutoff(float cutoff)
    {
        this.cutoff = cutoff;
    }

    public float getOutcutoff()
    {
        return outcutoff;
    }

    public void setOutcutoff(float outcutoff)
    {
        this.outcutoff = outcutoff;
    }

}
