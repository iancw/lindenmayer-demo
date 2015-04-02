/**
 * 
 */
package edu.gwu.graphics2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

/**
 * @author ian
 *
 */
public class TestDataParser
{
	@Test
	public void test() throws URISyntaxException, ModelParseException
	{
		File data = new File(getClass().getResource("atc.d").toURI());
		DataParser parser = new DataParser();
		PolygonModel model = parser.parseData(data);
		assertNotNull("Model was null", model);
		assertEquals("Incorrect number of points", 11577, model.getNumPoints());
		assertEquals("Incorrect number of polygons", 11344, model.getNumPolygons());
	}
}
