package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class SwingCanvas extends JPanel implements DisplayHandler
{
	private final Dimension dimension;
	private BufferedImage image;
	private ContextDisplay helper;
	private AtomicBoolean wireframe;
	private AtomicBoolean fill;
	private AtomicBoolean drawLight;
	
	public SwingCanvas(Dimension dim)
	{
		this.dimension = dim;
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		helper = new NoContext();
		wireframe = new AtomicBoolean(false);
		fill = new AtomicBoolean(true);
		drawLight = new AtomicBoolean(false);
	}
	
	public void setWireframe(boolean val)
	{
		wireframe.set(val);
	}
	
	public void setPolygonFill(boolean val)
	{
		fill.set(val);
	}
	
	public void setDisplayZBuffer(boolean val)
	{
		if(val)
		{
			helper = new ContextHelper(this);
		}else
		{
			helper = new NoContext();
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, 0, 0, null);
		this.helper.drawLines(g2d);
	}
	
	@Override
	public void renderModels(List<Model> models, Light light, Vector worldSpaceCamera, ShaderFactory shader) {
		BufferedImage nImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = nImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, dimension.width, dimension.height);
		
		List<PolygonModel> polys = new LinkedList<PolygonModel>();
		for(Model m : models)
		{
			renderLines(g, m.toLines());
			if(m instanceof PolygonModel)
			{
				polys.add((PolygonModel)m);
			}
		}
		
		if(fill.get())
		{
			
			Rasterizer raster = new Rasterizer(dimension, Color.white, shader, helper);
			renderModel(raster.rasterize(polys, light, worldSpaceCamera), g);
		}
		
		for(Model m : models)
		{
			if(m instanceof LineModel)
			{
				renderLines(g, ((LineModel)m).toLines());
			}
		}

//		for(PolygonModel model : models)
//		{
//			if(wireframe.get())
//			{
//				renderLines(g, model.toLines());
//			}
//			//renderLines(g, model.getNormals());
//			drawLight.set(false);
//			if(drawLight.get())
//			{
//				Line vector = model.getLightVector();
//				Vector p0 = model.getLightVector().getStart();
//				Vector p1 = model.getLightVector().getEnd();
//				Color c = g.getColor();
//				g.setColor(Color.black);
//				g.drawLine((int)p0.getX(), (int)p0.getY(), (int)p1.getX(), (int)p1.getY());
//				g.setColor(c);
//			}
//		}
		image = nImage;
	}
	
	public void renderLines(Graphics g, List<Line> lines)
	{
		Color colors[] = new Color[]{Color.red, Color.green, Color.blue, Color.magenta};
		int i=0;
        
		for(Line line : lines)
		{
			Vector p0 = line.getStart();
			Vector p1 = line.getEnd();
			g.setColor(colors[i]);
			g.drawLine((int)p0.getX(), (int)p0.getY(), (int)p1.getX(), (int)p1.getY());
			i = (i+1) % 4;
		}
	}
	
	public void renderModel(RasterModel model, Graphics g) {

		for(int x=0; x<dimension.width; x++)
		{
			for(int y=0; y<dimension.height; y++)
			{
				Color c = model.getColor(x, y);
				g.setColor(c);
				g.fillRect(x, y, 1, 1);
			}
		}
	}
}
