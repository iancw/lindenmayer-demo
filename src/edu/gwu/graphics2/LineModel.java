package edu.gwu.graphics2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LineModel implements Model{
	private final List<Line> lines;
	
	public LineModel ()
	{
		lines = new LinkedList<Line>();
	}
	
	public void addLine(Line l)
	{
		lines.add(l);
	}
	
	public List<Line> toLines()
	{
		return Collections.unmodifiableList(lines);
	}

	@Override
	public Model cullBackfacing(Vector camera) {
		return this;
	}

	@Override
	/**
	 * Multiplies all points in the Model by the supplied Matrix.  Also multiplies
	 * the axes.  Returns a new Model with the transformed data points.
	 * @param m
	 * @return
	 */
	public Model multiplyAll(Matrix m)
	{
		LineModel model = new LineModel();		
		for(int i=0; i<lines.size(); i++)
		{
			Line l = lines.get(i);
			Line newl = new Line(
					m.multiply(l.getStart()), 0,
					m.multiply(l.getEnd()), 0);
			model.addLine(newl);
		}
		return model;
	}

}
