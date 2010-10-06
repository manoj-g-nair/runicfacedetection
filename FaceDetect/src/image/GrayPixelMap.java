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

public class GrayPixelMap extends PixelMap
{
	
//	public GreyPixelMap()
//	{
//		pixMap = null;
//	}
	
	public GrayPixelMap( int width, int height )
	{
		super(width, height, 1);
	}
	
	public GrayPixelMap( InputStream in ) throws IOException
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
			int pixel[] = new int[3];
			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					pixel[0] = 0;
					pixel[1] = 0;
					pixel[2] = 0;
					raster.getPixel(i, j, pixel);
					double p0 = pixel[0];
					double p1 = pixel[1];
					double p3 = pixel[2];
					this.pixMap[i][j][0] =(int) (p0*0.3 + p1*0.59 + 0.11*p3);
					//this.pixMap[i][j] = pixel[0];
				}
			}
//			int debugHelper = 10;
		} 
		catch (IOException e) 
		{
			throw new IOException(e);
		} 
		
	}
	
	
	public void printPixelMap(OutputStream out)
	{
		PrintWriter printer = new PrintWriter(out);
		for(int i = 0; i < this.pixMap.length; i++)
		{
			for(int j = 0; j < this.pixMap[0].length; j++)
			{
				printer.printf("%d\t", pixMap[i][j]);
			}
			printer.println();
			printer.flush();
		}
		printer.close();
	}
	
	public void pixelMapToImage(OutputStream out) throws IOException
	{
		int width = getWidth();
		int height = getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
//		double[] pixel = new double[3];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				raster.setPixel(i, j, getPixel(i,j));
			}
		}
		ImageIO.write(image, "PNG", out);
	}
	
	public static void main(String args[])
	{
		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
//		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat.jpg");
		FileOutputStream fos = null;
//		InputStream in = null;
//		try {
//			in = new FileInputStream("D:/imagem.jpg");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		GrayPixelMap pixMap = null;
		
		try 
		{
			fos = new FileOutputStream("imagemteste.png");
			pixMap = new GrayPixelMap(in);
//			pixMap.printPixMap(System.out);
//			pixMap.pixMapToImage(fos);
			
			GrayPixelMap newPixMap =  PixelMapProcessor.binarizePixMap(pixMap);
			newPixMap.pixelMapToImage(fos);
//			newPixMap.printPixMap(System.out);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public int getLuminance(int coordX, int coordY) 
	{
		return this.pixMap[coordX][ coordY][0];
	}
}
