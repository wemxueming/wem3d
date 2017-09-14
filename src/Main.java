import org.math3d.Matrix4f;
import org.math3d.Vector2f;
import org.math3d.Vector3f;
import org.wem3d.*;
import org.wem3d.control.FreeLook;
import org.wem3d.light.DirectionLight;
import org.wem3d.util.MathUtil;

public class Main
{
	public static void main(String[] args)
	{
		run();
	}

	public static void run()
	{
		Engine engine = new Engine();
		Scene scene = engine.getScene();
		Loader loader = engine.getLoader();
		Camera camera = engine.getCamera();

		FreeLook freeLook = new FreeLook(camera);
		scene.add(freeLook);

		Model plan = loader.loadPlan();
		plan.getDefaultMod().get("face").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/bricks2.jpg", false));
		plan.getDefaultMod().get("face").getMaterial().setNormalMap(loader.loadTexture("asset/test/bricks2_normal.jpg", false));
		plan.getDefaultMod().get("face").getMaterial().setHeightMap(loader.loadTexture("asset/test/bricks2_disp.jpg", false));
		plan.getDefaultMod().get("face").getMaterial().setWrap(new Vector2f(2,2));
		plan.getDefaultMod().get("face").getMaterial().setHeightScale(0.05f);
		Object3D plan1 = new Object3D();
		plan1.setScale(new Vector3f(10));
		plan1.setModel(plan);
		scene.add(plan);

		Model cube = loader.loadCube();
		cube.getDefaultMod().get("up").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("up").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("up").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));
		cube.getDefaultMod().get("up").getMaterial().setHeightScale(0.15f);

		cube.getDefaultMod().get("down").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("down").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("down").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));

		cube.getDefaultMod().get("front").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("front").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("front").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));

		cube.getDefaultMod().get("back").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("back").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("back").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));

		cube.getDefaultMod().get("left").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("left").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("left").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));

		cube.getDefaultMod().get("right").getMaterial().setAlbedoMap(loader.loadTexture("asset/test/toy_box_diffuse.png", false));
		cube.getDefaultMod().get("right").getMaterial().setNormalMap(loader.loadTexture("asset/test/toy_box_normal.png", false));
		cube.getDefaultMod().get("right").getMaterial().setHeightMap(loader.loadTexture("asset/test/toy_box_disp.png", false));

		Object3D cube1 = new Object3D();
		cube1.setPosition(new Vector3f(0,1,-1));
		cube1.setModel(cube);
		scene.add(cube);

		DirectionLight directionLight = new DirectionLight(new Vector3f(0, -0.6f,1));
		scene.add(directionLight);

		engine.run();
	}

	public static void test()
	{
		Matrix4f transformMatrix = new Matrix4f();
		Vector3f scale = new Vector3f(1,1,1);
		Vector3f position = new Vector3f(0.5f,0.5f,0.5f);
		Vector3f rotate  = new Vector3f(0.2f,0.2f,0.2f);
		transformMatrix.setIdentity();
		Matrix4f.scale(scale, transformMatrix, transformMatrix);
		Matrix4f.translate(position, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.z), MathUtil.Z_AXIS, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.y), MathUtil.Y_AXIS, transformMatrix, transformMatrix);
		Matrix4f.rotate(MathUtil.toRadians(rotate.x), MathUtil.X_AXIS, transformMatrix, transformMatrix);
		System.out.println(transformMatrix);
	}
}
