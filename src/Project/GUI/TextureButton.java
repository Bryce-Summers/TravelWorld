package Project.GUI;

import Game_Engine.GUI.Components.small.gui_button;

public class TextureButton extends gui_button
{

	static TextureButton focus = null;
	
	int textureval;
	
	public TextureButton(double x, double y, int w, int h, int val)
	{
		super(x, y, w, h);
		focus = this;
		textureval = val;
	}
	
	int getVal()
	{
		return textureval;
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
