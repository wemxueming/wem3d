package org.wem3d;


import org.wem3d.util.Util;

public class Timer
{
	private long lastDeltaTime;
	private long lastFrameTime;
	private long time;
	private int frame;
	private int delta;
	private int fps;

	public Timer()
	{
		lastDeltaTime = Util.getTime();
		lastFrameTime = Util.getTime();
	}

	public void update()
	{
		time = Util.getTime();
		delta = (int) (time - lastDeltaTime);
		lastDeltaTime = time;
		if (time - lastFrameTime > 1000)
		{
			fps = frame;
			frame = 0;
			lastFrameTime += 1000;
		}
		frame++;
	}

	public int delta()
	{
		return delta;
	}

	public int fps()
	{
		return fps;
	}
}
