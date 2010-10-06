package processing.kmeans;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Centroid 
{
	private int x;
	private int y;
	private int luminance;
	
	private int imageWidth;
	private int imageHeight;
	
	private Collection<Location> neighbors;
	
	public Centroid(int x, int y, int luminance, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.luminance = luminance;
		this.imageWidth = width;
		this.imageHeight = height;
		
		neighbors = new TreeSet<Location>();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLuminance() {
		return luminance;
	}

	public void setLuminance(int luminance) {
		this.luminance = luminance;
	}
	
	public Collection<Location> getNeighbors()
	{
		return neighbors;
	}
	
	public double evaluateDistance(int i, int j, int luminance)
	{
		double distX = Math.pow((double)(x - i)/(double)this.imageWidth, 2);
		double distY = Math.pow((double)(y - j)/(double)this.imageHeight, 2);
		double distL = Math.pow((double)(this.luminance - luminance)/256f, 2);
		return distX + distY + distL;
	}
	
	public boolean adjustCenter()
	{
		int xSum = 0;
		int ySum = 0;
		for(Iterator<Location> iterator = neighbors.iterator(); iterator.hasNext();)
		{
			Location loc = iterator.next();
			xSum += loc.getX();
			ySum += loc.getY();
		}
		int oldX = this.x;
		int oldY = this.y;
		
		if(neighbors.size() > 0)
		{
			int totalNeighbors = neighbors.size(); 
			this.x = xSum /  totalNeighbors ;
			this.y = ySum / totalNeighbors ;
		}
		
		return oldX != this.x || oldY != this.y;
	}
	
	public void printPixels(OutputStream out)
	{
		PrintWriter printer = new PrintWriter(out);
		for(Iterator<Location> iter = this.neighbors.iterator(); iter.hasNext();)
		{
			Location location = iter.next();
			printer.printf("(%d, %d) , ", location.getX(), location.getY());
			printer.flush();
		}
		printer.println();
		printer.flush();
	}
	
}
