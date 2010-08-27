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

public class GreyPixelMap 
{
	private int pixMap[][];
	
//	public GreyPixelMap()
//	{
//		pixMap = null;
//	}
	
	public GreyPixelMap( int width, int height )
	{
		pixMap = new int[width][height];
	}
	
	public GreyPixelMap( InputStream in ) throws IOException
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
			this.pixMap = new int[width][height];
			int pixel[] = new int[3];
			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					pixel[0] = 0;
					pixel[1] = 0;
					pixel[2] = 0;
					raster.getPixel(i, j, pixel);
					this.pixMap[i][j] = (pixel[0] + pixel[1] + pixel[2])/3;
					//this.pixMap[i][j] = pixel[0];
				}
			}
			int debugHelper = 10;
		} 
		catch (IOException e) 
		{
			throw new IOException(e);
		} 
		
	}
	
	public int getPixel(int coordX, int coordY)
	{
		return this.pixMap[coordX][coordY];
	}
	
	public void setPixel(int coordX, int coordY, int value)
	{
		this.pixMap[coordX][coordY] = value;
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
				printer.printf("%d\t", pixMap[i][j]);
			}
			printer.println();
			printer.flush();
		}
		printer.close();
	}
	
	public void pixMapToImage(OutputStream out) throws IOException
	{
		int width = getWidth();
		int height = getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		double[] pixel = new double[3];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				pixel[0] = getPixel(i, j);
				raster.setPixel(i, j, pixel);
			}
		}
		ImageIO.write(image, "PNG", out);
	}
	
	public static void main(String args[])
	{
		InputStream in = GreyPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
		FileOutputStream fos = null;
//		InputStream in = null;
//		try {
//			in = new FileInputStream("D:/imagem.jpg");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		GreyPixelMap pixMap = null;
		
		try 
		{
			fos = new FileOutputStream("imagemteste.png");
			pixMap = new GreyPixelMap(in);
//			pixMap.printPixMap(System.out);
//			pixMap.pixMapToImage(fos);
			
			GreyPixelMap newPixMap =  PixelMapProcessor.binarizePixMap(pixMap);
			newPixMap.pixMapToImage(fos);
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
}
