package extraction;

import image.GrayPixelMap;

public class Boundary {

	private static final int WINDOW_CENTER_X;
	private static final int WINDOW_CENTER_Y;

	private static int window[][];
	private static int windowWidth;
	private static int windowHeight;

	static {
		window = new int[][] { { 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255 } };

		windowWidth = window.length;
		windowHeight = window[0].length;

		WINDOW_CENTER_X = Double.valueOf(Math.floor(windowWidth / 2)).intValue();
		WINDOW_CENTER_Y = Double.valueOf(Math.floor(windowHeight / 2)).intValue();
	}

	public static GrayPixelMap extract(GrayPixelMap originalImage) {

		if (originalImage == null)
			throw new IllegalArgumentException("pixel map cannot be null");

		// Erode a imagem
		GrayPixelMap erodedImage = erodeImage(originalImage);

		// Realiza a diferença entre a imagem erodida e a imagem original
		GrayPixelMap result = diffImage(originalImage, erodedImage);

		return result;
	}

	private static GrayPixelMap diffImage(GrayPixelMap originalImage, GrayPixelMap erodedImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		GrayPixelMap result = new GrayPixelMap(width, height);

		int originalPixel[] = null;
		int erodedPixel[] = null;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				originalPixel = originalImage.getPixel(i, j);
				erodedPixel = erodedImage.getPixel(i, j);

				if (originalPixel[0] != erodedPixel[0]) {
					result.setPixel(i, j, new int[] { 255, 0, 0 });
				} else {
					result.setPixel(i, j, new int[] { 0, 0, 0 });
				}
			}
		}

		return result;
	}

	private static GrayPixelMap erodeImage(GrayPixelMap pixelMap) {
		int width = pixelMap.getWidth();
		int height = pixelMap.getHeight();

		int pixel[] = null;
		int p = -1;
		boolean eroded = false;
		GrayPixelMap erodedImage = new GrayPixelMap(width, height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixel = pixelMap.getPixel(i, j);

				erodedImage.setPixel(i, j, new int[]{pixel[0],pixel[1],pixel[2]});

				if (pixel[0] == window[WINDOW_CENTER_X][WINDOW_CENTER_Y]) {
					eroded = false;

					for (int x = 0; x < windowWidth && !eroded; x++) {
						for (int y = 0; y < windowHeight && !eroded; y++) {
							p = pixelMap.getPixel(i + x - WINDOW_CENTER_X, j + y - WINDOW_CENTER_Y)[0];
							if (p < 0 || p > 255)
								continue;

							if (p - window[x][y] != 0) {
								erodedImage.setPixel(i, j, new int[] { 0, 0, 0 });
								eroded = true;
							}
						}
					}
				}

			}
		}

		return erodedImage;
	}

}
