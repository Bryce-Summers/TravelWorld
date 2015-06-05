package Project.Lights;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccDim extends ColorCalculator
{

	public ccDim(Dimension dim)
	{
		super(dim);
	}

	public ccDim(int width, int height)
	{
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public Color getColor(double x, double y)
	{
		return Color_hsv(0, 0, 0, 30);
	}

}
