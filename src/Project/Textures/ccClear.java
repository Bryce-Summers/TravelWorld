package Project.Textures;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;

public class ccClear extends ColorCalculator
{

	public ccClear(Dimension dim)
	{
		super(dim);
	}

	public ccClear(int width, int height)
	{
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public Color getColor(double x, double y)
	{
		return Colors.C_CLEAR;
	}

}
