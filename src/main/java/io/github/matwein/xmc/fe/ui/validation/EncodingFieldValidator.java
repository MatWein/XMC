package io.github.matwein.xmc.fe.ui.validation;

import com.google.common.collect.Lists;
import javafx.scene.control.ComboBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class EncodingFieldValidator implements ICustomFieldValidator<ComboBox<String>> {
	@Override
	public Collection<String> validate(ComboBox<String> textField) {
		String text = textField.getSelectionModel().getSelectedItem();
		if (StringUtils.isBlank(text)) {
			return new ArrayList<>(0);
		}
		
		try {
			Charset.forName(text);
			return new ArrayList<>(0);
		} catch (Throwable e) {
			String errorMessage = MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_ENCODING);
			return Lists.newArrayList(errorMessage);
		}
	}
}
