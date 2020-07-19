package org.xmc.fe.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.function.Function;

public class ComboBoxIconCellFactory<T> implements Callback<ListView<T>, ListCell<T>> {
    private final Function<T, Image> iconExtractor;
    private final Function<T, String> textExtractor;

    public ComboBoxIconCellFactory(Function<T, Image> iconExtractor, Function<T, String> textExtractor) {
        this.iconExtractor = iconExtractor;
        this.textExtractor = textExtractor;
    }

    @Override
    public ListCell<T> call(ListView<T> listView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(createRowComponent(item));
                }
            }
        };
    }

    private HBox createRowComponent(T item) {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0)");
        hbox.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView(iconExtractor.apply(item));
        hbox.getChildren().add(imageView);

        Label label = new Label(textExtractor.apply(item));
        label.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
        hbox.getChildren().add(label);

        return hbox;
    }
}
