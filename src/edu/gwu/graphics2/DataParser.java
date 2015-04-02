/**
 * 
 */
package edu.gwu.graphics2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * This class parses a data (.d) file into a Model object.
 * 
 * @author ian
 *
 */
public class DataParser
{
	private static final Logger logger = Logger.getLogger(DataParser.class.getName());
	
	public PolygonModel parseData(File data) throws ModelParseException
	{
		List<Vector> points;
		List<RefPolygon> polys;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(data));
			String first = reader.readLine();
			StringTokenizer tokenizer = new StringTokenizer(first);
			String tok1 = tokenizer.nextToken();
			if(!tok1.equalsIgnoreCase("data"))
			{
				String msg = "First token in the file is not \"data\", invalid file, exiting.";
				logger.warning(msg);
				throw new ModelParseException(msg);
			}
			String pointsTok = tokenizer.nextToken();
			int numPoints = Integer.parseInt(pointsTok);
			
			String polysTok = tokenizer.nextToken();
			int numPolys = Integer.parseInt(polysTok);
			
			points = readPoints(reader, numPoints);
			polys = readPolygons(reader, numPolys, Collections.unmodifiableList(points));
			PolygonModel mod =  new PolygonModel(data.getName(), points, polys, new LinkedList<Coord>(), Material.DEFAULT_MATERIAL);
			mod.addNormals();
			return mod;
		} catch (Exception e) {
			String msg = e.getClass().getName()+": "+e.getMessage()+", while parsing "+data.getName()+".";
			logger.warning(msg);
			throw new ModelParseException(msg);
		}
	}
	
	private List<Vector> readPoints(BufferedReader reader, int numPoints) throws IOException
	{
		 List<Vector> points = new ArrayList<Vector>(numPoints);
		for(int i=0; i<numPoints; i++)
		{
			String line = reader.readLine();
			StringTokenizer tok = new StringTokenizer(line);
			float x = Float.parseFloat(tok.nextToken());
			float y = Float.parseFloat(tok.nextToken());
			float z = Float.parseFloat(tok.nextToken());
			points.add(new Vector(x, y, z));
			
		}
		return points;
	}
	
	private List<RefPolygon> readPolygons(BufferedReader reader, int numPolys, List<Vector> points) throws IOException
	{
		List<RefPolygon> polys = new ArrayList<RefPolygon>();
		for(int i=0; i<numPolys; i++)
		{
			String line = reader.readLine();
			StringTokenizer tok = new StringTokenizer(line);
			int numPts = Integer.parseInt(tok.nextToken());
			List<Integer> refs = new ArrayList<Integer>(numPts);
			for(int pt=0; pt<numPts; pt++)
			{
				refs.add(Integer.parseInt(tok.nextToken()));
			}
			polys.add(new RefPolygon(refs));
		}
		return polys;
	}
}
