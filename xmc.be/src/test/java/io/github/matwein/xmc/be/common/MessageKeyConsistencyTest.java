package io.github.matwein.xmc.be.common;

import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class MessageKeyConsistencyTest {
	private static final String MESSAGE_FILE_EXTENSION = ".properties";
	private static final String COMMON_MESSAGES_FILENAME = "messages_backend" + MESSAGE_FILE_EXTENSION;
	private static final String MESSAGES_FOLDER = "src/main/resources/messages/";

	private Map<File, Set<String>> messageFileKeys;
	private Set<String> allUniqueKeys;
	private Set<String> allUniqueKeysWithoutCommonKeys;
	private File[] messageFiles;

	@BeforeEach
	void setUp() throws IOException {
		File messagesFolder = new File(MESSAGES_FOLDER);
		messageFiles = messagesFolder.listFiles((file) -> file.getName().endsWith(MESSAGE_FILE_EXTENSION));

		Assertions.assertTrue(messageFiles != null && messageFiles.length > 0);

		messageFileKeys = new HashMap<>();
		allUniqueKeys = new HashSet<>();
		allUniqueKeysWithoutCommonKeys = new HashSet<>();

		for (File messageFile : messageFiles) {
			Properties messages = readPropertiesFromFile(messageFile);
			Set<String> keySet = (Set)messages.keySet();

			messageFileKeys.put(messageFile, keySet);
			allUniqueKeys.addAll(keySet);

			if (!COMMON_MESSAGES_FILENAME.equals(messageFile.getName())) {
				allUniqueKeysWithoutCommonKeys.addAll(keySet);
			}
		}
	}

	@Test
	void testMessageKeyConsistency() {
		for (File messageFile : messageFiles) {
			if (COMMON_MESSAGES_FILENAME.equals(messageFile.getName())) {
				continue;
			}
			
			Set<String> availableKeys = messageFileKeys.get(messageFile);
			Set<String> disjunctionKeys = new HashSet<>(allUniqueKeysWithoutCommonKeys); disjunctionKeys.removeAll(availableKeys);
			
			Assertions.assertEquals(new HashSet<>(), disjunctionKeys, String.format("Message keys '%s' are not contained in every message file.", disjunctionKeys));
		}
	}

	@Test
	void testMessageKeyConsistency_EachFileContainsEachEnumValue() {
		for (MessageKey messageKey : MessageKey.values()) {
			if (messageKey.getEnumType() != null) {
				for (Enum<?> enumConstant : messageKey.getEnumType().getEnumConstants()) {
					String key = messageKey.getKey() + "." + enumConstant.name();
					Assertions.assertTrue(allUniqueKeys.contains(key), String.format("Message key '%s' is contained in enum but not in any message file.", key));
				}
			} else {
				String key = messageKey.getKey();
				Assertions.assertTrue(allUniqueKeys.contains(key), String.format("Message key '%s' is contained in enum but not in any message file.", key));
			}
		}
	}
	
	private Properties readPropertiesFromFile(File file) throws IOException {
		Properties properties = new Properties();

		try (FileReader fileReader = new FileReader(file)) {
			properties.load(fileReader);
		}
		
		return properties;
	}
}
