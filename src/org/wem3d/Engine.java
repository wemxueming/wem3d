package org.wem3d;
import org.math3d.Vector3f;

public class Engine
{
	public static final String defaultEngineName = "wem3d engine";
	public static final int defaultEngineWidth = 800;
	public static final int defaultEngineHeight = 600;

	public static final float defaultCameraFov = 60f;
	public static final float defaultCameraNear = 0.1f;
	public static final float defaultCameraFar = 1000f;
	public static final Vector3f defaultCameraPosition = new Vector3f(0,0,-1);
	public static final Vector3f defaultCameraRotate = new Vector3f(0,0,0);

	private boolean runnable;
	private Window window;
	private Camera camera;
	private Scene scene;
	private Loader loader;

	public Engine()
	{
		window = new Window(defaultEngineName, defaultEngineWidth, defaultEngineHeight);
		camera = new Camera(window.getWidth(), window.getHeight(), defaultCameraFov, defaultCameraNear, defaultCameraFar, defaultCameraPosition, defaultCameraRotate);
		scene = new Scene();
		loader = new Loader();
	}

	public void run()
	{
		Timer timer = new Timer();
		Renderer renderer = new Renderer(scene, window, camera);
		Controller controller = new Controller(scene, timer);
		runnable = true;
		while (runnable && !window.isClosed())
		{
			controller.update();
			renderer.update();
			window.update();
			timer.update();
		}
		renderer.destroy();
		window.destroy();
	}

	public Scene getScene()
	{
		return scene;
	}

	public Window getWindow()
	{
		return window;
	}

	public Loader getLoader()
	{
		return loader;
	}

	public Camera getCamera()
	{
		return camera;
	}
}
