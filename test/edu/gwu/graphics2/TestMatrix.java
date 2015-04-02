/**
 * 
 */
package edu.gwu.graphics2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases came from http://people.hofstra.edu/stefan_waner/realworld/tutorialsf1/frames3_2.html
 * 
 * @author ian
 *
 */
public class TestMatrix
{
	@Test
	public void test()
	{
		Matrix m = new Matrix(new float[][]{{3, -2}});
		Matrix m2 = new Matrix(new float[][]{{4}, {5}});
		
		Matrix m3 = m.multiply(m2);
		
		assertEquals(1, m3.numColumns());
		assertEquals(1, m3.numRows());
		assertEquals(2, m3.get(0, 0), .0001);
	}
	
	@Test
	public void test2()
	{
		Matrix m = new Matrix(new float[][]{{10, 15, 20}});
		Matrix m2 = new Matrix(new float[][]{{3}, {4}, {1}});
		
		Matrix m3 = m.multiply(m2);
		
		assertEquals(1, m3.numColumns());
		assertEquals(1, m3.numRows());
		assertEquals(110, m3.get(0, 0), .0001);
	}
	
	
	@Test
	public void test3()
	{
		Matrix m = new Matrix(new float[][]{{2, 0, -1, 1}});
		Matrix m2 = new Matrix(new float[][]{{1, 5, -7}, {1, 1, 0}, {0, -1, 1}, {2, 0, 0}});
		
		Matrix m3 = m.multiply(m2);
		
		assertEquals(3, m3.numColumns());
		assertEquals(1, m3.numRows());
		assertEquals(4, m3.get(0, 0), .0001);
		assertEquals(11, m3.get(0, 1), .0001);
		assertEquals(-15, m3.get(0, 2), .0001);
	}
	
	@Test
	public void test4()
	{
		Matrix m = new Matrix(new float[][]{{2, 0, -1, 1}, {1, 2, 0, 1}});
		Matrix m2 = new Matrix(new float[][]{{1, 5, -7}, {1, 1, 0}, {0, -1, 1}, {2, 0, 0}});
		
		Matrix m3 = m.multiply(m2);
		
		assertEquals(3, m3.numColumns());
		assertEquals(2, m3.numRows());
		assertEquals(4, m3.get(0, 0), .0001);
		assertEquals(11, m3.get(0, 1), .0001);
		assertEquals(-15, m3.get(0, 2), .0001);
		assertEquals(5, m3.get(1, 0), .0001);
		assertEquals(7, m3.get(1, 1), .0001);
		assertEquals(-7, m3.get(1, 2), .0001);
	}
	
	@Test
	public void test5()
	{
		Matrix m = new Matrix(new float[][]{{2, 0, 0, 0},{1, 0, 0, 1},{3,0,0,2},{0,0,0,1}});
		Vector v = new Vector(2, 3, 4, 1);
		Vector result = m.multiply(v);
		assertEquals(4, result.getX(), .00001);
		assertEquals(3, result.getY(), .0001);
		assertEquals(8, result.getZ(), .0000001);
		assertEquals(1, result.getW(), .0000001);
	}
}
