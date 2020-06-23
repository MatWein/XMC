package org.xmc.common.utils;

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public static Image readFromFile$(File file) {
        try {
            return readFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error on reading image from file '%s'.", file), e);
        }
    }

    public static Image readFromFile(File file) throws IOException {
        try (var inputStream = FileUtils.openInputStream(file)) {
            return new Image(inputStream);
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
            return new Image(inputStream);
        }
    }
}
