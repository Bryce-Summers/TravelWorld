package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.ImageUtil;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Operations.Drawing;
import BryceImages.Operations.ImageFactory;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;

public class WorldViewer extends Obj
{
	// World attributes.
	
	int view_x;
	int view_y;
	
		
	public int mouse_x = 0;
	public int mouse_y = 0;
	
	public int start_x, start_y, index_start_x, index_start_y;
	
	
	double view_x_frac = 0;
	double view_y_frac = 0;
	
		
	int[] zooms = {64, 128, 256};

	int square_size = 64;

	public World W;

	final int world_size;
	
	
	public WorldViewer(int size)
	{
		world_size = size;
		generate_world();
	}

	public void generate_world()
	{
		W = new World(world_size, world_size);
		view_x = W.getViewX();
		view_y = W.getViewY();
	}
	
	@Override
	protected void update()
	{
		// Update square_size to the proper zoom amount.
		square_size = (int)(.9*square_size + .1*zooms[W.zoom]);
		
		// Eliminate camera wrapping artifacts for the torus world.
		int target_x = W.getViewX();
		int target_y = W.getViewY();
		
		if(target_x > view_x + world_size/2)
		{
			view_x += world_size;
		}
		
		if(target_x < view_x - world_size/2)
		{
			view_x -= world_size;
		}

		if(target_y > view_y + world_size/2)
		{
			view_y += world_size;
		}
		
		if(target_y < view_y - world_size/2)
		{
			view_y -= world_size;
		}

		
		// Interpolate the camera between location points.
		moveCameraSmall((W.getViewX() - view_x) * .1, (W.getViewY() - view_y) * .1);
		
		
		//updateWorld();
		updateAll();
	}
	
	// Updates every world object on the map. This actually does not take too long.
	public void updateAll()
	{
		W.update();
		
		int w = W.getWorldIndexW();
		int h = W.getWorldIndexH();
		
		for(int r = 0; r < h; r++)
		for(int c = 0; c < w; c++)
		{
			W.update(c, r);
		}
	}
	
	// Updates all objects in sight.
	public void updateWorld()
	{
		
		W.update();
		
		int w = getRoomW();
		int h = getRoomH();
		
		int xinc = (int)square_size;
		int yinc = (int)square_size;
		
		int half_w = w/2;
		int half_h = h/2;
		
		int x_start = (half_w - xinc*1000) %xinc - (int)(view_x_frac*square_size) - xinc/2;
		int y_start = (half_h - yinc*1000) %yinc - (int)(view_y_frac*square_size) - yinc/2;
		
		int x_index = view_x - half_w/square_size - 1;
		int y_index = view_y - half_h/square_size - 1;
		int y_index_start = y_index;
		
		index_start_x = x_index;
		index_start_y = y_index;
		
		start_x = x_start;
		start_y = y_start;
		
						
		// Update all world squares in sight.
		for(int x = x_start; x < w; x += xinc)
		{
			for(int y = y_start; y < h; y += yinc)
			{
				W.update(x_index, y_index);
				y_index++;
			}
			
			x_index++;
			y_index = y_index_start;
		}
	}
	
	// Draw all of the current images that constitute the visible world.
	public void draw(ImageB i, AffineTransform AT)
	{
		super.draw(i, AT);
		
		int w = getRoomW();
		int h = getRoomH();
		
		Graphics g = i.getGraphics();
		
		// Compute the size of the squares.
		int xinc = (int)square_size;
		int yinc = (int)square_size;
		
		
		// Makes sure these values are rock solid.
		int x_start = getLeftMostVisibleSquareX();
		int y_start = getTopMostVisibleSquareY();

		// Compute the left and top indices for visible squares.
		int x_index = (int)(view_x - w/2.0/square_size);
		int y_index = (int)(view_y - h/2.0/square_size);
		int y_index_start = y_index;
		
		
		// Draw scaled versions of the images.
		for(int x = x_start; x < w; x += xinc)
		{
			for(int y = y_start; y < h; y += yinc)
			{
				Drawing.draw_sized(g, x, y, W.getTextureImage(x_index, y_index), (int)square_size, (int)square_size);
				
				List<BufferedImage> images = W.getImages(x_index, y_index);
				
				for(BufferedImage b : images)
				{
					Drawing.draw_sized(g, x, y, b, (int)square_size, (int)square_size);
				}
				
				// Code for highlighting the world object location that the mouse is currently on.
				/*
				if(x_index == mouse_x && y_index == mouse_y)
				{
					g.setColor(new Color(255, 255, 255, 200));
					g.fillRect(x, y, square_size, square_size);
				}
				//*/


				y_index++;
			}
			
			x_index++;
			y_index = y_index_start;
		}
		
		String message = W.getCurrentMessage();
		
		if(message != null)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, h - 64, w, 64);
			
