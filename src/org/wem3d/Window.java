package org.wem3d;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class Window
{
	public static final boolean defaultFullScreen = false;
	public static final boolean defaultSyncFps = true;
	public static final int defaultMaxFps = 100;

	public static final int openglMajorVersion = 4;
	public static final int openglMinorVersion = 4;

	private String title;
	private int width;
	private int height;
	private boolean fullScreen;
	private boolean syncFps;
	private int maxFps;

	public Window(String title, int width, int height, boolean fullScreen, boolean syncFps, int maxFps)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullScreen = fullScreen;
		this.syncFps = syncFps;
		this.maxFps = maxFps;
		init();
	}

	public Window(String title, int width, int height)
	{
		this(title, width, height, defaultFullScreen, defaultSyncFps, defaultMaxFps);
	}

	public void update()
	{
		if (syncFps)
		{
			Display.sync(maxFps);
		}
		Display.update();
	}

	public void destroy()
	{
		Display.destroy();
	}

	public boolean isClosed()
	{
		return Display.isCloseRequested();
	}

	protected void init()
	{
		Display.setTitle(title);
		setMode(width, height, fullScreen);
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(openglMajorVersion, openglMinorVersion).withForwardCompatible(true).withProfileCore(true);
		try
		{
			Display.create(pixelFormat, contextAttribs);
		}
		catch (LWJGLException e)
		{
			System.out.println("error! create display make a wrong!");
			e.printStackTrace();
			System.exit(0);
		}
		GL11.glViewport(0, 0, width, height);
	}

	protected void setMode(int width, int height, boolean fullScreen)
	{
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullScreen))
		{
			return;
		}
		try
		{
			DisplayMode targetDisplayMode = null;

			if (fullScreen)
			{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++)
				{
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height))
					{
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
						{
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
							{
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
						{
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else
			{
				targetDisplayMode = new DisplayMode(width, height);
			}
			if (targetDisplayMode == null)
			{
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullScreen);
				return;
			}
			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullScreen);

		} catch (LWJGLException e)
		{
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullScreen + e);
		}
	}

	public String getTitle()
	{
		return title;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean isFullScreen()
	{
		return fullScreen;
	}

	public boolean isSyncFps()
	{
		return syncFps;
	}

	public int getMaxFps()
	{
		return maxFps;
	}
}
