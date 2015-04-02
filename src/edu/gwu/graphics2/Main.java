/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import edu.gwu.graphics2.ConfigurableShading.ShadingMode;




/**
 * This is the main entry point for the lab 1 program.  It has a few methods
 * for initialization (parsing command line arguments, prompting for inputs
 * from the command prompt, etc).  It also initiates parsing a Model object
 * from a supplied file.
 * 
 * It transfers control to Lab1Display, which provides the window and a gui for
 * camera manipulation.
 * 
 * Most of the interesting graphics work happens in GraphicsPipeline and Rasterizer.
 * Also see the EdgeTable, Entry, and Segment classes, which support the work
 * of the Rasterizer.
 * 
 * This lab features enhanced GUI that lets you drag the mouse in the window to
 * edit the camera position!  Changes in x and y are directly translated into changes
 * to the camera x and y.  The scroll wheel changes the camera's z coordinates.
 * 
 * @author ian
 *
 */
public class Main
{
	private final static Logger logger = Logger.getLogger(Main.class.getName());
	
	private Vector ref = new Vector(0,0,0);
	private Vector camera = new Vector(100, 100, 100); 
	private Vector up = new Vector(0, 1, 0);
	private float d = 10f;
	private float h = 20;
	private float f = 1000;
	private List<PolygonModel> models = new LinkedList<PolygonModel>();
	
	public void parseModels(String[] args)
	{
		DataParser parser = new DataParser();
		int i=1;
		for(String str : args)
		{
			File file = new File(str);
			if(file.exists())
			{
				try
				{
					PolygonModel m = parser.parseData(file);
					m.buildTextureCoords();
					Matrix mat = Matrix.fromRotationY((float)(i*Math.PI/8));
					models.add(m.multiplyAll(mat));
					i++;
				}catch(Exception e)
				{
					logger.warning("Could not parse "+str+", "+e.getClass().getName()+": "+e.getMessage());
				}
			}else
			{
				logger.warning("File "+file.getName()+" does not exist.");
			}
		}
	}
	
	public void parseArguments(String[] args)
	{
		ref = Vector.parseVector(args[0]);
		camera = Vector.parseVector(args[1]);
		up = Vector.parseVector(args[2]);
		
		h = Float.parseFloat(args[3]);
		d = Float.parseFloat(args[4]);
		f = Float.parseFloat(args[5]);
		parseModels(Arrays.copyOfRange(args, 6, args.length));
	}
	
	public void promptForInputs()
	{
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("Enter reference vector (x, y, z): ");
			String refStr = consoleIn.readLine();
			System.out.println("Enter camera vector (x, y, z): ");
			String camStr = consoleIn.readLine();
			System.out.println("Enter up vector (x, y, z): ");
			String upStr = consoleIn.readLine();
			System.out.println("Enter window height (h): ");
			String hStr = consoleIn.readLine();
			System.out.println("Enter near clipping plane (d): ");
			String dStr = consoleIn.readLine();
			System.out.println("Enter far clipping plane (f): ");
			String fStr = consoleIn.readLine();
			
			ref = Vector.parseVector(refStr);
			camera = Vector.parseVector(camStr);
			up = Vector.parseVector(upStr);
			
			h = Float.parseFloat(hStr);
			d = Float.parseFloat(dStr);
			f = Float.parseFloat(fStr);
			
		} catch (IOException e) {
			System.err.println("Error reading from standard in, exiting");
			System.exit(1);
		}
		
	}
	
	public void displayModels()
	{
		LabDisplay display = new LabDisplay();
		LabSettings settings = new LabSettings(
				camera,
				ref, 
				up, 
				d,
				h, 
				f,
				new Light(new Vector(200, 200, 200), Color.white),
				new Vector(.1f, .1f, .1f),
				new Vector(.2f, .2f, .2f),
				new Vector(.7f, .7f, .7f),
				4,
				ShadingMode.Phong, null,
				"AB\n"
				+"A:[F[+FCA][-FCA]]\n"
				+"B:[F[^FCB][&FCB]]\n"
				+"C:'(0.7071)",
				4,
				5,
				45
				);
		display.show(
				new LabController(
						new GraphicsPipeline( settings, new Dimension(500,500)), 
								models,
								//Arrays.asList(new Light[]{new Light(new Vector(100, 100, 100), Color.green)}),
								new PhongIllumination(),
								settings));
	}
	
	
	public static void printUsage()
	{
		System.out.println("Usage:  java -jar lab2.jar [<x_ref, y_ref, z_ref> <x_cam, y_cam, z_cam> <x_up, y_up, z_up> h d f models...]");
		System.out.println("(or)    java -jar lab2.jar <model.d> [model.d ...]");
		System.out.println("        (each subsequent model is rotated by 45 degrees)");
	}
	
	public static void main(String[] args)
	{
		Main main = new Main();
		if(args.length >= 7)
		{
			main.parseArguments(args);		
		}else if(args.length < 1)
		{
		}else
		{
			main.parseModels(args);
			if(main.models.size() == 0)
			{
				System.exit(0);
			}
		}
		
		main.displayModels();
	}

}
