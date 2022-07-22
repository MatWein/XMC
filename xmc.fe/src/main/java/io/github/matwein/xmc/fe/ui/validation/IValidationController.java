package io.github.matwein.xmc.fe.ui.validation;

import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IValidationController {
    Map<IValidationComponent, List<String>> validate();
    
    default Map<IValidationComponent, List<String>> defaultValidate(SimpleBooleanProperty loading, SimpleBooleanProperty fileErrors) {
	    Map<IValidationComponent, List<String>> errors = new HashMap<>();
	
	    if (loading.get() || fileErrors.get()) {
		    List<String> messages = errors.getOrDefault(null, new ArrayList<>());
		    messages.add("Input file not valid or already loading.");
		    errors.put(null, messages);
	    }
	
	    return errors;
    }
}
