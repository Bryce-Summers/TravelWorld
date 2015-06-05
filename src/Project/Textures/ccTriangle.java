package Project.Textures;

import java.awt.Color;
import BryceImages.Engines.Image_vectorGeometry;


public class ccTriangle extends Image_vectorGeometry
{

	Color color;
	
	public ccTriangle(int width, int height, Color c)
	{
		super(width, height);
		color = c;
		set_color(color);
		
		antiAliasing = 4;
	}

	@Override
	public void i_geoms()
	{
		int w = getWidth();
		int h = getHeight();
		
		i_triangle(v(0, h), v(w, 0), v(w, h));
	}
	


}
