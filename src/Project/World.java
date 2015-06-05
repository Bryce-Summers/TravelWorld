package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Operations.Drawing;
import BryceImages.Operations.ImageFactory;
import Data_Structures.Structures.List;

public class World
{

	int view_x;
	int view_y;
	
	public enum Mode{USER, TRAIN};
	public Mode mode = Mode.USER;
	public int train_direction = 0;
	
	public WorldIO data;
	
	public int zoom = 2;
	
	public static final int BLANK = 0, HALLWAY = 1, ELEVATOR = 2, LIGHT = 3,
			LIGHT_FUSE = 4, LIGHT_TRANSFORM = 5,// An object that propogates increased light.
			WINDOW = 6,
			ZOOM1 = 7, ZOOM2 = 8, ZOOM3 = 9,
			TRAIN = 10, RAMP_RIGHT = 11, RAMP_LEFT = 12,
			M1 = 20, M2 = 21, M3 = 22, M4 = 23;
	
	public static final int TEX_CONCRETE = 0;
	
	
	public static final 
	 String s1 = "Go down the hallway to your right",
			s2 = "Go down the elevator",
			s3 = "Take the train to the right to the airport.",
			s4 = "Ride the plane";
	
	
	boolean shift   = false;
	Integer key     = null;
	boolean keyflag = false;// Signals that the key has been released.
	
	public World(int w, int h)
	{
		data = new WorldIO(w, h);
		
		// FIXME : Should this be specified?
		view_x = w/2;
		view_y = h/2;
	}

	public BufferedImage getTextureImage(int index_x, int index_y)
	{
	
		WorldObject o = data.getWorldObject(index_x, index_y);
		
		if(o.texture == TEX_CONCRETE)
		{
			int len = Spr.tex_concrete.length;
			o.rand = (o.rand + 1 + (int)(Math.random()*2)) % len;
			return Spr.tex_concrete[o.rand % len];
			
		}
		
		return null;
	}
	
	// Background.
	public List<BufferedImage> getImages(int index_x, int index_y)
	{
		List<BufferedImage> output = new List<BufferedImage>();
				
		WorldObject o = data.getWorldObject(index_x, index_y);
		
		BufferedImage canvas = ImageFactory.blank(128, 128);
		Graphics g = canvas.getGraphics();
		
		output.add(canvas);
		RenderTypeImage(o, g);
		RenderLighting(o, g);
		RenderAccessShades(o, output, index_x, index_y);
		
		if(index_x == view_x && index_y == view_y)
		{
			g.drawImage(Spr.player_circle, 0, 0, null);
		}
		
		g.dispose();
			
		return output;
	}
	
	public void RenderAccessShades(WorldObject o, List<BufferedImage> output,
									int index_x, int index_y)
	{
	
		int i1 = o.right ? 1 : 0;
		int i2 = o.up    ? 1 : 0;
		int i3 = o.left  ? 1 : 0;
		int i4 = o.down  ? 1 : 0;
		
		output.append(Spr.shades[i1][i2][i3][i4]);
		
		WorldObject down  = data.getWorldObject(index_x, index_y + 1);
		WorldObject up    = data.getWorldObject(index_x, index_y - 1);
		WorldObject left  = data.getWorldObject(index_x - 1, index_y);
		WorldObject right = data.getWorldObject(index_x + 1, index_y);
		
		// Left down corner shade.
		if((!o.left && !o.down) && (down.left || left.down|| down.up || left.right))
		{
			output.append(Spr.corner_shades[1]);
		}
		
		// right down corner shade.
		if((!o.right && !o.down) && (down.right || right.down || down.up || right.left))
		{
			output.append(Spr.corner_shades[0]);
		}
		
		// Left, up corner shade.
		if((!o.left && !o.up) && (up.left || left.up || up.down || left.right))
		{
			output.append(Spr.corner_shades[2]);
		}
		
		// Left, up corner shade.
		if((!o.right && !o.up) && (up.right || right.up || up.down || right.left))
		{
			output.append(Spr.corner_shades[3]);
		}

		
	}

