package org.xmc.fe.ui.wizard;

public interface IWizardStepPopulator<INPUT_TYPE, CONTROLLER_TYPE> {
    void populateState(INPUT_TYPE input, CONTROLLER_TYPE controller);
    void populateGui(INPUT_TYPE input, CONTROLLER_TYPE controller);
}
