package Project.Textures;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccConcrete extends ColorCalculator
{

	public ccConcrete(Dimension dim)
	{
		super(dim);
	}

	public ccConcrete(int width, int height)
	{
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public Color getColor(double x, double y)
	{
		double rand = Math.random()*10;
		
		int val = (int)(200 + rand);
		
		return new Color(val, val, val);
	}

}
