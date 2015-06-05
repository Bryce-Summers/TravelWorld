package Project.Textures;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.DoubleMath.ColorB;
import BryceMath.DoubleMath.Vector;

public class ccRect extends ColorCalculator
{

	ColorB colorb;
	Color color;
	
	public ccRect(int width, int height, Color c)
	{
		super(width, height);
		
		colorb = new ColorB(c);
		color = c;
	}

	@Override
	public Color getColor(double x, double y)
	{
		int w = room_width;
		int h = room_height;
		
		int padding = 16;
		
		if(x > padding && x < w - padding && y > padding && y < h - padding)
		{
			return ((ColorB) (colorb.mult(.9))).toColor();
		}
	
		
		return Color.BLACK;
	}
	


}
