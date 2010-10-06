package processing.kmeans;

public class Location implements Comparable<Location> {
	private int x;
	private int y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	@Override
	public int compareTo(Location o) {
		if(this.x < o.x)
			return -1;
		else if(this.x > o.x)
			return 1;
		else if(this.x == o.x)
		{
			if(this.y < o.y)
				return -1;
			else if(this.y > o.y)
				return 1;
		}
		
		return 0;
	}
	
	
	
}
