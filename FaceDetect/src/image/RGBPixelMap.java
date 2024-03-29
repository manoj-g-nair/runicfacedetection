package image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

import processing.PixelMapProcessor;

public class RGBPixelMap extends PixelMap
{
	private int pixMap[][][];
	
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	
	public RGBPixelMap( int width, int height )
	{
//		pixMap = new int[width][height][3];
		super(width, height, 3);
	}
	
	public RGBPixelMap( InputStream in ) throws IOException
	{
		super(1, 1, 1);
		loadPixelMap(in);
	}
	
	public void loadPixelMap( InputStream in ) throws IOException
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
	
	public int getPixel(int coordX, int coordY, int color)
	{
		int pixel = this.pixMap[coordX][coordY][color];
		return pixel;
	}
	
	public void setPixel(int coordX, int coordY, int value, int color)
	{
		this.pixMap[coordX][coordY][color] = value;	
	}
	
	
	public void printPixelMap(OutputStream out)
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
	
	public GrayPixelMap createGrayPixelMap(int color)
	{
		GrayPixelMap gpm = new GrayPixelMap(getWidth(), getHeight());
		int dim = color >= RED || color <= BLUE ? color : -1;
		int[] value = new int[3];
		for(int i = 0; i < gpm.getWidth(); i++)
		{
			for(int j = 0; j < gpm.getHeight(); j++)
			{
				if(dim == -1)
					value[0] = this.pixMap[i][j][0] + 
						this.pixMap[i][j][1] + this.pixMap[i][j][2]; 
				else
					value[0] = this.pixMap[i][j][dim];
				gpm.setPixel(i, j, value);
			}
		}
		return gpm;
	}
	
	public void mergeGrayPixelMaps(GrayPixelMap gpmR, GrayPixelMap gpmG, GrayPixelMap gpmB)
	{
		for(int i = 0; i < this.getWidth(); i++ )
		{
			for(int j = 0; j < this.getHeight(); j++ )
			{
				this.pixMap[i][j][RED] = gpmR.getPixel(i, j)[0];
				this.pixMap[i][j][GREEN] = gpmG.getPixel(i, j)[0];
				this.pixMap[i][j][BLUE] = gpmB.getPixel(i, j)[0];
			}
		}
	}
	
	public void pixelMapToImage(OutputStream out) throws IOException
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
//			InputStream in = RGBPixelMap.class.getResourceAsStream("/resources/cat.jpg");
//			InputStream in = RGBPixelMap.class.getResourceAsStream("/resources/imagem.jpg");
			RGBPixelMap rgbPM = new RGBPixelMap(in);
			GrayPixelMap gpmR = rgbPM.createGrayPixelMap(RED);
			GrayPixelMap gpmG = rgbPM.createGrayPixelMap(GREEN);
			GrayPixelMap gpmB = rgbPM.createGrayPixelMap(BLUE);
			GrayPixelMap gpmGray = rgbPM.createGrayPixelMap(-1);
			
//			GreyPixelMap newGpmR = PixelMapProcessor.binarizePixMap(gpmR);
//			GreyPixelMap newGpmG = PixelMapProcessor.binarizePixMap(gpmG);
//			GreyPixelMap newGpmB = PixelMapProcessor.binarizePixMap(gpmB);
			
			GrayPixelMap newGpmR = PixelMapProcessor.negativePixMap(gpmR);
			GrayPixelMap newGpmG = PixelMapProcessor.negativePixMap(gpmG);
			GrayPixelMap newGpmB = PixelMapProcessor.negativePixMap(gpmB);
			GrayPixelMap newGpmGray = PixelMapProcessor.negativePixMap(gpmGray);
			
			FileOutputStream redfos = new FileOutputStream("testimagered.png");
			newGpmR.pixelMapToImage(redfos);
			
			FileOutputStream greenfos = new FileOutputStream("testimagegreen.png");
			newGpmG.pixelMapToImage(greenfos);
			
			FileOutputStream bluefos = new FileOutputStream("testimageblue.png");
			newGpmB.pixelMapToImage(bluefos);
			
			FileOutputStream grayfos = new FileOutputStream("testimagegray.png");
			newGpmGray.pixelMapToImage(grayfos);
			
			rgbPM.mergeGrayPixelMaps(newGpmR, newGpmG, newGpmB);
			rgbPM.pixelMapToImage(fos);
			
			redfos.close();
			greenfos.close();
			bluefos.close();
			grayfos.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getLuminance(int coordX, int coordY) 
	{
		double red = this.pixMap[coordX][coordY][RED];
		double green = this.pixMap[coordX][coordY][GREEN];
		double blue = this.pixMap[coordX][coordY][BLUE];
		return (int)(red * 0.3 + green * 0.59 + blue * 0.11);
	}
}
