package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.common.FileMimeType;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.*;

public class ImageUtilFrontend {
	public static Image readFromClasspath$(String path) {
		try {
			return readFromClasspath(path);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error on reading image from classpath '%s'.", path), e);
		}
	}
	
	public static Image readFromClasspath(String path) throws IOException {
		try (InputStream inputStream = ImageUtilFrontend.class.getResourceAsStream(path)) {
			if (inputStream == null) {
				throw new IOException(String.format("Could not find class path file '%s'.", path));
			}
			
			return createImageFromInputStream(inputStream, "classpath");
		}
	}
	
	public static Image readFromFile$(File file) {
		try {
			return readFromFile(file);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error on reading image from file '%s'.", file), e);
		}
	}
	
	public static Image readFromFile(File file) throws IOException {
		try (var inputStream = FileUtils.openInputStream(file)) {
			return createImageFromInputStream(inputStream, file.getAbsolutePath());
		}
	}
	
	public static Image readFromByteArray$(byte[] image) {
		try {
			return readFromByteArray(image);
		} catch (IOException e) {
			throw new RuntimeException("Error on reading image from byte array.", e);
		}
	}
	
	public static Image readFromByteArray(byte[] image) throws IOException {
		if (image == null) {
			return null;
		}
		
		try (var inputStream = new ByteArrayInputStream(image)) {
			return createImageFromInputStream(inputStream, "byte array");
		}
	}
	
	private static Image createImageFromInputStream(InputStream inputStream, String errorParam) throws IOException {
		Image image = new Image(inputStream);
		if (image.isError()) {
			throw new IOException(String.format("Error on loading image from %s.", errorParam), image.getException());
		}
		return image;
	}
	
	public static byte[] imageToByteArray$(Image image) {
		try {
			return imageToByteArray(image);
		} catch (Exception e) {
			throw new RuntimeException("Error on creating byte array from image.", e);
		}
	}
	
	public static byte[] imageToByteArray(Image image) throws Exception {
		if (image == null) {
			return null;
		}
		
		try (var outputStream = new ByteArrayOutputStream()) {
			var bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ImageIO.write(bufferedImage, "png", outputStream);
			return outputStream.toByteArray();
		}
	}
	
	public static Image invertColors(Image input) {
		if (input == null) {
			return null;
		}
		
		PixelReader reader = input.getPixelReader();
		int w = (int) input.getWidth();
		int h = (int) input.getHeight();
		WritableImage result = new WritableImage(w, h);
		PixelWriter writer = result.getPixelWriter();
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Color color = reader.getColor(x, y);
				writer.setColor(x, y, color.invert());
			}
		}
		
		return result;
	}
	
	public static byte[] resize$(byte[] image, int width, int height) {
		try {
			return resize(image, width, height);
		} catch (IOException e) {
			throw new RuntimeException("Error on resizing byte array image.", e);
		}
	}
	
	public static byte[] resize(byte[] image, int width, int height) throws IOException {
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
