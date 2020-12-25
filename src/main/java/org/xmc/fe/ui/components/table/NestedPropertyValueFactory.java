package org.xmc.fe.ui.components.table;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.xmc.common.stubs.Money;
import org.xmc.common.stubs.Percentage;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

public class NestedPropertyValueFactory implements Callback<CellDataFeatures, ObservableValue> {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + " HH:mm:ss";

    private String property;
    private Double fitToWidth;
    private Double fitToHeight;
    private int fractionDigits = 2;
    private String translationKey;

    @Override
    public ObservableValue call(CellDataFeatures param) {
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

    protected Node mapValue(Object value) {
        if (value == null) {
            return null;
        }
	
	    if (value instanceof Enum && StringUtils.isNotBlank(translationKey)) {
		    MessageKey messageKey = MessageKey.valueOf(translationKey);
		    String text = MessageAdapter.getByKey(messageKey, (Enum) value);
		    return new Text(StringUtils.abbreviate(text, 255));
	    }
        
        if (value instanceof String) {
            return new Text(StringUtils.abbreviate((String) value, 255));
        } else if (value instanceof byte[]) {
            return createImageView((byte[]) value);
        } else if (value instanceof Currency) {
            return new Text(((Currency) value).getCurrencyCode());
        } else if (value instanceof LocalDateTime) {
            return new Text(((LocalDateTime) value).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        } else if (value instanceof LocalDate) {
            return new Text(((LocalDate) value).format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        } else if (value instanceof Number) {
            return new Text(createNumberInstance().format(value));
        } else if (value instanceof Money) {
            Money money = (Money) value;
            return new Text(createNumberInstance().format(money.getValue()) + " " + money.getCurrency());
        } else if (value instanceof Percentage) {
            Percentage percentage = (Percentage) value;
            return new Text(createNumberInstance().format(percentage.getValue()) + " %");
        }

        return new Text(value.toString());
    }

    private NumberFormat createNumberInstance() {
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.getDefault());
        numberInstance.setMinimumFractionDigits(fractionDigits);
        numberInstance.setMaximumFractionDigits(fractionDigits);
        return numberInstance;
    }

    private ImageView createImageView(byte[] value) {
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

    public int getFractionDigits() {
        return fractionDigits;
    }

    public void setFractionDigits(int fractionDigits) {
        this.fractionDigits = fractionDigits;
    }
	
	public String getTranslationKey() {
		return translationKey;
	}
	
	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}
}
