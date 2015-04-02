package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

public class EdgeTable {
	
	/**
	 * If last and line are monotonic, this returns a new value for last
	 * that is adjusted to be one scan line offset to insure proper
	 * every-other scanning.
	 * @param last previous line
	 * @param line next line
	 * @return adjusted line, a replacement for last
	 */
	private static Line adjustLine(Line last, Line line)
	{

		if(line.getBottom().equals(last.getTop()))
		{
			//line is monotonically increasing
			Vector top = last.getTop();
			return new Line(
					new Vector(top.getX(), top.getY()-1, top.getZ()), last.getTopReference(),
					last.getBottom(), last.getBottomReference());
		}else if(line.getTop().equals(last.getBottom()))
		{
			//line is monotonically decreasing
			Vector bottom = last.getBottom();
			return new Line(
					new Vector(bottom.getX(), bottom.getY()+1, bottom.getZ()), last.getBottomReference(),
					last.getTop(), last.getTopReference());
		}else
		{
			return last;
		}
	}
	
	/**
	 * Adjusts lines to insure they are all monotonically increasing,
	 * and thus have no gaps
	 */
	private static List<Line> getAdjustedLines(Polygon polygon)
	{
		List<Line> adjusted = new LinkedList<Line>();
		Line last = null;
		for(Line line : polygon.toLines())
		{
			if(last != null)
			{
				adjusted.add(adjustLine(last, line));
			}
			last = line;
		}
		Line first = polygon.toLines().get(0);
		adjusted.add(adjustLine(last, first));
		return adjusted;
	}
	
	public static EdgeTable build(Dimension view, Polygon polygon, ShadingModel shading)
	{
		EdgeTable et = new EdgeTable(view.height);
		List<Line> adjusted = getAdjustedLines(polygon);
		for(Line line : adjusted)
		{
			if(!line.isHorizontal() && line.isWithin(view))
			{
				//Insure line.start has the lowest y
				if(line.getEnd().getY() < line.getStart().getY())
				{
					line= line.flip();
				}
				Vector startNorm = polygon.getVertexNormal(line.getStartReference());
				Vector endNorm = polygon.getVertexNormal(line.getEndReference());
				Coord startText = polygon.getTextureCoordinate(line.getStartReference());
				Coord endText = polygon.getTextureCoordinate(line.getEndReference());
				Entry entry = new Entry(line, 
						startNorm, 
						endNorm, 
						shading.getColor(startNorm), 
						shading.getColor(endNorm),
						startText, 
						endText);
				if(et.table[entry.y] == null)
				{
					et.table[entry.y] = new EdgeList();
				}
				
				et.table[entry.y].add(entry);
			}
		}
		return et;
	}
	
	private final EdgeList[] table;
	
	private EdgeTable(int height)
	{
		table = new EdgeList[height];
	}
	
	/**
	 * 
	 * @param y
	 * @return null if no entry exists at the given y, otherwise 
	 * a sorted linked list of table entries
	 */
	public EdgeList getStartingEntries(int y)
	{
		if(y >= 0 && y < table.length)
		{
			return table[y];
		}
		return null;
	}

}
