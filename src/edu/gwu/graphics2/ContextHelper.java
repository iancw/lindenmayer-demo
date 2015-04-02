package edu.gwu.graphics2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

public class ContextHelper implements ContextDisplay{

	public static class Context
	{
		static Context fromX(int x, int height)
		{
			Context ctx = new Context();
			ctx.s_x = x;
			ctx.e_x = x;
			ctx.s_y = 0;
			ctx.e_y = height;
			ctx.color = Color.black;
			ctx.stroke = new BasicStroke(1);
			return ctx;
		}
		
		static Context fromY(int y, int width)
		{
			Context ctx = new Context();
			ctx.s_x = 0;
			ctx.e_x = width;
			ctx.s_y = y;
			ctx.e_y = y;
			ctx.color = Color.black;
			ctx.stroke = new BasicStroke(1);
			return ctx;
		}
		
		int s_x;
		int s_y;
		int e_x;
		int e_y;
		Color color;
		Stroke stroke;
		
		@Override
		public boolean equals(Object o)
		{
			if(o instanceof Context)
			{
				Context other = (Context)o;
				return s_x == other.s_x && s_y == other.s_y
				&& e_x == other.e_x && e_y == other.e_y;
			}
			return false;
		}
	}

	
	private final List<Context> contextLines;
	private final Dimension dimension;
	private final SwingCanvas canvas;
	
	public ContextHelper(SwingCanvas canvas)
	{
		this.contextLines = new CopyOnWriteArrayList<Context>();
		this.canvas = canvas;
		this.dimension = canvas.getSize();
	}

	public void removeVertical(int x)
	{
		contextLines.remove(Context.fromX(x, dimension.height));
		repaintInSwing();
	}
	

	public void addVertical(int x)
	{
		contextLines.add(Context.fromX(x, dimension.height));
		repaintInSwing();
	}
	
	public void addVertical(float x, Color c)
	{
		Context ctx = Context.fromX((int)x, dimension.height);
		ctx.color = c;
		contextLines.add(ctx);
		repaintInSwing();
	}
	

	public void removeEntry(Entry ent)
	{
		contextLines.remove(Context.fromX((int)ent.x, dimension.height));
		contextLines.remove(Context.fromY((int)ent.yMax, dimension.width));
		repaintInSwing();
	}
	
	public void addEntry(Entry ent, Color c)
	{
		Context ctxX = Context.fromX((int)ent.x, dimension.height);
		ctxX.color = c;
		ctxX.stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{1f, 0f}, 0f);
		contextLines.add(ctxX);
		Context ctxY = Context.fromY((int)ent.yMax, dimension.width);
		ctxY.color = c;
		ctxY.stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{1f, 0f}, 0f);
		contextLines.add(ctxY);
		repaintInSwing();

	}
	

	public void addHorizontal(int y)
	{
		contextLines.add(Context.fromY(y, dimension.width));
		repaintInSwing();
	}
	
	public void removeHorizontal(int y)
	{
		contextLines.remove(Context.fromY(y, dimension.width));
		repaintInSwing();
	}
	
	
	public void addVertical(float x)
	{
		addVertical((int)x);
	}
	
	public void removeVertical(float x)
	{
		removeVertical((int)x);
	}
	
	public void addHorizontal(float y)
	{
		addHorizontal((int)y);
	}
	
	public void removeHorizontal(float y)
	{
		removeHorizontal((int)y);
	}
	
	private void repaintInSwing()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				canvas.repaint();
			}
		});
	}
	
	public void drawLines(Graphics2D g2d)
	{
		for(Context ctx : contextLines)
		{
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(ctx.color);
			g2d.setStroke(ctx.stroke);
			g2d.drawLine(ctx.s_x, ctx.s_y, ctx.e_x, ctx.e_y);
			g2d.setStroke(oldStroke);
		}
	}
}
