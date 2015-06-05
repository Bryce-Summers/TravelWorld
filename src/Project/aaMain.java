package Project;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;


import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_looper;
import Game_Engine.Engine.engine.Game_output;

/*
 *		Starts up the Hoth Level Editor.
 *		This is the start of Something big.
 *		Started on 9 - 27 - 2013.
 *
 *	- Also specifies the global configuration variables for this program.
 *
 *  We need to be careful that actual game rooms run with GUI_DRAW mode disabled.
 */

public class aaMain
{

	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int GAME_W = 1280;
	public static final int GAME_H = 800;
		
	public static void main(String[] args)
	{
		// Set the room directory for this program.
		Room.room_directory = "Project.Editor";// This is bogus.
		
		// Render the GUI images.
		Game_Engine.GUI.SpriteLoader.render1();
		
		// Create an output object for the game.
		Game_output out = new Game_output("Travel Story Game", dim);
		
		out.setColor(Color.BLACK);
		
		//Game_looper.GUI_DRAW_ENABLED = true;
		
		// Start the game.
		//new room_main(out);
		
		
		Spr.Render();
		
		// Start the Editor.
		Room room = new room_world(out);
		

	}
	
}
