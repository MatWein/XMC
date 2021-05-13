package io.github.matwein.xmc.fe.ui.components.table;

import com.google.common.collect.Iterables;
import io.github.matwein.xmc.common.stubs.IPagingField;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.common.ReflectionUtil;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import scalc.SCalcBuilder;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.function.Consumer;

public class ExtendedTable<ITEM_TYPE extends Serializable, SORT_ENUM_TYPE extends Enum<SORT_ENUM_TYPE> & IPagingField> extends VBox {
    public static final double MAX_AUTORESIZE_COLUMN_WIDTH = 400.0;

    private final BaseTable<ITEM_TYPE> table;
    private final Label placeholder;
    private final TextField filterTextfield;

    private final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(50);
    private final SimpleIntegerProperty page = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty lastPage = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty currentItemCount = new SimpleIntegerProperty(0);
    private final TableOrderMapper orderMapper;

    private ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> dataProvider;
    private Consumer<ITEM_TYPE> doubleClickConsumer;
    private Class<SORT_ENUM_TYPE> fieldEnumType;
    private SortType sortType;
    private SORT_ENUM_TYPE sortBy;
    private boolean autoResize = true;

    public ExtendedTable() {
        orderMapper = (TableOrderMapper) XmcFrontendContext.createNewInstanceFactory().call(TableOrderMapper.class);

        table = new BaseTable<>();
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getItems().addListener((ListChangeListener<ITEM_TYPE>) c -> currentItemCount.set(table.getItems().size()));
        getColumns().addListener(this::validateNewColumns);
        currentItemCount.addListener((observable, oldValue, newValue) -> {
            for (ExtendedTableColumn<ITEM_TYPE, ?> column : getColumns()) {
                column.setVisible(newValue.intValue() > 0);
            }
        });

        table.setRowFactory(tv -> {
            TableRow<ITEM_TYPE> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ITEM_TYPE rowData = row.getItem();
                    if (doubleClickConsumer != null) {
                        doubleClickConsumer.accept(rowData);
                    }
                }
            });
            return row;
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

        toolBar.getItems().add(createTextButton(String.valueOf(10), event -> {
            pageSize.set(10);
            reload(true);
        }));
        toolBar.getItems().add(createTextButton(String.valueOf(25), event -> {
            pageSize.set(25);
            reload(true);
        }));
        toolBar.getItems().add(createTextButton(String.valueOf(50), event -> {
            pageSize.set(50);
            reload(true);
        }));
        toolBar.getItems().add(createTextButton(String.valueOf(100), event -> {
            pageSize.set(100);
            reload(true);
        }));

        getChildren().add(table);
        getChildren().add(toolBar);
    }

    public void autoAdjustColumnSize() {
        if (!autoResize) {
            return;
        }

        table.setColumnResizePolicy(BaseTable.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn<ITEM_TYPE, ?> column : table.getColumns()) {
            if (!column.isResizable() || StringUtils.isBlank(column.getText()) || ((ExtendedTableColumn) column).isAvoidAutoResize()) {
                continue;
            }

            Node text = new Text(column.getText());
            double max = text.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                if (column.getCellData(i) instanceof Node) {
                    text = (Node)column.getCellData(i);
                    
                    double calcwidth = text.getLayoutBounds().getWidth();
                    if (calcwidth == 0.0 && text instanceof Region) {
	                    calcwidth = ((TextArea) text).getPrefWidth() + 50.0;
                    }
                    
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            column.setMaxWidth(Double.MAX_VALUE);
            column.setPrefWidth(Math.min(MAX_AUTORESIZE_COLUMN_WIDTH, max + 50.0));
        }
    }

    private void onSort() {
        ExtendedTableColumn<ITEM_TYPE, ?> columnToSort = (ExtendedTableColumn<ITEM_TYPE, ?>) Iterables.getFirst(table.getSortOrder(), null);
        if (columnToSort == null) {
            sortType = null;
            sortBy = null;
        } else {
            sortType = columnToSort.getSortType();
            sortBy = Enum.valueOf(fieldEnumType, columnToSort.getSortField());
        }

        reload(true);
    }

    private void validateNewColumns(Change<? extends ExtendedTableColumn<ITEM_TYPE, ?>> c) {
        if (c.next()) {
            for (ExtendedTableColumn<ITEM_TYPE, ?> column : c.getAddedSubList()) {
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

        if (XmcFrontendContext.applicationContext.get() == null) {
        	return;
        }
	
	    XmcFrontendContext.applicationContext.get().getBean(AsyncProcessor.class).runAsync(
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
    	if (items == null) {
    		items = QueryResults.emptyResults();
	    }
    	
        table.getItems().setAll(items.getResults());

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
        Platform.runLater(this::autoAdjustColumnSize);
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
        table.setSortPolicy(param -> {
            onSort();
            return true;
        });
    }

    public ObservableList<ExtendedTableColumn<ITEM_TYPE, ?>> getColumns() {
        return (ObservableList) table.getColumns();
    }

    public TableViewSelectionModel<ITEM_TYPE> getSelectionModel() {
        return table.getSelectionModel();
    }

    public String getFieldEnumType() {
        return fieldEnumType.getName();
    }

    public void setFieldEnumType(String fieldEnumType) {
        this.fieldEnumType = (Class) ReflectionUtil.forName(fieldEnumType);
    }

    public boolean isAutoResize() {
        return autoResize;
    }

    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

    public Consumer<ITEM_TYPE> getDoubleClickConsumer() {
        return doubleClickConsumer;
    }

    public void setDoubleClickConsumer(Consumer<ITEM_TYPE> doubleClickConsumer) {
        this.doubleClickConsumer = doubleClickConsumer;
    }

    public SelectionMode getSelectionMode() {
        return table.getSelectionModel().getSelectionMode();
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        table.getSelectionModel().setSelectionMode(selectionMode);
    }
    
    public boolean isFilteringDisable() {
    	return filterTextfield.isDisable();
    }
    
    public void setFilteringDisable(boolean filteringDisable) {
	    filterTextfield.setDisable(filteringDisable);
    }
}
