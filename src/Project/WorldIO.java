package Project;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;

import BryceMath.Calculations.Colors;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.InDevelopment.UnionFind;

import util.FileIO;

/*
 * A class that handle saving and loading chunks.
 * 
 * This class abstracts the process of loading and saving regions of a large world to a file.
 * 
 * It may be time consuming to load or save an entire world, 
 * so dividing it into chunks enables us to use larger worlds without the user complaining about the time
 * it takes to load and save data to memory.
 */

public class WorldIO
{
	public int w, h;
	public WorldObject[][] world;
	
	// Chunk size constant.
	final static int chunk_size = 100;
	
	public boolean[][] chunks_loaded;
	public boolean[][] chunks_modified;
	
	boolean manual_world = false;
	
	public static int color_light_num = 10000;
	
	// -- Constructor.
	public WorldIO(int w, int h)
	{
		generate_world(w, h);
	}
	
	private void generate_world(int w, int h)
	{
		this.w = w;
		this.h = h;
		
		world = new WorldObject[w][h];
		
		for(int c = 0; c < w; c++)
		for(int r = 0; r < h; r++)
		{
			WorldObject o = new WorldObject();
			
			world[c][r] = o;
		}
		
		// These should start out as initialized to false.
		chunks_loaded   = new boolean[w/chunk_size + 1][h/chunk_size + 1];
		chunks_modified = new boolean[w/chunk_size + 1][h/chunk_size + 1];
		
		if(!manual_world)
		randomizeWorld();
	}
	
	public void randomizeWorld()
	{
		int paths = 0;// 1000000;
		
		UnionFind<Pair<Integer, Integer>> UF = new UnionFind<Pair<Integer, Integer>>();
		
		for(int x = 0; x < w; x++)
		for(int y = 0; y < h; y++)
		{
			UF.makeset(new Pair<Integer, Integer>(x, y));
			WorldObject o = getWorldObject(x, y);

			/*
			o.red   = 255;//(int)(Math.random()*255);
			o.green = 0;  //(int)(Math.random()*255);
			o.blue  = 0;  //(int)(Math.random()*255);
			o.alpha = 10; //(int)(Math.random()*20);
			*/
		}
		
		addRandoms(paths, UF);
		
		UBA<Pair<Integer, Integer>> ps = new UBA<Pair<Integer, Integer>>();
		
		// Connect all of the squares.
		for(int y = 0; y < h; y++)
		for(int x = 0; x < w; x++)
		{
			ps.add(getPair(x, y));
		}
		
		Pair<Integer, Integer>[] array = ps.toArray();
		
		shuffleArray(array);
		
		for(Pair p : array)
		{
			int x = (Integer) p.getKey();
			int y = (Integer)p.getVal();
			
			WorldObject o1 = getWorldObject(x, y);
			WorldObject o2 = getWorldObject(x + 1, y);
			WorldObject o3 = getWorldObject(x, y + 1);
			
			Pair p1 = getPair(x, y);
			Pair p2 = getPair(x + 1, y);
			Pair p3 = getPair(x, y + 1);

			if(UF.connected(p1, p2) == false)
			{
				o1.right = false;
				o2.left  = false;
				UF.union(p1, p2);
			}
			
			if(UF.connected(p1, p3) == false)
			{
				o1.down = false;
				o3.up   = false;
				UF.union(p1, p3);
			}
			
		}
		
		addRandoms(paths, UF);
		
		
		// Add Lights.
		for(int y = 0; y < h; y++)
		for(int x = 0; x < w; x++)
		{
			WorldObject o = getWorldObject(x, y);
			if(o.up)
			{
				if(Math.random() < .3)
				{
					o.type = World.LIGHT;
				}
			}
			
			if(o.up && o.down)
			{
				if(Math.random() < .1)
				{
					o.type = World.ELEVATOR;
					o.up = false;
					o.down = false;
				}
			}
		}
		
		// Add random light sections.
		addRandomLights(color_light_num);
		
	}
		
