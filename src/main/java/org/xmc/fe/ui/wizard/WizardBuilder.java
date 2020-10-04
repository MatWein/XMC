package org.xmc.fe.ui.wizard;

import com.google.common.collect.Lists;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

import java.util.List;

public class WizardBuilder<INPUT_TYPE> {
    public static WizardBuilder getInstance() { return new WizardBuilder(); }

    private MessageKey titleKey;
    private List<WizardStep> steps = Lists.newArrayList();
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

    public WizardBuilder addStep(MessageKey textKey, Parent content) {
        return addStep(textKey, content, null);
    }

    public WizardBuilder addStep(MessageKey textKey, Parent content, IWizardStepPopulator<INPUT_TYPE, ?> populator) {
        steps.add(new WizardStep(textKey, content, null, populator));
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
        stage.initModality(Modality.APPLICATION_MODAL);

        showBackdrop(stage);

        return stage;
    }

    private static void showBackdrop(Stage stage) {
        if (MainController.backdropRef != null && !MainController.backdropRef.isVisible()) {
            stage.setOnShown(event -> MainController.backdropRef.setVisible(true));
            stage.setOnHidden(event -> MainController.backdropRef.setVisible(false));
        }
    }
}
