package org.xmc.fe.ui.wizard;

import javafx.scene.Parent;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class WizardStep<INPUT_TYPE> {
    private MessageKey textKey;
    private Parent content;
    private Object controller;
    private IWizardStepPopulator<INPUT_TYPE, Object> populator;
    private boolean finished;

    public WizardStep() {
    }

    public WizardStep(
            MessageKey textKey,
            Parent content,
            Object controller,
            IWizardStepPopulator<INPUT_TYPE, Object> populator) {

        this.textKey = textKey;
        this.content = content;
        this.controller = controller;
        this.populator = populator;
    }

    public MessageKey getTextKey() {
        return textKey;
    }

    public void setTextKey(MessageKey textKey) {
        this.textKey = textKey;
    }

    public Parent getContent() {
        return content;
    }

    public void setContent(Parent content) {
        this.content = content;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public IWizardStepPopulator<INPUT_TYPE, Object> getPopulator() {
        return populator;
    }

    public void setPopulator(IWizardStepPopulator<INPUT_TYPE, Object> populator) {
        this.populator = populator;
    }
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
