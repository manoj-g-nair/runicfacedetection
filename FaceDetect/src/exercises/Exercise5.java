package exercises;

import image.GrayPixelMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import extraction.Boundary;

import processing.PixelMapProcessor;

public class Exercise5 {

	public static void main(String[] args) {
		InputStream in = GrayPixelMap.class.getResourceAsStream("/resources/cat_grande.jpg");

		FileOutputStream fos = null;
		GrayPixelMap pixMap = null;

		try {
			fos = new FileOutputStream("imagemteste3.png");
			pixMap = new GrayPixelMap(in);

			//Binariza a imagem utilizando Otsu
			GrayPixelMap newPixMap = PixelMapProcessor.binarizePixMap(pixMap);
			
			//Extraí as bordas contidas na imagem
			newPixMap = Boundary.extract(newPixMap);
			
			newPixMap.pixelMapToImage(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
