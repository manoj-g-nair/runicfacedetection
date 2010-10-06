package processing;

import image.GrayPixelMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import extraction.Histogram;

public class EqualizedHistogram extends Histogram 
{
	private int[] cdfGrayFreqMap;
	private int minCdf;
	private int size;
	
	public EqualizedHistogram (GrayPixelMap pixMap)
	{
		super(pixMap);
		cdfGrayFreqMap = new int[greyFreqMap.length];
		size = pixMap.getWidth() * pixMap.getHeight();
		minCdf = size;
		equalize();
	}
	
	public void loadHistogram(GrayPixelMap pixMap)
	{
		super.loadHistogram(pixMap);
		cdfGrayFreqMap = new int[greyFreqMap.length];
		size = pixMap.getWidth() * pixMap.getHeight();
		minCdf = size;
		equalize();
	}
	
	private void equalize()
	{
		int sum = 0;
		for(int i = 0; i < greyFreqMap.length; i++)
		{
			sum = 0;
			for(int j = 0; j <= i; j++)
			{
				sum += greyFreqMap[j];
			}
			cdfGrayFreqMap[i] = sum;
			if(sum < minCdf)
				minCdf = sum;
		}
	}
	
	public int getEqualizedColor(int color)
	{
		double num = cdfGrayFreqMap[color] - minCdf;
		double div = size - minCdf;
		double result = Math.round((num / div) * (cdfGrayFreqMap.length - 1));
		return (int) result;
	}
	
	public static void main(String args[])
	{
		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
		FileOutputStream out = null; 
		FileOutputStream nout = null;
		GrayPixelMap pixMap = null;
		try 
		{
			out = new FileOutputStream("equalizedtest.png");
			nout = new FileOutputStream("notequalizedtest.png");
			pixMap = new GrayPixelMap(in);
			EqualizedHistogram hist = new EqualizedHistogram(pixMap);
			hist.printHistogram(System.out);
			
			GrayPixelMap newPixMap = new GrayPixelMap(pixMap.getWidth(),pixMap.getHeight());
			for(int i = 0; i < newPixMap.getWidth(); i++)
			{
				for(int j = 0; j < newPixMap.getHeight(); j++)
				{
					int value = pixMap.getLuminance(i, j);
					int[] pix = new int[3];
					pix[0] = hist.getEqualizedColor(value);
					newPixMap.setPixel(i, j, pix);
				}
			}
			pixMap.pixelMapToImage(nout);
			newPixMap.pixelMapToImage(out);
//			System.out.println("Threshold: " + threshold);
//			System.out.println("Threshold Value: " + hist.getColorFreq(threshold));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				nout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
