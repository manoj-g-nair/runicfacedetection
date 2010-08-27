package image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class RGBPixelMap 
{
//	private int pixMap[][][];
//	
//	public enum Color 
//	{
//		RED, GREEN, BLUE
//	}
//	
//	public RGBPixelMap( int width, int height )
//	{
//		pixMap = new int[width][height][3];
//	}
//	
//	public RGBPixelMap( InputStream in ) throws IOException
//	{
//		loadPixMap(in);
//	}
//	
//	public void loadPixMap( InputStream in ) throws IOException
//	{
//		ImageInputStream imgInput = null;
//		
//		try 
//		{
//			imgInput = new FileCacheImageInputStream(in, null);
//			BufferedImage bufImage = ImageIO.read(imgInput);
//			Raster raster = bufImage.getRaster();
//			int width = raster.getWidth();
//			int height = raster.getHeight();
//			this.pixMap = new int[width][height][3];			
//			for(int i = 0; i < width; i++)
//			{
//				for(int j = 0; j < height; j++)
//				{
//					raster.getPixel(i, j, pixMap[i][j]);
//				}
//			}
//		} 
//		catch (IOException e) 
//		{
//			throw new IOException(e);
//		} 
//		
//	}
//	
//	public int[] getPixel(int coordX, int coordY)
//	{
//		return this.pixMap[coordX][coordY];
//	}
//	
//	public int getPixel(int coordX, int coordY, Color color)
//	{
//		int pixel = 0;
//		switch(color)
//		{
//			case RED:
//				pixel = this.pixMap[coordX][coordY][0];
//				break;
//			case GREEN:
//				pixel = this.pixMap[coordX][coordY][1];
//				break;
//			case BLUE:
//				pixel = this.pixMap[coordX][coordY][2];
//				break;
//		}
//		return pixel;
//	}
//	
//	public void setPixel(int coordX, int coordY, int[] values)
//	{
//		this.pixMap[coordX][coordY] = values;
//	}
//	
//	public void setPixel(int coordX, int coordY, int[] values)
//	{
//		this.pixMap[coordX][coordY] = values;
//	}
//	
//	public int getHeight()
//	{
//		return this.pixMap[0].length;
//	}
//	
//	public int getWidth()
//	{
//		return this.pixMap.length;
//	}
//	
//	public void printPixMap(OutputStream out)
//	{
//		PrintWriter printer = new PrintWriter(out);
//		for(int i = 0; i < this.pixMap.length; i++)
//		{
//			for(int j = 0; j < this.pixMap[0].length; j++)
//			{
//				printer.printf("%d\t", pixMap[i][j]);
//			}
//			printer.println();
//			printer.flush();
//		}
//		printer.close();
//	}
//	
//	public void pixMapToImage(OutputStream out) throws IOException
//	{
//		int width = getWidth();
//		int height = getHeight();
//		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//		WritableRaster raster = image.getRaster();
//		double[] pixel = new double[3];
//		for(int i = 0; i < width; i++)
//		{
//			for(int j = 0; j < height; j++)
//			{
//				pixel[0] = getPixel(i, j);
//				raster.setPixel(i, j, pixel);
//			}
//		}
//		ImageIO.write(image, "PNG", out);
//	}
}
