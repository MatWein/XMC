package org.xmc.fe.ui.components.table;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.xmc.common.utils.ImageUtil;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

public class NestedPropertyValueFactory implements Callback<TableColumn.CellDataFeatures, ObservableValue> {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + " HH:mm:ss";

    private String property;
    private Double fitToWidth;
    private Double fitToHeight;
    private boolean formatAsCurrency;
    private boolean formatAsPercentage;

    @Override
    public ObservableValue call(TableColumn.CellDataFeatures param) {
        return getCellDataReflectively(param.getValue());
    }

    private ObservableValue getCellDataReflectively(Object rowData) {
        if (property == null || property.isEmpty() || rowData == null) return null;

        try {
            Object value = PropertyUtils.getNestedProperty(rowData, property);
            Object mappedValue = mapValue(value);
            return new ReadOnlyObjectWrapper<>(mappedValue);
        } catch (NestedNullException | NoSuchMethodException e) {
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Could not read property '%s' from: %s", property, rowData), e);
        }
    }

    protected Object mapValue(Object value) {
        if (value instanceof byte[]) {
            return createImageView((byte[]) value);
        } else if (value instanceof Currency) {
            return ((Currency) value).getCurrencyCode();
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        } else if (value instanceof Number && formatAsCurrency) {
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(value);
        } else if (value instanceof Number && formatAsPercentage) {
            return NumberFormat.getPercentInstance(Locale.getDefault()).format(value);
        } else if (value instanceof Number) {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        }

        return value;
    }

    private Object createImageView(byte[] value) {
        ImageView imageView = new ImageView(ImageUtil.readFromByteArray$(value));

        if (fitToWidth != null) {
            imageView.setFitWidth(fitToWidth);
        }
        if (fitToHeight != null) {
            imageView.setFitHeight(fitToHeight);
        }

        return imageView;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Double getFitToWidth() {
        return fitToWidth;
    }

    public void setFitToWidth(Double fitToWidth) {
        this.fitToWidth = fitToWidth;
    }

    public Double getFitToHeight() {
        return fitToHeight;
    }

    public void setFitToHeight(Double fitToHeight) {
        this.fitToHeight = fitToHeight;
    }

    public boolean isFormatAsCurrency() {
        return formatAsCurrency;
    }

    public void setFormatAsCurrency(boolean formatAsCurrency) {
        this.formatAsCurrency = formatAsCurrency;
    }

    public boolean isFormatAsPercentage() {
        return formatAsPercentage;
    }

    public void setFormatAsPercentage(boolean formatAsPercentage) {
        this.formatAsPercentage = formatAsPercentage;
    }
}
