package extraction;

import image.GrayPixelMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import processing.PixelMapProcessor;

public class CooccurrenceMatrix 
{
	private int matrix[][];
	private int resizeFactor;
	
	public static final int RESIZE_2 = 1;
	public static final int RESIZE_4 = 2;
	public static final int RESIZE_8 = 3;
	public static final int RESIZE_16 = 4;
	public static final int RESIZE_32 = 5;
	public static final int RESIZE_64 = 6;
	
	/**
	 * 
	 * @param resizeFactor number between 1 and 5
	 */
	public CooccurrenceMatrix( int resizeFactor )
	{
		this.resizeFactor = 1 << resizeFactor;
		int matrixSize = 256 / this.resizeFactor;
		this.matrix = new int[matrixSize][matrixSize];
	}
	
	public int getValue(int col, int row)
	{
		return this.matrix[col][row];
	}
	
	public void setValue(int col, int row, int value)
	{
		this.matrix[col][row] = value;
	}
	
	public void buildMatrix (GrayPixelMap pixMap)
	{
		for( int i = 0; i < pixMap.getWidth() - 1; i++ )
		{
			for(int j = 0; j < pixMap.getHeight(); j++)
			{
//				if( i < (pixMap.getWidth() - 1))
//				{
					int actualFactor = pixMap.getPixel(i, j)[0] / this.resizeFactor;
					int nextFactor = pixMap.getPixel(i + 1, j)[0] / this.resizeFactor;
					this.matrix[actualFactor][nextFactor] += 1;
//				}
			}
		}
	}
	
	public void printMatrix()
	{
		System.out.println("Resize Factor: " + this.resizeFactor);
		for(int i = 0; i < this.matrix.length; i++)
		{
			for(int j = 0; j < this.matrix[0].length; j++)
			{
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	public static void main( String args[] )
	{
		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");
		FileOutputStream fos = null;
//		InputStream in = null;
//		try {
//			in = new FileInputStream("D:/imagem.jpg");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		GrayPixelMap pixMap = null;
		
		try 
		{
			fos = new FileOutputStream("imagemteste.png");
			pixMap = new GrayPixelMap(in);
//			pixMap.printPixMap(System.out);
//			pixMap.pixMapToImage(fos);
			
			GrayPixelMap newPixMap =  PixelMapProcessor.binarizePixMap(pixMap);
			newPixMap.pixelMapToImage(fos);
//			newPixMap.printPixMap(System.out);
			
			CooccurrenceMatrix coMatrix = new CooccurrenceMatrix(CooccurrenceMatrix.RESIZE_32);
			coMatrix.buildMatrix(pixMap);
			
			coMatrix.printMatrix();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
