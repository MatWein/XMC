package io.github.matwein.xmc.be.config;

import com.google.gson.*;
import io.github.matwein.xmc.common.annotations.JsonIgnore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
public class JsonConfig {
    private static final ExclusionStrategy EXCLUSION_STRATEGY = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(JsonIgnore.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    };

    @Bean
    public Gson gson(ApplicationContext applicationContext) {
        JsonSerializer<LocalDate> localDateAdapter = (date, type, context) -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        JsonSerializer<Class> classAdapter = (clazz, type, context) -> new JsonPrimitive(clazz.getName());
        JsonSerializer<LocalDateTime> localDateTimeAdapter = (date, type, context) -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        JsonSerializer<byte[]> byteArrayAdapter = (bytes, type, context) -> new JsonPrimitive(Base64Utils.encodeToString(bytes));
	    JsonSerializer<Optional> optionalAdapter = (optional, type, context) -> new JsonPrimitive(applicationContext.getBean(Gson.class).toJson(optional.orElse(null)));

        return new GsonBuilder()
                .setPrettyPrinting()
                .addSerializationExclusionStrategy(EXCLUSION_STRATEGY)
                .addDeserializationExclusionStrategy(EXCLUSION_STRATEGY)
                .registerTypeAdapter(LocalDate.class, localDateAdapter)
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .registerTypeAdapter(byte[].class, byteArrayAdapter)
                .registerTypeAdapter(Class.class, classAdapter)
                .registerTypeAdapter(Optional.class, optionalAdapter)
                .create();
    }
}
