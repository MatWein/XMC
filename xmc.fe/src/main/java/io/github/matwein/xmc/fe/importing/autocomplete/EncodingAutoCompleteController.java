package io.github.matwein.xmc.fe.importing.autocomplete;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Component
public class EncodingAutoCompleteController extends AutoCompleteByConverterController<String> {
	private static final List<String> ENCODINGS = Lists.newArrayList(
			StandardCharsets.UTF_8.name(),
			StandardCharsets.ISO_8859_1.name()
	);
	
	@Autowired
	public EncodingAutoCompleteController() {
		super(ENCODINGS, Function.identity());
	}
}
