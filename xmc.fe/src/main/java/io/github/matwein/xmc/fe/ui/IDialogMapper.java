package io.github.matwein.xmc.fe.ui;

import javafx.scene.control.ButtonBar.ButtonData;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface IDialogMapper<CONTROLLER, DTO> extends BiFunction<ButtonData, CONTROLLER, DTO>, BiConsumer<CONTROLLER, DTO> {
}
