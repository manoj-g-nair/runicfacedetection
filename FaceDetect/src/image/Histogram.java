package image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class Histogram 
{
	private int greyFreqMap[];
	
	public Histogram(GreyPixelMap pixMap)
	{
		greyFreqMap = new int[256];
		loadHistogram(pixMap);
	}
	
	public void loadHistogram(GreyPixelMap pixMap)
	{
		Arrays.fill(greyFreqMap, 0);
		int width = pixMap.getWidth();
		int height = pixMap.getHeight();
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				int pixel = pixMap.getPixel(i, j);
				greyFreqMap[pixel]++;
			}
		}
	}
	
	public void printHistogram(OutputStream out)
	{
		PrintWriter printer = new PrintWriter(out);
		for(int i = 0; i < this.greyFreqMap.length; i++)
		{
			printer.printf("%d,", greyFreqMap[i]);
			printer.flush();
		}
		printer.println();
		printer.flush();
//		printer.close();
	}
	
	public int getColorFreq(int x)
	{
		return this.greyFreqMap[x];
	}
	
	public int getHistogramSize()
	{
		return this.greyFreqMap.length;
	}
	 
	public static void main(String args[])
	{
		InputStream in = GreyPixelMap.class.getResourceAsStream("/resources/cat.jpeg");
		GreyPixelMap pixMap = null;
		try 
		{
			pixMap = new GreyPixelMap(in);
			Histogram hist = new Histogram(pixMap);
			hist.printHistogram(System.out);
			int threshold = Otsu.findThreshold(hist);
			System.out.println("Threshold: " + threshold);
			System.out.println("Threshold Value: " + hist.getColorFreq(threshold));
			
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
		}
	}
}