	public void keyP(int key)
	{
		this.key = key;
		
		if(key == KeyEvent.VK_SHIFT)
		{
			shift = true;
		}
		
	}
	
	// Handle key releases.
	public void keyR(int key)
	{
		if(this.key != null && key == this.key)
		{
			keyflag = true;
		}
		
		if(key == KeyEvent.VK_SHIFT)
		{
			shift = false;
		}
	}

	// Translates state values into world images.
	// REQUIRES : g points to a 128 by 128 image.
	private void RenderTypeImage(WorldObject o, Graphics g)
	{
		
		if(o.type == RAMP_RIGHT)
		{
			g.setColor(Color.black);
			Drawing.drawTriangle(g,
					0,   128,
					128, 128,
					128, 0);
		}
		
		if(o.type == RAMP_LEFT)
		{
			g.setColor(Color.black);
			Drawing.drawTriangle(g,
					128,   128,
					0, 128,
					0, 0);
		}
		
		// Sign posts for displaying messages.
		if(o.type >= M1 && o.type <= M4)
		{
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(32, 32, 64, 48);
			g.fillRect(64 - 16, 32+ 48, 32, 16);
			return;
		}
		
		if(o.type == ZOOM1 || o.type == ZOOM2 || o.type == ZOOM3)
		{
			g.drawImage(Spr.zoom_circle, 0, 0, null);
			
			//g.setColor(new Color(255, 255, 255, 200));
			//g.fillOval(32, 32, 64, 64);
			return;
		}
		
		if(o.type == LIGHT)
		{
			g.setColor(Color.WHITE);
			g.fillRect(32, 0, 64, 32);
			return;
		}
		
		if(o.type == LIGHT_FUSE)
		{
			g.setColor(Color.WHITE);
			g.fillRect(32, 128 - 32, 64, 32);
			return;
		}

		if(o.type == LIGHT_TRANSFORM)
		{
			g.setColor(Color.WHITE);
			g.fillRect(32, 32, 64, 64);
			return;
		}
		
		if(o.type == ELEVATOR)
		{
						
			g.setColor(Color.black);
			
			g.fillRect(32 - 32*o.state/100, 16, 32, 96);
			g.fillRect(64 + 32*o.state/100, 16, 32, 96);
			
			return;
		}
		
		if(o.type == TRAIN)
		{
						
			g.setColor(Color.black);
			
			int x_offset = 32*o.state/100;
			
			g.drawImage(Spr.triangle_left,  32 - x_offset, 32, null);
			g.drawImage(Spr.triangle_right, 64 + x_offset, 32, null);
			
			return;
		}
		
		if(o.type == WINDOW)
		{
			g.setColor(new Color(0, 0, 0, 175));
			g.fillRect(32, 32, 64, 64);
			return;			
		}
	}
	
	// Overlays a lighting value to the square.
	private void RenderLighting(WorldObject o, Graphics g)
	{
		
		int val = o.light;
		val = (int) BryceMath.bound(val, 0, 100);
		
		int red   = o.red*val/100;
		int blue  = o.blue*val/100;
		int green = o.green*val/100;
		int alpha = o.alpha*(val)/100 + 255*(100 - val)/100;
		
		
		Color c = new Color(red, green, blue, alpha);
		
		//Graphics2D g2 = (Graphics2D)g;
		//AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC);
		//g.setBlendMode();
		g.setColor(c);
		// 96;
		g.fillRect(0, 0, 128, 128);
		
	}
	
	public int getViewX()
	{
		return view_x;
	}
	
	public int getViewY()
	{
		return view_y;
	}
	
	public void moveView(int dx, int dy)
	{
		view_x += dx;
		view_y += dy;
		
		// The world is square, so w = h;
		int world_size = data.w;
		
		if(view_x < 0)
		{
			view_x += world_size;
		}
		
		if(view_x >= world_size)
		{
			view_x -= world_size;
		}
		
		if(view_y <= 0)
		{
			view_y += world_size;
		}
		
		if(view_y >= world_size)
		{
			view_y -= world_size;
		}
		
	}
	
