package org.xmc.fe.ui.components;

import com.querydsl.core.QueryResults;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import scalc.SCalcBuilder;

import java.math.RoundingMode;

public class TableViewEx<ITEM_TYPE, SORT_ENUM_TYPE> extends VBox {
    private final TableView<ITEM_TYPE> tableView;
    private final Label placeholder;

    private final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(50);
    private final SimpleIntegerProperty page = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty lastPage = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty currentItemCount = new SimpleIntegerProperty(0);

    private ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> dataProvider;

    public TableViewEx() {
        tableView = new TableView<>();
        tableView.setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getItems().addListener((ListChangeListener<ITEM_TYPE>) c -> currentItemCount.set(tableView.getItems().size()));

        ToolBar toolBar = new ToolBar();

        Button firstPageButton = createImageButton(FeConstants.CHEVRONS_LEFT, MessageKey.PAGING_FIRST_PAGE, event -> {
            page.set(0);
            reload();
        });
        firstPageButton.disableProperty().bind(page.isEqualTo(0).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(firstPageButton);

        Button previousPageButton = createImageButton(FeConstants.CHEVRON_LEFT, MessageKey.PAGING_BACK, event -> {
            page.set(page.get() - 1);
            reload();
        });
        previousPageButton.disableProperty().bind(page.isEqualTo(0).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(previousPageButton);

        Button nextPageButton = createImageButton(FeConstants.CHEVRON_RIGHT, MessageKey.PAGING_NEXT, event -> {
            page.set(page.get() + 1);
            reload();
        });
        nextPageButton.disableProperty().bind(page.isEqualTo(lastPage).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(nextPageButton);

        Button lastPageButton = createImageButton(FeConstants.CHEVRONS_RIGHT, MessageKey.PAGING_LAST_PAGE, event -> {
            page.set(lastPage.get());
            reload();
        });
        lastPageButton.disableProperty().bind(page.isEqualTo(lastPage).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(lastPageButton);

        placeholder = new Label();
        placeholder.setMaxWidth(Double.MAX_VALUE);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(placeholder, Priority.ALWAYS);
        toolBar.getItems().add(placeholder);

        toolBar.getItems().add(createTextButton(String.valueOf(10), event -> { page.set(0); pageSize.set(10); reload(); }));
        toolBar.getItems().add(createTextButton(String.valueOf(25), event -> { page.set(0); pageSize.set(25); reload(); }));
        toolBar.getItems().add(createTextButton(String.valueOf(50), event -> { page.set(0); pageSize.set(50); reload(); }));
        toolBar.getItems().add(createTextButton(String.valueOf(100), event -> { page.set(0); pageSize.set(100); reload(); }));

        getChildren().add(tableView);
        getChildren().add(toolBar);

        reload();
    }

    private Button createTextButton(String text, EventHandler<ActionEvent> runnable) {
        Button button = new Button();
        button.setText(text);
        button.setOnAction(runnable);
        button.disableProperty().bind(currentItemCount.isEqualTo(0));
        return button;
    }

    private Button createImageButton(Image image, MessageKey messageKey, EventHandler<ActionEvent> runnable) {
        Button button = new Button();
        button.setGraphic(new ImageView(image));
        button.setTooltip(new Tooltip(MessageAdapter.getByKey(messageKey)));
        button.setOnAction(runnable);
        return button;
    }

    public void reload() {
        QueryResults<ITEM_TYPE> items = dataProvider == null
                ? QueryResults.emptyResults()
                : dataProvider.loadItems(page.get() * pageSize.get(), pageSize.get(), null, null);

        tableView.getItems().setAll(items.getResults());

        int currentPage, pageCount;
        if (items.isEmpty()) {
            currentPage = 0;
            pageCount = 0;
            lastPage.set(0);
        } else {
            currentPage = page.get() + 1;
            pageCount = SCalcBuilder.instanceFor(Integer.class)
                    .expression("total / pageSize")
                    .resultScale(0, RoundingMode.UP)
                    .build()
                    .parameter("total", items.getTotal())
                    .parameter("pageSize", pageSize.get())
                    .calc();
            lastPage.set(pageCount - 1);
        }

        placeholder.setText(String.format("%s / %s (%s: %s)", currentPage, pageCount, MessageAdapter.getByKey(MessageKey.PAGING_COUNT), items.getTotal()));
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> dataProvider) {
        this.dataProvider = dataProvider;
        reload();
    }

    public ObservableList<TableColumn<ITEM_TYPE, ?>> getColumns() {
        return tableView.getColumns();
    }

    public TableViewSelectionModel<ITEM_TYPE> getSelectionModel() {
        return tableView.getSelectionModel();
    }
}
