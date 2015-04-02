package edu.gwu.graphics2;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;


public class Rasterizer {
	private Dimension dimension;
	private Color background;
	private final ContextDisplay canvas;
	private ShaderFactory factory;
	
	public Rasterizer(Dimension dim, Color bg, ShaderFactory shader, ContextDisplay canvas)
	{
		this.dimension = dim;
		this.background = bg;
		this.canvas = canvas;
		this.factory = shader;
	}
	
	/**
	 * 
	 * @param active
	 * @param data
	 * @param y
	 * @param fill
	 * @return entries whose yMax = y, which should be removed from active
	 */
	private void scan(EdgeList active, RasterData data, ZBuffer zBuffer, int y, ShadingModel fill)
	{
		canvas.addHorizontal(y);
		//Fill in pixels
		List<Segment> segments = active.getSegments();
		for(Segment seg : segments)
		{
			float ctxLeft = seg.xLeft();
			float ctxRight = seg.xRight();
			float leftYmax = seg.leftYMax();
			float rightYmax = seg.rightYMax();
			Vector normal = seg.leftNormal();
			Vector endNml = seg.rightNormal();
			Vector color = seg.leftColor();
			Vector endClr = seg.rightColor();
			Coord leftUV = seg.leftTextureCoord();
			Coord rightUV = seg.rightTextureCoord();
			
			
			float deltaX = ctxRight - ctxLeft; 
			
			Vector deltaNml = endNml.subtract(normal).scale(1/deltaX);
			Vector deltaColor = endClr.subtract(color).scale(1/deltaX);
			if(deltaX == 0)
			{
				deltaNml = new Vector(0,0,0,0);
				deltaColor = new Vector(0,0,0,0);
			}
			
			Coord deltaUV = leftUV.computeDelta(rightUV, deltaX);
			
			float zb = seg.zRight(y);
			float z = seg.zLeft(y);
			float deltaZ = (zb-z) / deltaX;
			canvas.addVertical(ctxLeft);
			canvas.addVertical(ctxRight);
			canvas.addHorizontal(leftYmax);
			canvas.addHorizontal(rightYmax);
			
			for(float x=seg.xLeft(); x<seg.xRight(); x++)
			{
				int i_x = (int)Math.floor(x);
				if(i_x < 0)
				{
					i_x = 0; //this fills in polygons that overlap the left side of the screen
				}
				canvas.addVertical(i_x);
				if(leftUV.getU() < 0 || leftUV.getU() > 1)
				{
					leftUV.set((leftUV.getU() % 1.0), leftUV.getV());
				}
				if(leftUV.getV() < 0 || leftUV.getV() > 1)
				{
					leftUV.set(leftUV.getU(), leftUV.getV() % 1.0);
				}
				
				if(zBuffer.set(z, i_x, y))
				{
					data.setPixel(i_x, y, fill.shadePixel(color, normal.normalize(), leftUV));
				}
				canvas.removeVertical(i_x);
				z += deltaZ;
				normal = normal.add(deltaNml);
				color = color.add(deltaColor);
				color = color.capTo(1f);
				leftUV = leftUV.add(deltaUV);
				double adjU = leftUV.getU();
				double adjV = leftUV.getV();
				//test that incrementing u and v didn't go over the bounds
				//So right U and right V aren't necessarily greater
				//than left U and left V.  It all depends on whether 
				//the deltas are negative or not
				//If delta U is decreasing, then rightU should
				//be smaller than left U				
				if(deltaUV.getU() < 0 && leftUV.getU() < rightUV.getU()
						|| deltaUV.getU() > 0 && leftUV.getU() > rightUV.getU())
				{
					adjU = rightUV.getU();
				}
				if(deltaUV.getV() < 0 && leftUV.getV() < rightUV.getV()
						|| deltaUV.getV() > 0 && leftUV.getV() > rightUV.getV())
				{
					adjV = rightUV.getV();
				}
				leftUV.set(adjU, adjV);
			}
			canvas.removeVertical(ctxLeft);
			canvas.removeVertical(ctxRight);
			canvas.removeHorizontal(leftYmax);
			canvas.removeHorizontal(rightYmax);
		}
		canvas.removeHorizontal(y);
	}
	
	private void rasterize(Polygon polygon, ShadingModel fill, RasterData data, ZBuffer zBuffer)
	{
		fill.setPolygon(polygon);
		fill.setMaterial(polygon.getMaterial());
		
		EdgeList active = new EdgeList();
		EdgeTable et = EdgeTable.build(dimension, polygon, fill);
		
		for(int y=0; y<dimension.height; y++)
		{
			EdgeList entries = et.getStartingEntries(y);
			if(entries != null)
			{
				active.addAll(entries);
			}
			scan(active, data, zBuffer, y, fill);
			List<Entry> toRemove = new LinkedList<Entry>();
			for(Entry entry : active)
			{
				if(y >= (int)Math.floor(entry.yMax))
				{
					canvas.addEntry(entry, Color.red);
					toRemove.add(entry);
					canvas.removeEntry(entry);
				}else
				{
					float oldx = entry.x;
					canvas.addVertical(oldx, Color.gray);
					entry.incrementX();
					canvas.addVertical(entry.x);
					entry.incrementZ();
					entry.incrementColor();
					entry.incrementNormal();
					entry.incrementTextureCoordinate();
					canvas.removeVertical(oldx);
					canvas.removeVertical(entry.x);
				}
			}
			active.removeAll(toRemove);
			active.sort();
		}
	}
	
	float findMinZ(List<PolygonModel> models)
	{
		float min = Float.MAX_VALUE;
		for(PolygonModel m : models)
		{
			float thisMin = m.findMinZ();
			if(thisMin < min)
			{
				min = thisMin;
			}
		}
		return min;
	}
	
	float findMaxZ(List<PolygonModel> models)
	{
		float max = Float.MIN_VALUE;
		for(PolygonModel m : models)
		{
			float thisMax = m.findMaxZ();
			if(thisMax > max)
			{
				max = thisMax;
			}
		}
		return max;
	}
	
	public RasterModel rasterize(List<PolygonModel> models, Light light, Vector camera)
	{
		RasterData data = new RasterData(dimension, background);
		ZBuffer zBuf = new ZBuffer(dimension, findMinZ(models), findMaxZ(models));
		//Because of infinite point source and viewer, L and H can be computed once for all
		//models
		ShadingModel shader = factory.create(light, camera);
		for(PolygonModel model : models)
		{
			for(RefPolygon polygon : model.getPolygons())
			{
				Polygon poly = new Polygon(polygon, model);
				rasterize(poly, shader, data, zBuf);
			}
		}
		return data;
	}
}
