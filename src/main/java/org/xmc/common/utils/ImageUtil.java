package org.xmc.common.utils;

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
}
