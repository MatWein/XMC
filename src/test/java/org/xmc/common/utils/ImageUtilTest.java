package org.xmc.common.utils;

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.fe.FeConstants;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class ImageUtilTest extends JUnitTestBase {
    private static final String VALID_IMAGE = FeConstants.APP_ICON_PATH;
    private static final String INVALID_IMAGE = "/application-test.yaml";

    @Test
    void testReadFromFile$_Error() throws IOException {
        byte[] image = IOUtils.resourceToByteArray(INVALID_IMAGE);
        File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString() + ".png");
        FileUtils.writeByteArrayToFile(tempFile, image);

        try {
            Assert.assertThrows(RuntimeException.class, () -> ImageUtil.readFromFile$(tempFile));
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testReadFromFile() throws IOException {
        byte[] image = IOUtils.resourceToByteArray(VALID_IMAGE);
        File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString() + ".png");
        FileUtils.writeByteArrayToFile(tempFile, image);

        try {
            Image result = ImageUtil.readFromFile(tempFile);

            Assert.assertNotNull(result);
            Assert.assertFalse(result.isError());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testReadFromFile_Error() throws IOException {
        byte[] image = IOUtils.resourceToByteArray(INVALID_IMAGE);
        File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString() + ".png");
        FileUtils.writeByteArrayToFile(tempFile, image);

        try {
            Assert.assertThrows(IOException.class, () -> ImageUtil.readFromFile(tempFile));
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testReadFromByteArray$_Error() {
        Assert.assertThrows(RuntimeException.class, () -> ImageUtil.readFromByteArray$(IOUtils.resourceToByteArray(INVALID_IMAGE)));
    }

    @Test
    void testReadFromByteArray() throws IOException {
        Image result = ImageUtil.readFromByteArray(IOUtils.resourceToByteArray(VALID_IMAGE));

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isError());
    }

    @Test()
    void testReadFromByteArray_Error() {
        Assert.assertThrows(IOException.class, () -> ImageUtil.readFromByteArray(IOUtils.resourceToByteArray(INVALID_IMAGE)));
    }

    @Test
    void testReadFromClasspath() throws IOException {
        Image result = ImageUtil.readFromClasspath("/images/feather/delete.png");

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isError());
    }

    @Test
    void testReadFromClasspath_NotFound() {
        Assert.assertThrows(IOException.class, () -> ImageUtil.readFromClasspath("/images/feather/delete2.png"));
    }

    @Test
    void testReadFromClasspath$() {
        Assert.assertThrows(RuntimeException.class, () -> ImageUtil.readFromClasspath$("/images/feather/delete2.png"));
    }

    @Test
    void testImageToByteArray() {
        throw new RuntimeException("not yet implemented");
    }
}