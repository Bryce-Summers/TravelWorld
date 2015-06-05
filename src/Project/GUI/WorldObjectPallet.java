package Project.GUI;

import java.io.File;
import java.io.PrintStream;

import util.FileIO;
import Game_Engine.GUI.Components.Input.gui_IntegerInput;
import Game_Engine.GUI.Components.Input.gui_booleanInput;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.levelEditor.rootSerial;
import Project.Spr;
import Project.World;
import Project.WorldObject;

public class WorldObjectPallet extends gui_window
{

	gui_booleanInput up, right, down, left;
	
	TextureButton concrete;
	
	TypeButton hallway;
	TypeButton elevator;
	TypeButton light, fuse, transform, window;
	TypeButton zoom1, zoom2, zoom3;
	TypeButton train;
	
	TypeButton message1, message2, message3, message4;
	
	TypeButton ramp_left, ramp_right;
	
	gui_button save, load;
	World myWorld;
	
	gui_IntegerInput red, green, blue, alpha;
	
	
	public WorldObjectPallet(double x, double y, int w, int h, World W)
	{
		super(x, y, w, h);
		
		myWorld = W;
	}

	@Override
	public void iObjs()
	{
		super.iObjs();
				
		up    = new gui_booleanInput(32, 0, 32, 32);
		right = new gui_booleanInput(64, 32, 32, 32);
		down  = new gui_booleanInput(32, 64, 32, 32);
		left  = new gui_booleanInput(0, 32, 32, 32);
		
		obj_create(up, right, down, left);
		
		ramp_right = new TypeButton(64 + 32, 0, 32, 32, World.RAMP_RIGHT);
		ramp_right.setText("/");
		ramp_left = new TypeButton(64 + 32 + 32, 0, 32, 32, World.RAMP_LEFT);
		ramp_left.setText("\\");
		obj_create(ramp_right, ramp_left);
		
		concrete = new TextureButton(0, 128, 32, 32, World.TEX_CONCRETE);
		concrete.setSprite(Spr.icon_tex_concrete);
		
		hallway = new TypeButton(0, 128 + 32, 64, 32, World.HALLWAY);
		hallway.setText("hall");

		elevator = new TypeButton(64, 128 + 32, 64, 32, World.ELEVATOR);
		elevator.setText("elev");
		
		train = new TypeButton(elevator.getX2(), 128 + 32, 64, 32, World.TRAIN);
		train.setText("Train");
		
		light = new TypeButton(0, 128 + 64, 64, 32, World.LIGHT);
		light.setText("light");
		
		fuse = new TypeButton(64, 128 + 64, 64, 32, World.LIGHT_FUSE);
		fuse.setText("fuse");
		
		transform = new TypeButton(128, 128 + 64, 64, 32, World.LIGHT_TRANSFORM);
		transform.setText("trans");

		window = new TypeButton(0, 128 + 96, 128, 32, World.WINDOW);
		window.setText("window");
	
		obj_create(concrete, hallway, elevator, light, fuse, transform, window);
		obj_create(train);
		
		int y = 256;
		zoom1 = new TypeButton(0, y, 32, 32,  World.ZOOM1);
		zoom1.setText("z1");
		zoom2 = new TypeButton(32, y, 32, 32, World.ZOOM2);
		zoom2.setText("z2");
		zoom3 = new TypeButton(64, y, 32, 32, World.ZOOM3);
		zoom3.setText("z3");
		obj_create(zoom1, zoom2, zoom3);
		
		int x = 64 + 32;
		message1 = new TypeButton(x, y, 32, 32,  World.M1);
		message1.setText("m1");
		x += 32;
		message2 = new TypeButton(x, y, 32, 32, World.M2);
		message2.setText("m2");
		x += 32;
		message3 = new TypeButton(x, y, 32, 32, World.M3);
		message3.setText("m3");
		x += 32;
		message4 = new TypeButton(x, y, 32, 32, World.M4);
		message4.setText("m4");
		x += 32;
		obj_create(message1, message2, message3, message4);
		
		
		// Default texture and type.
		TextureButton.focus = concrete;
		TypeButton.focus = hallway;
		
		
		save = new gui_button(0, 500 - 32, 96, 32);
		save.setText("Save");
		obj_create(save);
			
		load = new gui_button(96, 500 - 32, 96, 32);
		load.setText("Load");
		obj_create(load);
		
		int color_h = 256 + 32;
		red = new gui_IntegerInput(128,   color_h, 128, 32);
		green = new gui_IntegerInput(128, color_h + 32, 128, 32);
		blue = new gui_IntegerInput(128,  color_h + 64, 128, 32);
		alpha = new gui_IntegerInput(128, color_h + 96, 128, 32);
		red.disableNegatives();
		green.disableNegatives();
		blue.disableNegatives();
		alpha.disableNegatives();
		obj_create(red, green, blue, alpha);
		
		gui_label l1, l2, l3, l4;
		l1 = new gui_label(0, color_h, 128, 32);
		l2 = new gui_label(0, color_h + 32, 128, 32);
		l3 = new gui_label(0, color_h + 64, 128, 32);
		l4 = new gui_label(0, color_h + 96, 128, 32);
		
		l1.setText("Red");
		l2.setText("Green");
		l3.setText("Blue");
		l4.setText("Alpha");
		obj_create(l1, l2, l3, l4);
		
	}
	
	public WorldObject getObject()
	{
		// Create a world object.
		WorldObject o = new WorldObject();
		
		// Populate the various values.
		o.texture = TextureButton.focus.getVal();
		o.type = TypeButton.focus.getVal();
		
		o.up    = !up.getInput();
		o.right = !right.getInput();
		o.left  = !left.getInput();
		o.down  = !down.getInput();
		
		o.red   = red.getInput().toInt();
		o.green = green.getInput().toInt();
		o.blue  = blue.getInput().toInt();
		o.alpha = alpha.getInput().toInt();
		
		return o;
	}
	
	public void update()
	{
		super.update();
		
		if(save.flag())
		{
			// Bryce world Object
			saveLevel(new File("data.bwo"));
			return;
		}
		
		if(load.flag())
		{
			loadLevel(new File("data.bwo"));
		}
	}
	
	@rootSerial
	public void saveLevel(File file)
	{
		/*
		// Create the save file.
		FileIO.createFile(file);
				
		// Extract the print stream.
		PrintStream stream = FileIO.getStream(file);
		
		// A message to potential readers.
		stream.println(myWorld.getSerialName());

		// Print a blank space for good measure.
		stream.println();

		// Serialize the level.
		myWorld.serializeTo(stream);

		// Close the save file. This is somewhat important for safety reasons.
		FileIO.closeFile(file);
		*/
		
		// Just save the world data.
		myWorld.data.saveWorld();
	}
	
	// Level loading is now done gradually.
	// FIXME : Implement loading a player state file if desired.
	public void loadLevel(File file)
	{
		//myWorld.serializeFrom(FileIO.readFile(file).iterator());
	}

}
