package extraction;

import image.PixelMap;

import java.util.ArrayList;

public class CovarianceMatrix 
{
	public static ArrayList<Integer[]> toVector( PixelMap pixMap )
	{
		ArrayList<Integer[]> pixvector = new ArrayList<Integer[]> ();
		int width = pixMap.getWidth();
		int height = pixMap.getHeight();
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				Integer ig[] = new Integer[3];
				int[] pix = pixMap.getPixel(i, j);
				ig[0] = pix[0];
				ig[1] = pix[1];
				ig[2] = pix[2];
				pixvector.add(ig);
			}
		}
		
		return pixvector;
	}
	
	public static Integer[] computeMean(ArrayList<Integer[]> pixvector)
	{
		Integer[] mean = new Integer[3];
		for(int i = 0; i < pixvector.size(); i++)
		{
			Integer[] temp = pixvector.get(i);
			mean[0] += temp[0];
			mean[1] += temp[1];
			mean[2] += temp[2];
		}
		for(int i = 0; i < 3; i++)
		{
			mean[i] /= pixvector.size();
		}
		return mean;
	}
	
	public static double covariance (ArrayList<Integer[]> pixvector, Integer[] mean, int x, int y)
	{
		double covariance = 0f;
		for( int i = 0; i < 3; i++)
		{
			for( int j = 0; j < 3; j++)
			{
				covariance += (pixvector.get(x)[i] - mean[i]) * (pixvector.get(y)[j] - mean[j]);
			}
		}
		return covariance;
	}
	
	
}
