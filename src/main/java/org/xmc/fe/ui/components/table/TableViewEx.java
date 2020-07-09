package org.xmc.fe.ui.components.table;

import com.google.common.collect.Iterables;
import com.querydsl.core.QueryResults;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import org.xmc.Main;
import org.xmc.common.stubs.IPagingField;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.FeConstants;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import scalc.SCalcBuilder;

import java.math.RoundingMode;

public class TableViewEx<ITEM_TYPE, SORT_ENUM_TYPE extends Enum<SORT_ENUM_TYPE> & IPagingField> extends VBox {
    private final TableView<ITEM_TYPE> tableView;
    private final Label placeholder;
    private final TextField filterTextfield;

    private final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(50);
    private final SimpleIntegerProperty page = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty lastPage = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty currentItemCount = new SimpleIntegerProperty(0);
    private final TableOrderMapper orderMapper;

    private ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> dataProvider;
    private Class<SORT_ENUM_TYPE> fieldEnumType;
    private SortType sortType;
    private SORT_ENUM_TYPE sortBy;

    public TableViewEx() {
        orderMapper = (TableOrderMapper)ReflectionUtil.createNewInstanceFactory().call(TableOrderMapper.class);

        tableView = new TableView<>();
        tableView.setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getItems().addListener((ListChangeListener<ITEM_TYPE>) c -> currentItemCount.set(tableView.getItems().size()));
        getColumns().addListener(this::validateNewColumns);
        currentItemCount.addListener((observable, oldValue, newValue) -> {
            for (TableColumnEx<ITEM_TYPE, ?> column : getColumns()) {
                column.setVisible(newValue.intValue() > 0);
            }
        });

        ToolBar toolBar = new ToolBar();

        Button firstPageButton = createImageButton(FeConstants.CHEVRONS_LEFT, MessageKey.PAGING_FIRST_PAGE, event -> reload(true));
        firstPageButton.disableProperty().bind(page.isEqualTo(0).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(firstPageButton);

        Button previousPageButton = createImageButton(FeConstants.CHEVRON_LEFT, MessageKey.PAGING_BACK, event -> {
            page.set(page.get() - 1);
            reload(false);
        });
        previousPageButton.disableProperty().bind(page.isEqualTo(0).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(previousPageButton);

        PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);

        filterTextfield = new TextField();
        filterTextfield.setPrefWidth(250);
        filterTextfield.setPromptText(MessageAdapter.getByKey(MessageKey.PAGING_FILTER_PROMPT));
        filterTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(e -> reload(true));
            pause.playFromStart();
        });
        toolBar.getItems().add(filterTextfield);

        Button nextPageButton = createImageButton(FeConstants.CHEVRON_RIGHT, MessageKey.PAGING_NEXT, event -> {
            page.set(page.get() + 1);
            reload(false);
        });
        nextPageButton.disableProperty().bind(page.isEqualTo(lastPage).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(nextPageButton);

        Button lastPageButton = createImageButton(FeConstants.CHEVRONS_RIGHT, MessageKey.PAGING_LAST_PAGE, event -> {
            page.set(lastPage.get());
            reload(false);
        });
        lastPageButton.disableProperty().bind(page.isEqualTo(lastPage).or(currentItemCount.isEqualTo(0)));
        toolBar.getItems().add(lastPageButton);

        placeholder = new Label();
        placeholder.setMaxWidth(Double.MAX_VALUE);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setTextAlignment(TextAlignment.CENTER);
        placeholder.disableProperty().bind(currentItemCount.isEqualTo(0));
        HBox.setHgrow(placeholder, Priority.ALWAYS);
        toolBar.getItems().add(placeholder);

        toolBar.getItems().add(createTextButton(String.valueOf(10), event -> { pageSize.set(10); reload(true); }));
        toolBar.getItems().add(createTextButton(String.valueOf(25), event -> { pageSize.set(25); reload(true); }));
        toolBar.getItems().add(createTextButton(String.valueOf(50), event -> { pageSize.set(50); reload(true); }));
        toolBar.getItems().add(createTextButton(String.valueOf(100), event -> { pageSize.set(100); reload(true); }));

        getChildren().add(tableView);
        getChildren().add(toolBar);
    }

    private void onSort() {
        TableColumnEx<ITEM_TYPE, ?> columnToSort = (TableColumnEx<ITEM_TYPE, ?>) Iterables.getFirst(tableView.getSortOrder(), null);
        if (columnToSort == null) {
            sortType = null;
            sortBy = null;
        } else {
            sortType = columnToSort.getSortType();
            sortBy = Enum.valueOf(fieldEnumType, columnToSort.getSortField());
        }

        reload(true);
    }

    private void validateNewColumns(Change<? extends TableColumnEx<ITEM_TYPE, ?>> c) {
        if (c.next()) {
            for (TableColumnEx<ITEM_TYPE, ?> column : c.getAddedSubList()) {
                if (column.isSortable() && StringUtils.isBlank(column.getSortField())) {
                    throw new IllegalArgumentException(String.format("Sortable column '%s' must have specified a sort field!", column.getText()));
                }
            }
        }
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
        reload(true);
    }

    public void reload(boolean resetPage) {
        if (resetPage) {
            page.set(0);
        }

        Main.applicationContext.getBean(AsyncProcessor.class).runAsync(
                this::loadItemsFromProvider,
                this::updateItems
        );
    }

    private QueryResults<ITEM_TYPE> loadItemsFromProvider(AsyncMonitor monitor) {
        if (dataProvider == null) {
            return QueryResults.emptyResults();
        }

        int offset = page.get() * pageSize.get();
        int limit = pageSize.get();

        return dataProvider.loadItems(monitor, new PagingParams<>(
                offset, limit,
                sortBy, orderMapper.mapOrder(sortType),
                filterTextfield.getText()));
    }

    private void updateItems(QueryResults<ITEM_TYPE> items) {
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

        // This will automatically reload the table.
        // If we set the policy in constructor and call reload() here, the table will reload two times.
        tableView.setSortPolicy(param -> {
            onSort();
            return true;
        });
    }

    public ObservableList<TableColumnEx<ITEM_TYPE, ?>> getColumns() {
        return (ObservableList)tableView.getColumns();
    }

    public TableViewSelectionModel<ITEM_TYPE> getSelectionModel() {
        return tableView.getSelectionModel();
    }

    public String getFieldEnumType() {
        return fieldEnumType.getName();
    }

    public void setFieldEnumType(String fieldEnumType) {
        this.fieldEnumType = (Class)ReflectionUtil.forName(fieldEnumType);
    }
}
