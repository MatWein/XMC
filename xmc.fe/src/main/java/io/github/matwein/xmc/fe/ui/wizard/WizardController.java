package io.github.matwein.xmc.fe.ui.wizard;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.components.LineBreak;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@FxmlController
public class WizardController<INPUT_TYPE> implements IAfterInit<Pair<List<WizardStep<INPUT_TYPE>>, INPUT_TYPE>> {
    private List<WizardStep<INPUT_TYPE>> steps;
    private INPUT_TYPE input;
    private WizardStep<INPUT_TYPE> currentStep;

    @FXML private TextFlow stepsTextflow;
    @FXML private VBox contentVBox;
    @FXML private Button buttonNext;
    @FXML private Button buttonPrevious;

    @Override
    public void afterInitialize(Pair<List<WizardStep<INPUT_TYPE>>, INPUT_TYPE> param) {
        this.steps = param.getLeft();
        this.input = param.getRight();

        for (WizardStep<INPUT_TYPE> step : steps) {
            Text stepText = new Text(MessageAdapter.getByKey(step.getTextKey()));
            stepText.setUserData(step);

            stepsTextflow.getChildren().add(stepText);
            stepsTextflow.getChildren().add(new LineBreak());
        }

        switchStep(steps.get(0));
    }

    @FXML
    public void switchToPreviousStep() {
        populateState();

        WizardStep<INPUT_TYPE> previousStep = steps.get(steps.indexOf(currentStep) - 1);
        switchStep(previousStep);
    }

    @FXML
    public void switchToNextStep() {
        populateState();
	
	    currentStep.setFinished(true);
	
	    WizardStep<INPUT_TYPE> lastStep = getLastStep();
	
	    if (currentStep == lastStep) {
            ((Stage)buttonNext.getScene().getWindow()).close();
        } else {
            WizardStep<INPUT_TYPE> nextStep = steps.get(steps.indexOf(currentStep) + 1);
            switchStep(nextStep);
        }
    }
	
	private void populateState() {
        IWizardStepPopulator<INPUT_TYPE, Object> populator = currentStep.getPopulator();

        if (populator != null) {
            populator.populateState(input, currentStep.getController());
        }
    }

    private void switchStep(WizardStep<INPUT_TYPE> wizardStep) {
        currentStep = wizardStep;
        
        if (wizardStep.getPopulator() != null) {
	        wizardStep.getPopulator().populateGui(input, wizardStep.getController());
        }

        contentVBox.getChildren().clear();
        contentVBox.getChildren().add(wizardStep.getContent());

        for (Node child : stepsTextflow.getChildren()) {
            child.getStyleClass().remove("bold");

            if (currentStep == child.getUserData()) {
                child.getStyleClass().add("bold");
            }
        }

        updateButtons();

        if (currentStep.getController() instanceof IAfterInit) {
            ((IAfterInit<INPUT_TYPE>) currentStep.getController()).afterInitialize(input);
        }
    }

    private void updateButtons() {
        WizardStep<INPUT_TYPE> firstStep = getFirstStep();
        if (currentStep == firstStep) {
            buttonNext.setDisable(false);
            buttonPrevious.setDisable(true);
        } else {
            buttonNext.setDisable(false);
            buttonPrevious.setDisable(false);
        }

        WizardStep<INPUT_TYPE> lastStep = getLastStep();
        if (currentStep == lastStep) {
            buttonNext.setText(MessageAdapter.getByKey(MessageAdapter.MessageKey.WIZARD_FINISH));
        } else {
            buttonNext.setText(MessageAdapter.getByKey(MessageAdapter.MessageKey.WIZARD_NEXT));
        }

        SceneUtil.createValidationSceneState(buttonNext.getScene()).validate();
    }
	
	private WizardStep<INPUT_TYPE> getFirstStep() {
		WizardStep<INPUT_TYPE> lastStep = null;
		if (CollectionUtils.isNotEmpty(steps)) {
			lastStep = steps.get(0);
		}
		return lastStep;
	}
	
	private WizardStep<INPUT_TYPE> getLastStep() {
		WizardStep<INPUT_TYPE> lastStep = null;
		if (CollectionUtils.isNotEmpty(steps)) {
			lastStep = steps.get(steps.size() - 1);
		}
		return lastStep;
	}
}
