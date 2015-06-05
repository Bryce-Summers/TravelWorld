package Project;

import java.awt.Color;

import Data_Structures.Structures.List;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.HashingClasses.Set;

public class WorldGenerator
{

	private Set<Pair<Integer, Integer>> visited;
	private List<Pair<Integer, Integer>> frontier;
	private List<Pair<Integer, Integer>> frontier2;
	
	private WorldIO world;
	
	public WorldGenerator(WorldIO data)
	{
		this.world = data;
		
		visited    = new Set<Pair<Integer, Integer>>();
		frontier   = new List<Pair<Integer, Integer>>();
		frontier2  = new List<Pair<Integer, Integer>>();
	}
	
	
	public void addLightColor(int x, int y, Color c)
	{
		frontier.clear();
		frontier.push(world.getPair(x, y));
		
		visited.clear();
		
		int weight = 100;
		
		
		int c_red   = c.getRed();
		int c_green = c.getGreen();
		int c_blue  = c.getBlue();
		int c_alpha = c.getAlpha();
		
		
		while(weight > 0)
		{
			for(Pair<Integer, Integer> p : frontier)
			{
							
				WorldObject o = world.getWorldObject(p.getKey(), p.getVal());
				
				int w_2 = 100 - weight;
				
				o.red   = (c_red*weight + o.red*w_2)/100;
				o.green = (c_green*weight + o.green*w_2)/100;
				o.blue  = (c_blue*weight + o.blue*w_2)/100;
				o.alpha = (c_alpha*weight + o.alpha*w_2)/100;
			}
			
			// Iterative deepening to expand the frontier.
			deepen();
			
			weight -= 10;

		}
		
	}
	
	// Expands the frontier by 1 level using iterative deepening.
	private void deepen()
	{
		frontier2.clear();
		
		for(Pair<Integer, Integer> p : frontier)
		{
			visited.set_add(p);
			
			Iterable<Pair<Integer, Integer>> neighbors = world.getNeighbors(p.getKey(), p.getVal());
			
			//System.out.println(neighbors);
			
			// Add all new unvisited neighbors to the frontier.
			for(Pair<Integer, Integer> n : neighbors)
			{
				if(!visited.includes(n))
				{
					frontier2.add(n);
				}
			}
		}

		// Swap the references.
		List<Pair<Integer, Integer>> temp = frontier;
		frontier =  frontier2;
		frontier2 = temp;
		

	}


	
}
