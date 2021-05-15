package io.github.matwein.xmc.be.common;

import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MessageAdapterTest {
    @Test
    void testGetByKey() {
	    Locale.setDefault(Locale.GERMANY);
	    MessageAdapter.initBundle();
	    
        String result = MessageAdapter.getByKey(MessageKey.APP_DATE_FORMAT);
        Assertions.assertEquals("dd.MM.yyyy", result);
    }

    @Test
    void testGetByKey_Language() {
        Locale.setDefault(Locale.GERMANY);
        MessageAdapter.initBundle();

        String result = MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_STOCK);
        Assertions.assertEquals("Speichere Wertpapier...", result);

        Locale.setDefault(Locale.ENGLISH);
        MessageAdapter.initBundle();

        result = MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_STOCK);
        Assertions.assertEquals("Saving stock...", result);
    }
	
	@Test
	void testGetByKey_Enums_German() {
		Locale.setDefault(Locale.GERMANY);
		MessageAdapter.initBundle();
		
		testGetByKey_Enums();
	}
	
	@Test
	void testGetByKey_Enums_English() {
		Locale.setDefault(Locale.ENGLISH);
		MessageAdapter.initBundle();
		
		testGetByKey_Enums();
	}
	
	private void testGetByKey_Enums() {
		List<MessageKey> messageKeysWithEnumTypes = Stream.of(MessageKey.values())
				.filter(messageKey -> messageKey.getEnumType() != null)
				.collect(Collectors.toList());
		
		for (MessageKey messageKey : messageKeysWithEnumTypes) {
			Enum<?>[] enumConstants = messageKey.getEnumType().getEnumConstants();
			for (Enum<?> enumConstant : enumConstants) {
				String key = messageKey.getKey() + "." + enumConstant.name();
				
				String translation = MessageAdapter.getByKey(messageKey, enumConstant);
				
				Assertions.assertNotNull(translation);
				Assertions.assertNotEquals(key, translation);
			}
		}
	}
	
	@Test
    void testGetByKey_Params() {
	    Locale.setDefault(Locale.GERMANY);
	    MessageAdapter.initBundle();
	    
        String result = MessageAdapter.getByKey(MessageKey.ANALYSIS_TRANSACTIONS_AGGREGATE_DESCRIPTION, 10);
        Assertions.assertEquals("Transaktionen mit einem Anteil kleiner als 10 %.", result);
    }
	
	@Test
	void testGetByKey_AllValues_German() {
		Locale.setDefault(Locale.GERMANY);
		MessageAdapter.initBundle();
		
		testGetByKey_AllValues();
	}
	
	@Test
	void testGetByKey_AllValues_English() {
		Locale.setDefault(Locale.ENGLISH);
		MessageAdapter.initBundle();
		
		testGetByKey_AllValues();
	}
	
	private void testGetByKey_AllValues() {
		List<MessageKey> messageKeysWithoutEnumTypes = Stream.of(MessageKey.values())
				.filter(messageKey -> messageKey.getEnumType() == null)
				.collect(Collectors.toList());
		
		for (MessageKey messageKey : messageKeysWithoutEnumTypes) {
			String translation = MessageAdapter.getByKey(messageKey);
			
			Assertions.assertNotNull(translation);
			Assertions.assertNotEquals(messageKey.getKey(), translation);
		}
	}
}