package org.xmc.fe;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class MessageKeyConsistencyTest extends JUnitTestBase {
	private static final String MESSAGE_FILE_EXTENSION = ".properties";
	private static final String COMMON_MESSAGES_FILENAME = "messages.properties";
	private static final String MESSAGES_FOLDER = "src/main/resources/messages/";
	
	@Test
	public void testMessageKeyConsistency() throws Exception {
		File messagesFolder = new File(MESSAGES_FOLDER);
		File[] messageFiles = messagesFolder.listFiles((file) -> file.getName().endsWith(MESSAGE_FILE_EXTENSION));
		
		Assert.assertTrue(messageFiles != null && messageFiles.length > 0);
		
		Map<File, Set<String>> messageFileKeys = new HashMap<>();
		Set<String> allUniqueKeys = new HashSet<>();
		
		for (File messageFile : messageFiles) {
			if (COMMON_MESSAGES_FILENAME.equals(messageFile.getName())) {
				continue;
			}
			
			Properties messages = readPropertiesFromFile(messageFile);
			Set<String> keySet = (Set)messages.keySet();
			
			messageFileKeys.put(messageFile, keySet);
			allUniqueKeys.addAll(keySet);
		}
		
		for (File messageFile : messageFiles) {
			if (COMMON_MESSAGES_FILENAME.equals(messageFile.getName())) {
				continue;
			}
			
			Set<String> availableKeys = messageFileKeys.get(messageFile);
			Set<String> disjunctionKeys = new HashSet<>(allUniqueKeys); disjunctionKeys.removeAll(availableKeys);
			
			Assert.assertEquals(new HashSet<>(), disjunctionKeys);
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
