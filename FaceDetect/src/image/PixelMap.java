package image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class PixelMap {
	
	protected int[][][] pixMap;
	
	public PixelMap( int width, int height, int dimension )
	{
		pixMap = new int[width][height][3];
	}
	
	
	public void setPixel(int coordX, int coordY, int[] values)
	{
		this.pixMap[coordX][coordY] = values;
	}

	public int[] getPixel(int coordX, int coordY)
	{
		return this.pixMap[coordX][coordY];
	}
	
	public abstract int getLuminance(int coordX, int coordY);
	
	public int getHeight()
	{
		return this.pixMap[0].length;
	}
	
	public int getWidth()
	{
		return this.pixMap.length;
	}
	
	public int getDimension()
	{
		return this.pixMap[0][0].length;
	}
	
	public abstract void printPixelMap( OutputStream out );
	
	public abstract void loadPixelMap( InputStream in ) throws IOException;
	
	public abstract void pixelMapToImage(OutputStream out) throws IOException;

}
