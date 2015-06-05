package Project.Textures;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.ColorB;
import BryceMath.DoubleMath.Vector;

public class ccCircle extends ColorCalculator
{

	Color color;
	
	public ccCircle(int width, int height, Color c)
	{
		super(width, height);
		
		color = c;
		
		antiAliasing = 4;
	}

	@Override
	public Color getColor(double x, double y)
	{
		int w = room_width;
		int h = room_height;
		
		double radius = w/4.0;
		
		double dist = BryceMath.sqrDistance(x, y, w/2.0, h/2.0);
		
		double weight = dist/(radius*radius);
		
		if(weight >= 1)
		{
			return Colors.C_CLEAR;
		}
		
		return Colors.weightedAverageColor(color, Colors.C_CLEAR, weight);

	}
	


}
