package io.github.matwein.xmc.fe.stages.login.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CrypterTest {
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
		
		Assertions.assertEquals(source, decryptedString);
	}
	
	@Test
	void testDecrypt() {
		Assertions.assertEquals("3", crypter.decrypt("on7Etc4Bf5SiRrfNrCu8fg"));
		Assertions.assertEquals("99", crypter.decrypt("jF5AVcmXtBQS9ofkR1SHJQ"));
	}
}