	// http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	// Implementing Fisher–Yates shuffle
	static void shuffleArray(Object[] ar)
	{
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			Object a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
	    }
	}
	
	
	
	private void addRandoms(int paths, UnionFind<Pair<Integer, Integer>> UF)
	{
		for(int i = 0; i < paths; i++)
		{
			
			int x = (int)(Math.random()*w);
			int y = (int)(Math.random()*h);
			
			WorldObject o = getWorldObject(x, y);
			
			
			int dir = (int)(Math.random()*2);
			switch(dir)// Fall through used to create as many new edges as possible.
			{
			case 0:
				if(o.right)
				{
					o.right = false; getWorldObject(x + 1, y).left  = false;
					UF.union(getPair(x + 1, y), getPair(x, y));break;
				}
			case 1:
				if(o.down)
				{
					o.down  = false; getWorldObject(x, y + 1).up    = false;
					UF.union(getPair(x, y + 1), getPair(x, y));break;
				}
				
			default:
				if(o.right)
				{
					o.right = false; getWorldObject(x + 1, y).left  = false;
					UF.union(getPair(x + 1, y), getPair(x, y));break;
				}
			}
		}
	}
	
	private void addRandomLights(int num)
	{
		WorldGenerator G = new WorldGenerator(this);
		
		for(int i = 0; i < num; i++)
		{
			
			int x = (int)(Math.random()*w);
			int y = (int)(Math.random()*h);
			
			G.addLightColor(x, y, Colors.Color_hsv((int)(Math.random()*360), 100, 100, 10));
						
		}
	}
	
	public Pair<Integer, Integer> getPair(int x, int y)
	{
		return new Pair<Integer, Integer>((x + w) % w, (y + h) % h);
	}
	
	// Retrieve the value of the world object at a given location.
	// This method loads chunks from the disk gradually as they are needed.
	public WorldObject getWorldObject(int x, int y)
	{
		if(manual_world)
		ensure_indice_loaded(x, y);
						
		return world[(x + w) % w][(y + h) % h];
	}
	
	// Set a world location to a new world object.
	public void setWorldObject(int x, int y, WorldObject o)
	{
		
		int chunk_x = x / chunk_size;
		int chunk_y = y / chunk_size;
	
		world[x][y] = o;
		
		chunks_modified[chunk_x][chunk_y] = true;
	}
	
	// Saves all modified chunks in the world.
	public void saveWorld()
	{
		if(!manual_world)
		{
			return;
		}
		
		int chunk_h = chunks_modified.length;
		int chunk_w = chunks_modified[0].length;
		
		for(int x = 0; x < chunk_w; x++)
		for(int y = 0; y < chunk_h; y++)
		{
			if(chunks_modified[x][y])
			{
				save_chunk(x, y);
				chunks_modified[x][y] = false;
			}
		}		
	}
	
	// Loads the proper world object into this indice if it is not already loaded.
	private void ensure_indice_loaded(int x, int y)
	{
		int chunk_x = x / chunk_size;
		int chunk_y = y / chunk_size;
		
		if(chunks_loaded[x / chunk_size][y / chunk_size])
		{
			return;
		}
		
		load_chunk(chunk_x, chunk_y);
	}
	
	private void load_chunk(int chunk_x, int chunk_y)
	{
		
		File file = getChunkFile(chunk_x, chunk_y);
		
		// No need to load chunks that do not exist.
		if(!file.exists())
		{
			return;
		}
		
		Iterator<String> iter = FileIO.readFile(file).iterator();
		
		iter.next();// Skip header.
		iter.next();// Skip blank space.

		int chunk_x_start = chunk_x*chunk_size;
		int chunk_y_start = chunk_y*chunk_size;
		
		int chunk_w = new Integer(iter.next());//Math.min(chunk_size, w - chunk_x_start);
		int chunk_h = new Integer(iter.next());//Math.min(chunk_size, h - chunk_y_start);
		
		int chunk_x_end = chunk_x_start + chunk_w;
		int chunk_y_end = chunk_y_start + chunk_h;
		
		
		for(int x = chunk_x_start; x < chunk_x_end; x++)
		for(int y = chunk_y_start; y < chunk_y_end; y++)
		{
			WorldObject o = new WorldObject();
			
			o.serializeFrom(iter);
			world[x][y] = o;
		}
		
		chunks_loaded[chunk_x][chunk_y] = true;
	}
	
	private void save_chunk(int chunk_x, int chunk_y)
	{
		File file = getChunkFile(chunk_x, chunk_y);
		
		// Create the save file.
		FileIO.createFile(file);
				
		// Extract the print stream.
		PrintStream stream = FileIO.getStream(file);
		
		// A message to potential readers.
		stream.println("Chunk");

		// Print a blank space for good measure.
		stream.println();

		int chunk_x_start = chunk_x*chunk_size;
		int chunk_y_start = chunk_y*chunk_size;
		
		int chunk_w = Math.min(chunk_size, w - chunk_x_start);
		int chunk_h = Math.min(chunk_size, h - chunk_y_start);
		
		int chunk_x_end = chunk_x_start + chunk_w;
		int chunk_y_end = chunk_y_start + chunk_h;
		
		// Serialize the width and height of the chunk.
		stream.println(chunk_w);
		stream.println(chunk_h);
		
		for(int x = chunk_x_start; x < chunk_x_end; x++)
		for(int y = chunk_y_start; y < chunk_y_end; y++)
		{
			world[x][y].serializeTo(stream);
		}

		// Close the save file. This is somewhat important for safety reasons.
		FileIO.closeFile(file);
	}
	
	private File getChunkFile(int chunk_x, int chunk_y)
	{
		return FileIO.parseFile("WorldData", "Data" + chunk_x + "_" + chunk_y + ".bco");
	}
		
	
	// Returns a list of all of the neighbors of the world object at the given input location.
	public UBA<Pair<Integer, Integer>> getNeighbors(int x, int y)
	{
		UBA<Pair<Integer, Integer>> output = new UBA<Pair<Integer, Integer>>(4);
		
		WorldObject o = getWorldObject(x, y);
		
		if(!o.left)
		{
			output.add(getPair(x - 1, y));
		}
		
		if(!o.up)
		{
			output.add(getPair(x, y - 1));			
		}
		
		if(!o.down)
		{
			output.add(getPair(x, y + 1));			
		}
		
		if(!o.right)
		{
			output.add(getPair(x + 1, y));			
		}
		
		return output;
	}
}
