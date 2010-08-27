package image;

public class PixelMapProcessor 
{
	public static GreyPixelMap binarizePixMap( GreyPixelMap pixelMap )
	{
		Histogram hist = new Histogram(pixelMap);
		int threshold = Otsu.findThreshold(hist);
		int width = pixelMap.getWidth();
		int height = pixelMap.getHeight();
		GreyPixelMap newPixelMap = new GreyPixelMap(width, height);
		for( int i = 0; i < width; i++)
		{
			for( int j = 0; j < height; j++)
			{
				int pixel = pixelMap.getPixel(i, j);
				int newPixel = pixel >= threshold ? 255 : 0;
				newPixelMap.setPixel(i, j, newPixel);
			}
		}
		return newPixelMap;
	}
}
