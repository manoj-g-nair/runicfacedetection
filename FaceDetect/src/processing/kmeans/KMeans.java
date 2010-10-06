package processing.kmeans;

import image.GrayPixelMap;
import image.PixelMap;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class KMeans
{
	private List<Centroid> centroids;
	private PixelMap pixMap;
	
	public KMeans(PixelMap pixMap, int numberOfCentroids)
	{
		centroids = new ArrayList<Centroid>();
		this.pixMap = pixMap;
		
		for(int i = 0; i < numberOfCentroids; i++)
		{			
			Centroid cent = KMeans.createCentroid(pixMap);
			this.centroids.add(cent);
		}
				
	}
	
	public void findClusters()
	{
		int centroidIndex = 0;
		double minDistance = Double.MAX_VALUE;
		int currentIndex = 0;
		boolean running = true;
		for(int counter = 0; running && counter < 10000/*(Integer.MAX_VALUE >> 2)*/ ; counter++ )
		{
			for( Iterator<Centroid> iterator = this.centroids.iterator(); iterator.hasNext(); )
			{
				Centroid centroid = iterator.next();
				centroid.getNeighbors().clear();
			}
			for( int i = 0; i < pixMap.getWidth(); i++)
			{
				for(int j = 0; j < pixMap.getHeight(); j++)
				{
					Location location = new Location(i,j);
					currentIndex = 0;
					centroidIndex = 0;
					minDistance = Double.MAX_VALUE;
					for( Iterator<Centroid> iterator = this.centroids.iterator(); iterator.hasNext(); )
					{
						Centroid centroid = iterator.next();
						double distance = centroid.evaluateDistance(i, j, pixMap.getLuminance(i, j));
						if(distance < minDistance)
						{
							minDistance = distance;
							centroidIndex = currentIndex;
						}
						currentIndex++;
					}
					centroids.get(centroidIndex).getNeighbors().add(location);
				}
			}
			running = false;
			for( Iterator<Centroid> iterator = this.centroids.iterator(); iterator.hasNext(); )
			{
				Centroid centroid = iterator.next();
				running = centroid.adjustCenter() ? true : running;
				int x = centroid.getX();
				int y = centroid.getY();
				centroid.setLuminance(this.pixMap.getLuminance(x, y));
//				centroid.printPixels(System.out);
			}
			if(counter % 1000 == 0)
			System.out.println("--Iteration " + counter + " END---");
			if(counter == 999)
				System.out.println("Chegou aqui!");
		}
	}
	
	public List<Centroid> getCentroids()
	{
		return centroids;
	}
	
	private int findCentroidIndex(int locationX, int locationY)
	{
		int result = 0;
		Centroid temp = null;
		Location location = new Location(locationX, locationY);
		for(Iterator<Centroid> centroidsIter = this.centroids.iterator(); 
			centroidsIter.hasNext(); )
		{
			temp = centroidsIter.next();
			if(temp.getNeighbors().contains(location))
			{
				break;
			}
			result++;
		}
		return result;
	}
	
	public void centroidsToImage(OutputStream os) throws IOException
	{
		int div = 256 / this.centroids.size();
		
		int width = pixMap.getWidth();
		int height = pixMap.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
//		double[] pixel = new double[3];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				int[] pix = new int[3];
				int index = this.findCentroidIndex(i, j);
				pix[0] = index * div;
				raster.setPixel(i, j, pix);
			}
		}
		ImageIO.write(image, "PNG", os);
	}
	
	public static Centroid createCentroid(PixelMap pixMap)
	{
		int width = pixMap.getWidth();
		int height = pixMap.getHeight();
		
		Random rand = new Random ();
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		int luminance = pixMap.getLuminance(x, y);
		
		Centroid centroid = new Centroid(x, y, luminance, width, height);
		return centroid;
	}

	public static void main (String args[])
	{
		InputStream in = KMeans.class.getResourceAsStream("/resources/cat.jpg");
		FileOutputStream fos = null;
		FileOutputStream outImage = null;
		
		try 
		{
			fos = new FileOutputStream("kmeanstest.txt");
			outImage = new FileOutputStream("kmeanstest.png");
			GrayPixelMap pixMap = new GrayPixelMap(in);
			KMeans kmeans = new KMeans(pixMap, 10);
			kmeans.findClusters();
			for(Iterator<Centroid> cIter = kmeans.getCentroids().iterator(); cIter.hasNext();)
			{
				Centroid c = cIter.next();
				c.printPixels(fos);
			}
			kmeans.centroidsToImage(outImage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
