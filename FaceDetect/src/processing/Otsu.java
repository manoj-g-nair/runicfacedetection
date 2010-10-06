package processing;

import extraction.Histogram;

public class Otsu 
{	
	public static int findThreshold( Histogram hist )
	{
		double minVariance = Double.MAX_VALUE;
		int threshold = 1;
		for(int i = threshold; i < hist.getHistogramSize(); i++)
		{
			double[] leftFreqs = findFrequencies(hist, i, true);
			double[] rightFreqs = findFrequencies(hist,i, false);
			double leftMean = leftFreqs[1] / leftFreqs[0];
			double rightMean = rightFreqs[1] / rightFreqs[0];
			double leftVariance = findClassVariance(hist, i, leftMean, true);
			double rightVariance = findClassVariance(hist, i, rightMean, false);
			double currentVariance = findInterclassVariance(leftFreqs[0], rightFreqs[0], leftVariance, rightVariance);
			if(currentVariance < minVariance)
			{
				minVariance = currentVariance;
				threshold = i;
			}
		}
		return threshold;
	}	
	
	public static double findMaxValue(Histogram hist, int threshold)
	{
		return findFrequencies(hist, hist.getHistogramSize() - 1, true)[0];
	}
	
	public static double[] findFrequencies(Histogram hist, int curThreshold, boolean left)
	{
		int start = 0;
		int end = 0;
		double weightFreq = 0f;
		double grayFreq = 0f;
		if(left)
		{
			start = 0;
			end = curThreshold;
		}
		else
		{
			start = curThreshold;
			end = hist.getHistogramSize();
		}
		for(int i = start; i < end; i++)
		{
			weightFreq += hist.getColorFreq(i);
			grayFreq += i * hist.getColorFreq(i);
		}
		
		double result[] = new double[2];
		result[0] = weightFreq;
		result[1] = grayFreq;
		return result;
	}
	
	public static double findClassVariance(Histogram hist, int curThreshold, double mean, boolean left)
	{
		int start = 0;
		int end = 0;
		if(left)
		{
			start = 0;
			end = curThreshold;
		}
		else
		{
			start = curThreshold;
			end = hist.getHistogramSize();
		}
		
		double result = 0f;
		double weightFreq = 0f;
		for(int i = start; i < end; i++)
		{
			double localVariance = Math.pow( ((double)i) - mean, 2);
			result += localVariance * (double)hist.getColorFreq(i);
			weightFreq += (double) hist.getColorFreq(i);
		}
		result /= weightFreq;
		return result;
	}
	
	public static double findInterclassVariance( double wf1, double wf2, double var1, double var2)
	{
		double w1 = wf1 / (wf1 + wf2), 
			w2 = wf2 / (wf1 + wf2);
		
		double icVariance = w1 * var1 + w2 * var2;
		return icVariance;
	}

}
