package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Graphics2D;

public interface ContextDisplay {

	public void drawLines(Graphics2D g2d);
	
	public void removeHorizontal(int y);
	
	public void removeVertical(int x);
	
	public void addVertical(float x);
	
	public void removeVertical(float x);
	
	public void addVertical(int x);
	
	public void addVertical(float x, Color c);
	
	public void addHorizontal(float y);
	
	public void removeEntry(Entry ent);
	
	public void addEntry(Entry ent, Color c);
	
	public void removeHorizontal(float y);
	
	public void addHorizontal(int y);
}