	// Handle key presses in an update function.
	public void update()
	{
		
		WorldObject current = getCurrentWorldObj();
		
		if(mode == Mode.TRAIN)
		{
			handle_train_movement();
			if(getCurrentWorldObj().type == TRAIN)
			{
				mode = Mode.USER;
				key = null;
			}
			return;
		}
		
		// Handle zooming squares.
		switch(current.type)
		{
			case ZOOM1:
				zoom = 0;
				break;
			case ZOOM2:
				zoom = 1;
				break;
			case ZOOM3:
				zoom = 2;
				break;
		}

		if(key == null)
		{
			return;
		}
		
		handle_keyPress();
		
		// Release the key if shift is not pressed or a release signal has been sent.
		if(keyflag || !shift)
		{
			keyflag = false;
			this.key = null;
		}
		
	}
	
	private void handle_train_movement()
	{
		WorldObject o = getCurrentWorldObj();		
		
		// Handle passive effects of a train ride.
		switch(o.type)
		{
			case ZOOM1:
				zoom = 0;
				break;
			case ZOOM2:
				zoom = 1;
				break;
			case ZOOM3:
				zoom = 2;
				break;
		}
		
		
		// No exit from this block.
		if(o.left && o.up && o.right && o.down)
		{
			mode = Mode.USER;
			
			return;
		}
		
		// An exit must exist.
		
		int coming_direction = (train_direction + 2) % 4;
		
		// FIXME : Improve tunnel following.
		while(true)
		{
			train_direction = (train_direction + 4) % 4;
			
			// Do not try to go back the way we came if we can help it.
			if(train_direction == coming_direction)
			{
				coming_direction++;
				train_direction--;
			}
			
		switch(train_direction)
		{
			case 0:
				if(!o.right)
				{
					view_x++;
					return;
				}
				train_direction--;
				continue;
			case 1:
				if(!o.up)
				{
					view_y--;
					return;
				}
				train_direction--;
				continue;
			case 2:
				if(!o.left)
				{
					view_x--;
					return;
				}
				train_direction--;
				continue;
			case 3:
				if(!o.down)
				{
					view_y++;
					return;
				}
				train_direction--;
				continue;
		}
		}
	}

