package org.xmc.common.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.xmc.common.FileMimeType;

import javax.imageio.ImageIO;
import java.io.*;

public class ImageUtil {
    public static Image readFromClasspath$(String path) {
        try {
            return readFromClasspath(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error on reading image from classpath '%s'.", path), e);
        }
    }

    public static Image readFromClasspath(String path) throws IOException {
        try (InputStream inputStream = ImageUtil.class.getResourceAsStream(path)) {
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

    public static byte[] imageToByteArray(Image image) {
        var bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try (var outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error on creating byte array from image.", e);
        }
    }

    public static byte[] resize$(byte[] image, int width, int height) {
        try {
            return resize(image, width, height);
        } catch (IOException e) {
            throw new RuntimeException("Error on resizing byte array image.", e);
        }
    }

    public static byte[] resize(byte[] image, int width, int height) throws IOException {
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
