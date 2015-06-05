package Project;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;
import Project.GUI.WorldObjectPallet;

public class room_world extends Room
{
	
	WorldViewer W;
	WorldObjectPallet pallet;

	public room_world()
	{
		
	}

	public room_world(Game_output out)
	{
		super(out);
	}

	public void iObjs()
	{
		set_dimensions(aaMain.GAME_W, aaMain.GAME_H);
		
		W = new WorldViewer(72);
		obj_create(W);
		
		pallet = new WorldObjectPallet(0, 0, 256, 500, W.W);
		obj_create(pallet);
	}

	public void mouseM(int x, int y)
	{
		super.mouseM(x, y);
		W.mouse_x = W.getIndexX(x);
		W.mouse_y = W.getIndexY(y);
	}
	
	@Override
	public void mouseD(int x, int y)
	{
		super.mouseD(x, y);
		
		// Do not create new objects if the user is clicking on the pallet.
		if(pallet.mouseCollision(x, y))
		{
			return;
		}
		
		int index_x = W.getIndexX(x);
		int index_y = W.getIndexY(y);
		
		W.setWorldObject(index_x, index_y, pallet.getObject());
		
	}
	
	@Override
	public void mouseP(int x, int y)
	{
		super.mouseP(x, y);
		
		// Do not create new objects if the user is clicking on the pallet.
		if(pallet.mouseCollision(x, y))
		{
			return;
		}
		
		int index_x = W.getIndexX(x);
		int index_y = W.getIndexY(y);
		
		W.setWorldObject(index_x, index_y, pallet.getObject());
		
		//make_light_image(index_x, index_y);
		
		
	}
	
	private void make_light_image(int index_x, int index_y)
	{
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("images/plane.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int iw = image.getWidth();
		int ih = image.getHeight();
		
		for(int x = 0; x < iw; x++)
		for(int y = 0; y < ih; y++)
		{
			Color c = new Color(image.getRGB(x, y));
			WorldObject o = W.getWorldObject(index_x + x, index_y + y);
			o.red   = c.getRed();
			o.green = c.getGreen();
			o.blue  = c.getBlue();
			o.alpha = c.getAlpha();
			W.setWorldObject(index_x + x, index_y + y, o);
		}
	}
	
	public void keyR(int key)
	{
		super.keyR(key);
		
		if(key == KeyEvent.VK_SPACE)
		{
			pallet.setVisible(!pallet.isVisible());
		}
	}
	
	
}