			Graphics2D g2d = (Graphics2D)g;    
			Font font = new Font("Arial", Font.PLAIN, 48);

			g2d.setFont(font);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.black);
			g2d.drawString(message, 0, h - 16);
			
			//drawTextCenter((Graphics2D)g, AT, w/2, h - 32, message, 32);
		}
		
		g.dispose();
	}
	
	private int getLeftMostVisibleSquareX()
	{
		int w = getRoomW();
		
		int leftx = (int) (-((w/2.0) - square_size*1000)) % square_size;
				
		return -leftx - square_size/2 - (int)(view_x_frac*square_size);
	}
	
	private int getTopMostVisibleSquareY()
	{
		int h = getRoomH();
		
		int topy = (int) (-((h/2.0) - square_size*1000)) % square_size;
				
		return -topy - square_size/2 - (int)(view_y_frac*square_size);
	}

	/*
	 * 
	 * User input.
	 * 
	 */
	
	// Cycle through the various zoom levels.
	public void global_mouseScroll(int amount)
	{
		if(amount > 0)
		{
			zoom();
		}
		
		if(amount < 0)
		{
			unzoom();
		}
		
	}
	
	// FIXME : Perhaps move this to the world class.
	@Override
	public void keyP(int key)
	{
		W.keyP(key);
		
		if(key == KeyEvent.VK_S)
		{
			saveImage("WorldPicture");
		}
	}
	
	// Saves the entire world as an image.
	public void saveImage(String name)
	{
		int size = 7200;
		
		BufferedImage output = ImageFactory.blank(size, size);
		
		Graphics g = output.getGraphics();
		
		int w = W.getWorldIndexW();
		int h = W.getWorldIndexW();
		
		int square_size = size/w;
					
		for(int r = 0; r < h; r++)
		{
			System.out.println("R = " + r);
		
			for(int c = 0; c < w; c++)
			{
				int x = c*square_size;
				int y = r*square_size;
				
				Drawing.draw_sized(g, x, y, W.getTextureImage(c, r), (int)square_size, (int)square_size);
				List<BufferedImage> images = W.getImages(c, r);
				
				for(BufferedImage b : images)
				{
					Drawing.draw_sized(g, x, y, b, (int)square_size, (int)square_size);
				}
			}
		}

		ImageUtil.saveImage(output, name);
		System.out.println("Done");
	}
	
	@Override
	public void keyR(int key)
	{
		W.keyR(key);
	}

	// Zooming functions.
	public void zoom()
	{
		int new_zoom = W.zoom + 1;
		W.zoom = (new_zoom + zooms.length) % zooms.length;
		
	}
	
	public void unzoom()
	{
		int new_zoom = W.zoom - 1;
		W.zoom = (new_zoom + zooms.length) % zooms.length;
	}
	
	
	// Move the camera in index units.
	public void moveWorldCamera(int dx, int dy)
	{
		W.moveView(dx, dy);
	}
	
	// Move camera in fractional index.
	private void moveCameraSmall(double dx, double dy)
	{
		view_x_frac += dx;
		view_y_frac += dy;
		
		view_x += (int)view_x_frac;
		view_x_frac = (view_x_frac % 1.0);
		
		view_y += (int)view_y_frac;
		view_y_frac = (view_y_frac % 1.0);
	}
	
	// Methods for deriving the indices at given locations. 
	public int getIndexX(int mousex)
	{
		double offset = (mousex - getRoomW()*.5)/square_size;
		offset += .5*BryceMath.sign(offset);
		
		return view_x + (int)offset;
	}
	
	public int getIndexY(int mousey)
	{
		double offset = (mousey - getRoomH()*.5)/square_size;
		offset += .5*BryceMath.sign(offset);
		return view_y + (int)offset;
	}

	public void setWorldObject(int index_x, int index_y, WorldObject object)
	{
		W.data.setWorldObject(index_x, index_y, object);		
	}
	
	public WorldObject getWorldObject(int index_x, int index_y)
	{
		return W.data.getWorldObject(index_x, index_y);
	}
}
