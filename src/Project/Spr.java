package Project;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import Project.Lights.ccDim;
import Project.Textures.ccConcrete;
import Project.Textures.ccAccessShade;
import Project.Textures.ccAccessShadeCorners;
import Project.Textures.ccCircle;
import Project.Textures.ccTriangle;

public class Spr
{

	public static BufferedImage[] tex_concrete;
	public static BufferedImage concrete_leftrightdown;

	public static BufferedImage light_dim;
	
	public static BufferedImage[][][][] shades;
	public static BufferedImage[] single_shades;
	public static BufferedImage[] corner_shades;
	
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);
	public static BufferedImage icon_tex_concrete;
	
	
	public static BufferedImage player_circle;
	public static BufferedImage zoom_circle;
	
	public static BufferedImage triangle_left, triangle_right;
	
	
	public static void Render()
	{
		// Needs to be an even number
		tex_concrete = new BufferedImage[1];
		for(int i = 0; i < tex_concrete.length; i++)
		{
			tex_concrete[i] = R.render(new ccConcrete(128, 128));
		}
		
		icon_tex_concrete = R.render(new ccConcrete(32, 32));

		light_dim = R.render(new ccDim(128, 128));
		
		// Initialize the arrays.
		shades = new BufferedImage[2][2][2][2];
		
		single_shades = new BufferedImage[4];
		corner_shades = new BufferedImage[4];		
		
		// Populate the atomic single shades.
		single_shades[0] = R.render(new ccAccessShade(128, 128));
		for(int i = 1; i < 4; i++)
		{
			single_shades[i] = ImageFactory.rotate(single_shades[i - 1], 90.0);
		}
		
		// Populate the atomic corner shades.
		corner_shades[0] = R.render(new ccAccessShadeCorners(128, 128));
		for(int i = 1; i < 4; i++)
		{
			corner_shades[i] = ImageFactory.rotate(corner_shades[i - 1], 90.0);
		}
		
		for(int right = 0; right < 2; right++)
		for(int up = 0; up < 2; up++)
		for(int left = 0; left < 2; left++)
		for(int down = 0; down < 2; down++)
		{
			BufferedImage image = ImageFactory.blank(128, 128);
			Graphics g = image.getGraphics();
			
			if(right == 1)
			{
				g.drawImage(single_shades[0], 0, 0, null);
			}
			
			if(up == 1)
			{
				g.drawImage(single_shades[1], 0, 0, null);
			}
			
			if(left == 1)
			{
				g.drawImage(single_shades[2], 0, 0, null);
			}
			
			if(down == 1)
			{
				g.drawImage(single_shades[3], 0, 0, null);
			}
			
			g.dispose();
			
			shades[right][down][left][up] = image;
		}
		
		player_circle = R.render(new ccCircle(128, 128, Colors.Color_hsv(0, 0, 0, 200)));
		zoom_circle   = R.render(new ccCircle(128, 128, Colors.Color_hsv(0, 0, 40)));
		
		triangle_left  = R.render(new ccTriangle(32, 64, Colors.Color_hsv(0, 0, 0)));
		triangle_right = ImageFactory.horizontalReflection(triangle_left);
	}
}
