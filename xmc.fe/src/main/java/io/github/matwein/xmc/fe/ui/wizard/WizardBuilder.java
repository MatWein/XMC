package io.github.matwein.xmc.fe.ui.wizard;

import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.StageBuilder;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class WizardBuilder<INPUT_TYPE> {
    public static WizardBuilder getInstance() { return new WizardBuilder(); }

    private final List<WizardStep> steps = new ArrayList<>();
    private MessageKey titleKey;
    private INPUT_TYPE input;

    private WizardBuilder() {
    }

    public WizardBuilder titleKey(MessageKey titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public WizardBuilder addStep(MessageKey textKey, FxmlKey fxmlKey) {
        return addStep(textKey, fxmlKey, null);
    }

    public WizardBuilder addStep(MessageKey textKey, FxmlKey fxmlKey, IWizardStepPopulator<INPUT_TYPE, ?> populator) {
        Pair<Parent, ?> component = FxmlComponentFactory.load(fxmlKey);
        steps.add(new WizardStep(textKey, component.getLeft(), component.getRight(), populator));
        return this;
    }

    public WizardBuilder withInput(INPUT_TYPE input) {
        this.input = input;
        return this;
    }

    public Stage build() {
        Stage stage = StageBuilder.getInstance()
                .withDefaultIcon()
                .withTitleKey(titleKey)
                .withInput(ImmutablePair.of(steps, input))
                .resizable(false)
                .withFxmlSceneComponent(FxmlKey.WIZARD)
                .build();

        stage.initOwner(MainController.mainWindow);
        stage.initModality(Modality.WINDOW_MODAL);

        showBackdrop(stage);

        return stage;
    }
    
    public boolean buildAndShow() {
    	build().showAndWait();
    	
    	boolean allStepsFinished = true;
	    for (WizardStep step : steps) {
		    allStepsFinished &= step.isFinished();
	    }
	    return allStepsFinished;
    }

    private static void showBackdrop(Stage stage) {
        if (MainController.backdropRef != null && !MainController.backdropRef.isVisible()) {
            stage.setOnShown(event -> MainController.backdropRef.setVisible(true));
            stage.setOnHidden(event -> MainController.backdropRef.setVisible(false));
        }
    }
}
