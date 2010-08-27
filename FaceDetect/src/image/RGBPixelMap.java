package image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class RGBPixelMap 
{
	private int pixMap[][][];
	
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	
	public RGBPixelMap( int width, int height )
	{
		pixMap = new int[width][height][3];
	}
	
	public RGBPixelMap( InputStream in ) throws IOException
	{
		loadPixMap(in);
	}
	
	public void loadPixMap( InputStream in ) throws IOException
	{
		ImageInputStream imgInput = null;
		
		try 
		{
			imgInput = new FileCacheImageInputStream(in, null);
			BufferedImage bufImage = ImageIO.read(imgInput);
			Raster raster = bufImage.getRaster();
			int width = raster.getWidth();
			int height = raster.getHeight();
			this.pixMap = new int[width][height][3];			
			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					raster.getPixel(i, j, pixMap[i][j]);
				}
			}
		} 
		catch (IOException e) 
		{
			throw new IOException(e);
		} 
		
	}
	
	public int[] getPixel(int coordX, int coordY)
	{
		return this.pixMap[coordX][coordY];
	}
	
	public int getPixel(int coordX, int coordY, int color)
	{
		int pixel = this.pixMap[coordX][coordY][color];
		return pixel;
	}
	
	public void setPixel(int coordX, int coordY, int[] values)
	{
		this.pixMap[coordX][coordY] = values;
	}
	
	public void setPixel(int coordX, int coordY, int value, int color)
	{
		this.pixMap[coordX][coordY][color] = value;	
	}
	
	public int getHeight()
	{
		return this.pixMap[0].length;
	}
	
	public int getWidth()
	{
		return this.pixMap.length;
	}
	
	public void printPixMap(OutputStream out)
	{
		PrintWriter printer = new PrintWriter(out);
		for(int i = 0; i < this.pixMap.length; i++)
		{
			for(int j = 0; j < this.pixMap[0].length; j++)
			{
				printer.printf("%3d,%3d,%3d\t", pixMap[i][j][0], 
						pixMap[i][j][1], pixMap[i][j][2]);
			}
			printer.println();
			printer.flush();
		}
		printer.close();
	}
	
	public GreyPixelMap createGrayPixelMap(int color)
	{
		GreyPixelMap gpm = new GreyPixelMap(getWidth(), getHeight());
		int dim = color >= RED || color <= BLUE ? color : RED;
		for(int i = 0; i < gpm.getWidth(); i++)
		{
			for(int j = 0; j < gpm.getHeight(); j++)
			{
				int value = this.pixMap[i][j][dim];
				gpm.setPixel(i, j, value);
			}
		}
		return gpm;
	}
	
	public void mergeGrayPixelMaps(GreyPixelMap gpmR, GreyPixelMap gpmG, GreyPixelMap gpmB)
	{
		for(int i = 0; i < this.getWidth(); i++ )
		{
			for(int j = 0; j < this.getHeight(); j++ )
			{
				this.pixMap[i][j][RED] = gpmR.getPixel(i, j);
				this.pixMap[i][j][GREEN] = gpmG.getPixel(i, j);
				this.pixMap[i][j][BLUE] = gpmB.getPixel(i, j);
			}
		}
	}
	
	public void pixMapToImage(OutputStream out) throws IOException
	{
		int width = getWidth();
		int height = getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		int[] pixel = new int[3];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				pixel = getPixel(i, j);
				raster.setPixel(i, j, pixel);
			}
		}
		ImageIO.write(image, "PNG", out);
	}
	
	public static void main(String args[])
	{
		try {
			FileOutputStream fos = new FileOutputStream("testimagergb.png");
			InputStream in = RGBPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
			RGBPixelMap rgbPM = new RGBPixelMap(in);
			GreyPixelMap gpmR = rgbPM.createGrayPixelMap(RED);
			GreyPixelMap gpmG = rgbPM.createGrayPixelMap(GREEN);
			GreyPixelMap gpmB = rgbPM.createGrayPixelMap(BLUE);
			
			GreyPixelMap newGpmR = PixelMapProcessor.binarizePixMap(gpmR);
			GreyPixelMap newGpmG = PixelMapProcessor.binarizePixMap(gpmG);
			GreyPixelMap newGpmB = PixelMapProcessor.binarizePixMap(gpmB);
			
			rgbPM.mergeGrayPixelMaps(newGpmR, newGpmG, newGpmB);
			rgbPM.pixMapToImage(fos);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
