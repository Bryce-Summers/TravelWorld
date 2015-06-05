package Project;

import java.io.PrintStream;
import java.util.Iterator;

import util.SerialB;

public class WorldObject implements SerialB
{
	
	public int type    = 0;
	public int texture = 0;
	
	public int light   = 100;
	public int state   = 0;// Used for things like animations.
	
	public int red   = 255;
	public int green = 255;
	public int blue  = 255;
	public int alpha = 0;
	
	// Free specifier value.
	public int val1 = 0;
	
	// true iff the world object has a wall at this location.
	// By default all world objects are walled off from each other.
	public boolean up = true, left = true, down = true, right = true;
	
	// Used to perform some actions only one time.
	public int charge = 100;

	// Random seed.
	public int rand = (int)(Math.random()*1000);
	
	public WorldObject clone()
	{
		WorldObject output = new WorldObject();
		
		output.type    = type;
		output.light   = light;
		output.texture = texture;
		output.state   = state;
		
		output.left  = left;
		output.up    = up;
		output.right = right;
		output.down  = down;
		
		output.red   = red;
		output.green = green;
		output.blue  = blue;
		
		output.val1 = val1;
		
		return output;
	}

	public void serializeTo(PrintStream stream)
	{
		stream.println(type);
		stream.println(texture);
		
		stream.println(red);
		stream.println(green);
		stream.println(blue);
		stream.println(alpha);
		
		stream.println(right);
		stream.println(up);
		stream.println(left);
		stream.println(down);
	}

	public String getSerialName()
	{
		// TODO Auto-generated method stub
		return "WorldObject";
	}
	
	public void serializeFrom(Iterator<String> iter)
	{
		type    = new Integer(iter.next());
		texture = new Integer(iter.next());
		
		red     = new Integer(iter.next());
		green   = new Integer(iter.next());
		blue    = new Integer(iter.next());
		alpha   = new Integer(iter.next());
		
		right  = new Boolean(iter.next());
		up     = new Boolean(iter.next());
		left   = new Boolean(iter.next());
		down   = new Boolean(iter.next());
		
		state = 0;
		light = 0;
	}
	
}

