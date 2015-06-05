package Project.Textures;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccAccessShade extends ColorCalculator
{

	public ccAccessShade(Dimension dim)
	{
		super(dim);
	}

	public ccAccessShade(int width, int height)
	{
		super(width, height);
	}

	public Color getColor(double x, double y)
	{
		return new Color(0, 0, 0, getVal(x, y));
	}
	
	public int getVal(double x, double y)
	{
		int half_w = room_width/2;
		
		double dist = Math.max(x - half_w, 0);
		dist = dist /= half_w;
				
		int val = (int)(dist*dist*255);
		
		val = Math.min(255, val);
		
		return val;
	}

}
