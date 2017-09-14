package org.wem3d.util;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public final class FileUtil
{
	public final static CharSequence readString(String src)
	{
		BufferedReader reader = null;
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try
		{
			reader = new BufferedReader(new FileReader(src));
			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line).append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return stringBuilder;
	}

	public final static BufferedImage readImage(String src)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new FileInputStream(src));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}
}
