package extraction;

import image.GrayPixelMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Moments 
{
	public static double extractRawMoment(GrayPixelMap pixMap, int p, int q)
	{
		double moment = 0f;
		for(int i = 0; i < pixMap.getWidth(); i++)
		{
			for(int j = 0; j < pixMap.getHeight(); j++)
			{
				double xVal = Math.pow(i, p);
				double yVal = Math.pow(j, q);
				double fVal = pixMap.getPixel(i, j)[0];
				moment += xVal * yVal * fVal;
			}
		}
		
		return moment;
	}
	
	enum Centroid 
	{
		X, Y
	}
	
	public static double extractCentroid(GrayPixelMap pixMap, Centroid cent)
	{
		double result = 0f;
		double m00 = extractRawMoment(pixMap, 0, 0);
		
		switch(cent)
		{
			case X:
			{
				double m10 = extractRawMoment(pixMap,1,0);
				result = m10 / m00;
				break;
			}
			case Y:
			{
				double m01 = extractRawMoment(pixMap,0,1);
				result = m01 / m00;
				break;
			}
		}
		
		return result;
	}
	
	public static double extractCentralMoment(GrayPixelMap pixMap, 
			double centX, double centY, int p, int q)
	{
		
		double moment = 0f;
		for(int i = 0; i < pixMap.getWidth(); i++)
		{
			for(int j = 0; j < pixMap.getHeight(); j++)
			{
				double xVal = Math.pow((i - centX), p);
				double yVal = Math.pow((j - centY), q);
				double fVal = pixMap.getPixel(i, j)[0];
				moment += xVal * yVal * fVal;
			}
		}
		return moment;
	}
	
	public static double scaleInvariantMoment(GrayPixelMap pixMap, double centX, double centY, double u00, int i, int j)
	{
		double result = 0f;
		double gama = Math.pow(u00,  ( 1 + (i+j)/2 )  );
		if( i == 0 && j == 0 )
			result = u00 / gama;
		else
		{
			double uij = extractCentralMoment(pixMap, centX, centY, i, j);
			result = uij / u00;
		}
		return result;
	}
	
	public static ArrayList<ArrayList<Double>> thirdOrderScaleInvariantSet(GrayPixelMap pixMap)
	{
		double centX = extractCentroid(pixMap, Centroid.X);		
		double centY = extractCentroid(pixMap, Centroid.Y);
		double u00 = extractCentralMoment(pixMap, centX, centY, 0, 0);
		
		ArrayList<ArrayList<Double>> momentMatrix = new ArrayList<ArrayList<Double>> ();
		ArrayList<Double> momentVector = null;
		for( int i = 0; i <= 3; i++)
		{
			momentVector = new ArrayList<Double>();
			for(int j = 0; (i + j) <= 3; j++)
			{
				double nij = scaleInvariantMoment(pixMap, centX, centY, u00, i, j);
				momentVector.add(nij);
			}
			momentMatrix.add(momentVector);
		}
		return momentMatrix;
	}
	
	public static ArrayList<Double> extractHuSet(GrayPixelMap pixMap)
	{
		ArrayList<ArrayList<Double>> n = Moments.thirdOrderScaleInvariantSet(pixMap);
		
		double i1 = n.get(2).get(0) + n.get(0).get(2);
		double i2 = Math.pow(i1, 2) + Math.pow(2*n.get(1).get(1),2);
		double i3 = Math.pow( n.get(3).get(0) - 3 * n.get(1).get(2) , 2 ) +
					Math.pow( 3 * n.get(2).get(1) - n.get(0).get(3), 2);
		double i4 = Math.pow( n.get(3).get(0) - n.get(1).get(2) , 2 ) +
					Math.pow( n.get(2).get(1) - n.get(0).get(3), 2);
		
		double i5 = (n.get(3).get(0) - 3 * n.get(1).get(2)) *
					(n.get(3).get(0) + n.get(1).get(2)) *
					(
						Math.pow(n.get(3).get(0) + n.get(1).get(2), 2) -
						3 * Math.pow( n.get(2).get(1) + n.get(0).get(3) , 2)
					) +
					(3 * n.get(2).get(1) + n.get(0).get(3)) *
					(n.get(2).get(1) + n.get(0).get(3)) *
					(
						3 * Math.pow( n.get(3).get(0) + n.get(1).get(2), 2) -
						Math.pow( n.get(2).get(1) + n.get(0).get(3) , 2)
					);
		
		double i6 = i1 *
					(
						Math.pow( n.get(3).get(0) + n.get(1).get(2), 2) -
						Math.pow( n.get(2).get(1) + n.get(0).get(3) , 2)
					) + 
					4 * n.get(1).get(1) *
					(n.get(3).get(0) + n.get(1).get(2)) *
					(n.get(2).get(1) + n.get(0).get(3))
					;
		
		double i7 = (3 * n.get(2).get(1) - n.get(0).get(3)) *
					(n.get(3).get(0) + n.get(1).get(2)) *
					(
						Math.pow(n.get(3).get(0) + n.get(1).get(2), 2) -
						3 * Math.pow( n.get(2).get(1) + n.get(0).get(3) , 2)
					) +
					(n.get(3).get(0) + 3 * n.get(1).get(2)) *
					(n.get(2).get(1) + n.get(0).get(3)) *
					(
						3 * Math.pow( n.get(3).get(0) + n.get(1).get(2), 2) -
						Math.pow( n.get(2).get(1) + n.get(0).get(3) , 2)
					);
		
		ArrayList<Double> huSet = new ArrayList<Double>();
		huSet.add(new Double(0f)); //sentinela, para possibilitar a busca a partir do indice 1
		huSet.add(i1);
		huSet.add(i2);
		huSet.add(i3);
		huSet.add(i4);
		huSet.add(i5);
		huSet.add(i6);
		huSet.add(i7);
		
		return huSet;
	}
	
	public static void main(String args[])
	{
		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
//		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat.jpg");
		FileOutputStream fos = null;

		GrayPixelMap pixMap = null;
		
		try 
		{
			pixMap = new GrayPixelMap(in);	
			double m00 = extractRawMoment(pixMap, 0, 0);
			System.out.println("m00: " + m00);
			
			double centX = extractCentroid(pixMap, Centroid.X);
			System.out.println("centX: " + centX);
			
			double centY = extractCentroid(pixMap, Centroid.Y);
			System.out.println("centY: " + centY);
			
			double u00 = extractCentralMoment(pixMap, centX, centY, 0, 0);
			System.out.println("u00: " + u00);
			
			double u01 = extractCentralMoment(pixMap, centX, centY, 0, 1);
			System.out.println("u01: " + u01);
			
			double u10 = extractCentralMoment(pixMap, centX, centY, 1, 0);
			System.out.println("u10: " + u10);
			
			double n01 = Moments.scaleInvariantMoment(pixMap, centX, centY, u00, 0, 1);
			System.out.println("n01: " + n01);
			
			ArrayList<ArrayList<Double>> scaleInvariantSet = Moments.thirdOrderScaleInvariantSet(pixMap);
			double n21 = scaleInvariantSet.get(2).get(1);
			System.out.println("n21: " + n21);
			double xn01 = scaleInvariantSet.get(0).get(1);
			System.out.println("n01: " + xn01);
			
			ArrayList<Double> huSet = extractHuSet(pixMap);
			System.out.println("---HU SET---");
			for( int i = 1; i <= 7; i++)
			{
				System.out.print("i" + i + ": ");
				System.out.println(huSet.get(i));
			}
		} 	
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				in.close();
				if(fos != null)
					fos.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
