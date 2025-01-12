package io.github.matwein.xmc.fe.stages.login.logic;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class Crypter {
	private static final Logger LOGGER = LoggerFactory.getLogger(Crypter.class);

	private static final String PASSWORD = "feup3Gy1sAW8A31QBjOtIhllqgQed5ww";
	private static final String ALGORITHM = "AES";
	private static final String ENCODING = "UTF-8";
	private static final int AES_PASSWORD_LENGTH = 16;
	
	private final Cipher encryptionCipher;
	private final Cipher decryptionCipher;

	public Crypter() {
		try {
			byte[] password = generateSmallBytesFromPassword(PASSWORD);
			SecretKeySpec key = new SecretKeySpec(password, ALGORITHM);
			
			encryptionCipher = Cipher.getInstance(ALGORITHM);
			decryptionCipher = Cipher.getInstance(ALGORITHM);
			
			encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
			decryptionCipher.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			String message = String.format("Error on initializing '%s'.", Crypter.class.getName());
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public byte[] encrypt(byte[] source) {
		if (source == null) {
			return null;
		}

		try {
			return encryptionCipher.doFinal(source);
		} catch (Throwable e) {
			String message = "Error on encryption of bytes.";
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public String encrypt(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		try {
			byte[] utf8Content = source.getBytes(ENCODING);  
            byte[] encryptedSource = encryptionCipher.doFinal(utf8Content);  
              
            return Base64.getEncoder().encodeToString(encryptedSource);
		} catch (Throwable e) {
			String message = "Error on encryption of string.";
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public byte[] decrypt(byte[] source) {
		if (source == null) {
			return null;
		}

		try {
			return decryptionCipher.doFinal(source);
		} catch (Throwable e) {
			String message = "Error on decryption of string.";
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public String decrypt(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		try {
			byte[] decryptedSource = Base64.getDecoder().decode(source);
            byte[] utf8Content = decryptionCipher.doFinal(decryptedSource);  
  
            return new String(utf8Content, ENCODING);  
		} catch (Throwable e) {
			String message = "Error on decryption of string.";
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}
	
	private static byte[] generateSmallBytesFromPassword(String password) {
		byte[] passwordAsBytes = getBytesFromString(password);
		return Arrays.copyOf(passwordAsBytes, AES_PASSWORD_LENGTH);
	}
	
	private static byte[] getBytesFromString(String value) {
		try {
			return value.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(String.format("Error on generating password. Encoding '%s' unknown.", ENCODING), e);
		}
	}
}
