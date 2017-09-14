package org.wem3d;
import org.lwjgl.input.Keyboard;

import java.util.Map;

public class Controller
{
	private Scene scene;
	private Timer timer;

	public Controller(Scene scene, Timer timer)
	{
		this.scene = scene;
		this.timer = timer;
	}

	public void update()
	{
		Map<Integer, Control> controlMap = scene.getControlMap();
		if (Keyboard.next())
		{
			for (Control control : controlMap.values())
			{
				if (control.isActive())
				{
					control.nextControl(timer.delta());
				}
			}
		}
		for (Control control : controlMap.values())
		{
			if (control.isActive())
			{
				control.control(timer.delta());
			}
		}
	}





}
