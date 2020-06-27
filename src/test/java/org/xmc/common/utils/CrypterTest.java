package org.xmc.common.utils;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

class CrypterTest extends JUnitTestBase {
	private Crypter crypter;

	@BeforeEach
	public void setUp() {
		crypter = new Crypter();
	}
	
	@Test
	void testEncryptAndDecrypt() {
		String source = "This is a test string. With linebreak\r\n and numbers 1234567890.";
		
		String encryptedString = crypter.encrypt(source);
		String decryptedString = crypter.decrypt(encryptedString);
		
		Assert.assertEquals(source, decryptedString);
	}
	
	@Test
	void testDecrypt() {
		Assert.assertEquals("3", crypter.decrypt("on7Etc4Bf5SiRrfNrCu8fg"));
		Assert.assertEquals("99", crypter.decrypt("jF5AVcmXtBQS9ofkR1SHJQ"));
	}
}
