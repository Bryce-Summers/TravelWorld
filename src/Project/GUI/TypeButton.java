package Project.GUI;

import Game_Engine.GUI.Components.small.gui_button;

public class TypeButton extends gui_button
{

	static TypeButton focus = null;
	
	int typeval;
	
	public TypeButton(double x, double y, int w, int h, int val)
	{
		super(x, y, w, h);
		focus = this;
		typeval = val;
	}
	
	int getVal()
	{
		return typeval;
	}
	
	public void update()
	{
		super.update();
		if(flag())
		{
			focus = this;
		}
	}
}