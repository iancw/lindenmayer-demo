package edu.gwu.graphics2.lindenmayer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.gwu.graphics2.Line;
import edu.gwu.graphics2.LineModel;
import edu.gwu.graphics2.Vector;

/**
 * variables : X F
 * constants : + −
 * start  : X
 * rules  : (X → F-[[X]+X]+F[+FX]-X), (F → FF)
 * angle  : 25°
 * 
 */

public class LindenmayerPanel extends JPanel {
	
	private Map<Character, String> ruleMap;
	
	public LindenmayerPanel()
	{
		ruleMap = new HashMap<Character, String>();
		ruleMap.put('X', "F-[[X]+X]+F[+FX]-X");
		ruleMap.put('F', "FF");
	}
	

	public void paint2D(Graphics g)
	{
		SwingTurtleFactory factory = new SwingTurtleFactory((Graphics2D)g);
		
		LindenmayerGenerator gen = new LindenmayerGenerator(factory,
				3, Math.toRadians(25), 250, 500, 0, -Math.PI/2, -Math.PI/2);
		Lindenmayer lind = new Lindenmayer(ruleMap);
		String prev = "X";
		for(int i=0; i<6; i++)
		{
			prev = lind.iterate(prev);
		}
		
		gen.generate(prev);
	}
	
	
	public void paint(Graphics g)
	{
		LineModelTurtleFactory factory = new LineModelTurtleFactory();
		
		LindenmayerGenerator gen = new LindenmayerGenerator(factory,
				3, Math.toRadians(25), 250, 500, 0, -Math.PI/2, -Math.PI/2);
		Lindenmayer lind = new Lindenmayer(ruleMap);
		String prev = "X";
		for(int i=0; i<6; i++)
		{
			prev = lind.iterate(prev);
		}
		
		gen.generate(prev);
		
		LineModel mode = factory.getModel();
		List<Line> lines = mode.toLines();
		for(Line line : lines)
		{
			Vector start = line.getStart();
			Vector end = line.getEnd();
			g.drawLine(
					(int)start.getX(), (int)start.getY(), 
					(int)end.getX(), (int)end.getY());
		}
	}
	
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.getContentPane().add(new LindenmayerPanel());
		frame.setVisible(true);

	}
}
