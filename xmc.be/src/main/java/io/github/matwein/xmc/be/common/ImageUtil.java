package io.github.matwein.xmc.be.common;

import io.github.matwein.xmc.common.FileMimeType;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageUtil {
	public byte[] resize$(byte[] image, int width, int height) {
		try {
			return resize(image, width, height);
		} catch (IOException e) {
			throw new RuntimeException("Error on resizing byte array image.", e);
		}
	}
	
	private byte[] resize(byte[] image, int width, int height) throws IOException {
		if (image == null) {
			return null;
		}
		
		try (var inputStream = new ByteArrayInputStream(image); var outputStream = new ByteArrayOutputStream()) {
			Thumbnails
					.of(inputStream)
					.size(width, height)
					.outputFormat(FileMimeType.PNG.getFileExtension())
					.toOutputStream(outputStream);
			
			return outputStream.toByteArray();
		}
	}
}
