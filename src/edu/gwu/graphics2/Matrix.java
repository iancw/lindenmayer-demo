/**
 * 
 */
package edu.gwu.graphics2;

/**
 * @author ian
 *
 */
public class Matrix
{
	public static Matrix fromUVN(Vector U, Vector V, Vector N)
	{
		return new Matrix(new float[][]{
				{U.getX(), U.getY(), U.getZ(), 0},
				{V.getX(), V.getY(), V.getZ(), 0},
				{N.getX(), N.getY(), N.getZ(), 0},
				{0, 0, 0, 1}});
	}
	
	public static Matrix fromHDF(float h, float d, float f)
	{
		return new Matrix(new float[][]{
				{d/h, 0, 0, 0},
				{0, d/h, 0, 0},
				{0, 0, f/(f-d), -d*f/(f-d)},
				{0, 0, 1, 0}
		});
	}
	
	public static Matrix fromTranslationVector(Vector vect)
	{
		return new Matrix(new float[][]{
				{1, 0, 0, -1*vect.getX()},
				{0, 1, 0, -1*vect.getY()},
				{0, 0, 1, -1*vect.getZ()},
				{0, 0, 0, 1}
		});
	}
	

	public static Matrix fromScale(Vector scale)
	{
		return new Matrix(new float[][]{
              {scale.getX(), 0, 0, 0},
              {0, scale.getY(), 0, 0},
              {0, 0, scale.getZ(), 0},
              {0, 0, 0, 1}
              });
	}
	
	public static Matrix fromRotationY(float theta)
	{
		return new Matrix(new float[][]{
              {(float)Math.cos(theta), 0, (float)Math.sin(theta), 0},
              {0, 1, 0, 0},
              {(float)-1f*(float)Math.sin(theta), 0, (float)Math.cos(theta), 0},
              {0, 0, 0, 1}
              });
	}
	

	public static Matrix fromRotationZ(float phi)
	{
		return new Matrix(new float[][]{
              {(float)Math.cos(phi), (float)-Math.sin(phi), 0, 0},
              {(float)Math.sin(phi), (float)Math.cos(phi), 0, 0},
              {0, 0, 1, 0},
              {0, 0, 0, 1}
              });
	}
	
	
	/**
	 * Stored in row major order, [row][column]
	 */
	private final float[][] values;
	
	/**
	 * 
	 * @param vals a matrix in row-major form ([row][column])
	 */
	public Matrix(float[][] vals)
	{
		this.values = vals;
	}
	
	public int numRows()
	{
		return values.length;
	}
	
	public int numColumns()
	{
		if(values.length > 0)
		{
			return values[0].length;
		}else
		{
			return 0;
		}
	}
	
	public Vector rowVector(int row)
	{
		if(row < 0 || row > values.length)
		{
			throw new IllegalArgumentException("Illegal row number "+row);
		}
		return new Vector(values[row]);
	}
	
	public Vector colVector(int col)
	{
		if(col < 0 || col > values.length){
			throw new IllegalArgumentException("Illegal column number "+col);
		}
		float[] colvals = new float[numRows()];
		for(int i=0; i<numRows(); i++)
		{
			colvals[i] = values[i][col];
		}
		return new Vector(colvals);
	}
	
	public float get(int row, int col)
	{
		return values[row][col];
	}
	
	public Line multiply(Line other)
	{
		return new Line(
				multiply(other.getStart()).toCartesian(), 
				multiply(other.getEnd()).toCartesian());
	}
	
	public Vector multiply(Vector other)
	{
		Matrix mult = multiply(new Matrix(new float[][]{
				{other.getX()},
				{other.getY()},
				{other.getZ()},
				{1}}));
		return mult.colVector(0);
	}
	
	public Matrix multiply(Matrix other)
	{
		if(this.numColumns() != other.numRows())
		{
			throw new IllegalArgumentException("Can not multiply matrices with different column and row dimensions");
		}
		float[][] newMatr = new float[this.numRows()][other.numColumns()];
		for(int row = 0; row<this.numRows(); row++)
		{
			for(int col=0; col<other.numColumns(); col++)	
			{
				newMatr[row][col] = this.rowVector(row).dot(other.colVector(col));
			}
		}
		return new Matrix(newMatr);
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int i=0; i<numRows(); i++)
		{
			str.append("[");
			for(int j=0; j<numColumns(); j++)
			{
				str.append(get(i, j));
				if(j != numColumns()-1)
				{
					str.append(",");
				}
			}
			str.append("]");
			if(i != numRows()-1)
			{
				str.append(",\n");
			}
		}
		str.append("]");
		return str.toString();
	}
}
