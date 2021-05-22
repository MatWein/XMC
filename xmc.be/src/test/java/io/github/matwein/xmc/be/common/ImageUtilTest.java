package io.github.matwein.xmc.be.common;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

class ImageUtilTest {
	private ImageUtil imageUtil;
	
	@BeforeEach
	void setUp() {
		imageUtil = new ImageUtil();
	}
	
	@Test
	void testResize$() throws IOException {
		byte[] image = IOUtils.toByteArray(getClass().getResourceAsStream("/images/XMC_64.png"));
		
		byte[] result = imageUtil.resize$(image, 128, 128);
		
		try (var inputStream = new ByteArrayInputStream(result)) {
			BufferedImage resultImage = ImageIO.read(inputStream);
			
			Assertions.assertEquals(128, resultImage.getWidth());
			Assertions.assertEquals(128, resultImage.getHeight());
		}
	}
}