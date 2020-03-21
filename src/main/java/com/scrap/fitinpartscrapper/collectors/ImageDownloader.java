package com.scrap.fitinpartscrapper.collectors;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Path;

public class ImageDownloader {

	private ImageDownloader() {
		// utility class
	}

	/**
	 * Downloads the image
	 * 
	 * @param imageURL  The image URL
	 * @param imagePath The image path
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void downloadImage(String imageURL, Path imagePath) throws FileNotFoundException, IOException {

		try (FileOutputStream outputStream = new FileOutputStream(imagePath.toFile())) {
			outputStream.getChannel().transferFrom(
					Channels.newChannel(new URL(imageURL).openConnection().getInputStream()), 0, Long.MAX_VALUE);
		}
	}
}
