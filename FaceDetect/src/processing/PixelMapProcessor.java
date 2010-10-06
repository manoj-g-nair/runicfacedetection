package processing;

import extraction.Histogram;
import image.GrayPixelMap;

public class PixelMapProcessor 
{
	public static GrayPixelMap binarizePixMap( GrayPixelMap pixelMap )
	{
		Histogram hist = new Histogram(pixelMap);
		int threshold = Otsu.findThreshold(hist);
		int width = pixelMap.getWidth();
		int height = pixelMap.getHeight();
		GrayPixelMap newPixelMap = new GrayPixelMap(width, height);
		for( int i = 0; i < width; i++)
		{
			for( int j = 0; j < height; j++)
			{
				int pixel = pixelMap.getPixel(i, j)[0];
				int[] newPixel = new int[3]; 
				newPixel[0] = pixel >= threshold ? 255 : 0;
				newPixelMap.setPixel(i, j, newPixel);
			}
		}
		return newPixelMap;
	}
	
	public static GrayPixelMap negativePixMap(GrayPixelMap pixelMap)
	{
		int width = pixelMap.getWidth();
		int height = pixelMap.getHeight();
		GrayPixelMap newPixelMap = new GrayPixelMap(width, height);
		for( int i = 0; i < width; i++)
		{
			for( int j = 0; j < height; j++)
			{
				int pixel = pixelMap.getPixel(i, j)[0];
				int[] newPixel = new int[3];
				newPixel[0] = 255 - pixel;
				newPixelMap.setPixel(i, j, newPixel);
			}
		}
		return newPixelMap;
	}
}
