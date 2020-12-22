package org.xmc.fe.ui.wizard;

import com.google.common.collect.Iterables;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.components.LineBreak;

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

        for (WizardStep step : steps) {
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

        WizardStep previousStep = steps.get(steps.indexOf(currentStep) - 1);
        switchStep(previousStep);
    }

    @FXML
    public void switchToNextStep() {
        populateState();

        WizardStep lastStep = Iterables.getLast(steps, null);
        if (currentStep == lastStep) {
            ((Stage)buttonNext.getScene().getWindow()).close();
        } else {
            WizardStep nextStep = steps.get(steps.indexOf(currentStep) + 1);
            switchStep(nextStep);
        }
    }

    private void populateState() {
	    currentStep.setFinished(true);
	    
        IWizardStepPopulator<INPUT_TYPE, Object> populator = currentStep.getPopulator();

        if (populator != null) {
            populator.populateState(input, currentStep.getController());
        }
    }

    private void switchStep(WizardStep wizardStep) {
        currentStep = wizardStep;

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
            ((IAfterInit) currentStep.getController()).afterInitialize(input);
        }
    }

    private void updateButtons() {
        WizardStep firstStep = Iterables.getFirst(steps, null);
        if (currentStep == firstStep) {
            buttonNext.setDisable(false);
            buttonPrevious.setDisable(true);
        } else {
            buttonNext.setDisable(false);
            buttonPrevious.setDisable(false);
        }

        WizardStep lastStep = Iterables.getLast(steps, null);
        if (currentStep == lastStep) {
            buttonNext.setText(MessageAdapter.getByKey(MessageAdapter.MessageKey.WIZARD_FINISH));
        } else {
            buttonNext.setText(MessageAdapter.getByKey(MessageAdapter.MessageKey.WIZARD_NEXT));
        }

        SceneUtil.createValidationSceneState(buttonNext.getScene()).validate();
    }
}
