package Project.Textures;

import java.awt.Color;

public class ccAccessShadeCorners extends ccAccessShade
{

	public ccAccessShadeCorners(int width, int height)
	{
		super(width, height);
	}

	// Override the get color method.
	public Color getColor(double x, double y)
	{
		
		return new Color(0, 0, 0, getVal(x, y));
	}
	
	public int getVal(double x, double y)
	{
		double half_w = room_width/2.0;
		double half_h = room_height/2.0;
		
		double distx = Math.max(x - half_w, 0);
		distx = distx /= half_w;

		double disty = Math.max(y - half_h, 0);
		disty = disty /= half_h;
		
		int val = (int)(distx*distx*disty*disty*255);
		
		val = Math.min(255, val);
		
		return val;
	}
	
}