	public void handle_keyPress()
	{
		WorldObject o = getCurrentWorldObj();
		
		if(o.type == RAMP_RIGHT)
		{
			if(KeyEvent.VK_RIGHT == key)
			{
				// Move up.
				moveView(0, -1);
				return;
			}
			
			if(KeyEvent.VK_DOWN == key)
			{
				// Move left.
				moveView(-1, 0);
				return;
			}
			
		}
		
		if(o.type == RAMP_LEFT)
		{
			if(KeyEvent.VK_LEFT == key)
			{
				// Move up.
				moveView(0, -1);
				return;
			}
			
			if(KeyEvent.VK_DOWN == key)
			{
				// Move left.
				moveView(1, 0);
				return;
			}

		}
		
		
		if(o.type == TRAIN)
		{
			// right key and right is not blocked by a wall.
			if(KeyEvent.VK_RIGHT == key && !o.right)
			{
				mode = Mode.TRAIN;
				train_direction = 0;
				key = null;
				return;
			}
			
			// left key and left is not blocked by a wall.
			if(KeyEvent.VK_LEFT == key && !o.left)
			{
				mode = Mode.TRAIN;
				train_direction = 2;
				key = null;
				return;
			}
		}
		
		
		// Handle Elevators.
		if(getCurrentType() == ELEVATOR)
		{
			if(KeyEvent.VK_DOWN == key && !o.down)
			{
				do
				{
					moveView(0, 1);
				}
				while(getCurrentType() != ELEVATOR);
				return;
			}
			
			if(KeyEvent.VK_UP == key && !o.up)
			{
				do
				{
					moveView(0, -1);
				}
				while(getCurrentType() != ELEVATOR);
				return;
			}
		}
			

		
		// Default movement.
		switch(key)
		{
		case KeyEvent.VK_LEFT:
			if(o.left == false)
			{	
				moveView(-1, 0);
			}
			break;
		case KeyEvent.VK_UP:
			if(o.up == false)
			{
				moveView(0, -1);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(o.right == false)
			{
				moveView(1, 0);
			}
			break;
		case KeyEvent.VK_DOWN:
			if(o.down == false)
			{
				moveView(0, 1);
			}
			break;
		}
	}

	// We need to update the animatable attributes.
	public void update(int x_index, int y_index)
	{
		WorldObject o = data.getWorldObject(x_index, y_index);
		
		// Bring all non active squares to a resting state.
		if(x_index != view_x || y_index != view_y)
		{
			
			if(o.type != LIGHT)
			{
				o.state = o.state*9/10;
				o.light = o.light*9/10;				
			}

			propagateLight(o, x_index, y_index);
			return;
		}
		
		
		// Active square code.
		
		int val = 100;//50 + (int)(Math.random()*50);
		
		// Active square.
		o.state = o.state*8/10 + 100*2/10;
		o.light = o.light*8/10 + val*2/10;
		
		propagateLight(o, x_index, y_index);
		
				
		return;
	}
	
	private void propagateLight(WorldObject o, int x_index, int y_index)
	{
		
		// Handle windows.
		if(o.type == WINDOW)
		{
			propogateLight(o, data.getWorldObject(x_index + 1, y_index));
			propogateLight(o, data.getWorldObject(x_index - 1, y_index));
			propogateLight(o, data.getWorldObject(x_index, y_index - 1));
			propogateLight(o, data.getWorldObject(x_index, y_index + 1));
			return;
		}
		
		
		if(!o.up && o.type != ELEVATOR)
		{
			propogateLight(o, data.getWorldObject(x_index, y_index - 1));	
		}
		
		if(!o.right)
		{
			propogateLight(o, data.getWorldObject(x_index + 1, y_index));	
		}
		
		if(!o.left)
		{
			propogateLight(o, data.getWorldObject(x_index - 1, y_index));
		}
		
		if(!o.down && o.type != ELEVATOR)
		{
			propogateLight(o, data.getWorldObject(x_index, y_index + 1));
		}
		
		if(o.type == LIGHT_FUSE)
		{
			if(o.charge > 0)
			o.charge--;
		}
	}
	
	// This function specifies how much light gets propogate between connected squares.
	private void propogateLight(WorldObject src, WorldObject dest)
	{
		
		if(src.type == LIGHT_FUSE && src.light > 80 && src.charge > 0)
		{
			dest.light += src.charge;
			dest.light = Math.max(dest.light, dest.light*1/10 + src.charge*9/10);
			return;
		}
	
		int src_light = Math.min(src.light, 100);
		if(src.type == LIGHT || src == getCurrentWorldObj())
		{
			src_light -= (int)(Math.random()*15);
			src_light = Math.max(0, src_light);
		}
		
		// Transformers propogate more light than they receive.
		if(src.type == LIGHT_TRANSFORM)
		{
			src_light = src.light * 13 / 9;
			src_light = (int)(BryceMath.bound(src_light, 0, 100) -  (Math.random()*5));
			dest.light = Math.max(dest.light, dest.light*1/10 + src_light*9/10);
			dest.light = (int)BryceMath.bound(dest.light, 0, 100);
			
			return;
		}
		
		dest.light = Math.max(dest.light, dest.light*5/10 + src_light*5/10);
	}
		
	private int getCurrentType()
	{
		WorldObject o = data.getWorldObject(view_x, view_y);
		return o.type;
	}
	
	private WorldObject getCurrentWorldObj()
	{		
		return data.getWorldObject(view_x, view_y);		
	}

	public String getCurrentMessage()
	{
		switch(getCurrentWorldObj().type)
		{
		case M1 : return s1;
		case M2 : return s2;
		case M3 : return s3;
		case M4 : return s4;
		}
		
		return null;
	}

	// Returns the number of tiles across that the world is.
	public int getWorldIndexW()
	{
		return data.w;
	}
	
	// Returns the number of tiles vertically the world is.
	public int getWorldIndexH()
	{
		return data.h;
	}
	
}
