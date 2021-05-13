package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.fe.FeConstants;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class ImageUtilFrontendTest extends JUnitTestBase {
	private static final String VALID_IMAGE = FeConstants.APP_ICON_PATH;
	private static final String INVALID_IMAGE = "/logback-test.xml";
	
	@Test
	void testReadFromFile$_Error() throws IOException {
		byte[] image = IOUtils.resourceToByteArray(INVALID_IMAGE);
		File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString() + ".png");
		FileUtils.writeByteArrayToFile(tempFile, image);
		
		try {
			Assertions.assertThrows(RuntimeException.class, () -> ImageUtilFrontend.readFromFile$(tempFile));
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
			Image result = ImageUtilFrontend.readFromFile(tempFile);
			
			Assertions.assertNotNull(result);
			Assertions.assertFalse(result.isError());
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
			Assertions.assertThrows(IOException.class, () -> ImageUtilFrontend.readFromFile(tempFile));
		} finally {
			tempFile.delete();
		}
	}
	
	@Test
	void testReadFromByteArray$_Error() {
		Assertions.assertThrows(RuntimeException.class, () -> ImageUtilFrontend.readFromByteArray$(IOUtils.resourceToByteArray(INVALID_IMAGE)));
	}
	
	@Test
	void testReadFromByteArray() throws IOException {
		Image result = ImageUtilFrontend.readFromByteArray(IOUtils.resourceToByteArray(VALID_IMAGE));
		
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isError());
	}
	
	@Test()
	void testReadFromByteArray_Error() {
		Assertions.assertThrows(IOException.class, () -> ImageUtilFrontend.readFromByteArray(IOUtils.resourceToByteArray(INVALID_IMAGE)));
	}
	
	@Test
	void testReadFromClasspath() throws IOException {
		Image result = ImageUtilFrontend.readFromClasspath("/images/feather/delete.png");
		
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isError());
	}
	
	@Test
	void testReadFromClasspath_NotFound() {
		Assertions.assertThrows(IOException.class, () -> ImageUtilFrontend.readFromClasspath("/images/feather/delete2.png"));
	}
	
	@Test
	void testReadFromClasspath$() {
		Assertions.assertThrows(RuntimeException.class, () -> ImageUtilFrontend.readFromClasspath$("/images/feather/delete2.png"));
	}
	
	@Test
	void testImageToByteArray() throws IOException {
		Image image = ImageUtilFrontend.readFromClasspath("/images/feather/delete.png");
		
		byte[] result = ImageUtilFrontend.imageToByteArray$(image);
		Assertions.assertNotNull(result);
	}
}