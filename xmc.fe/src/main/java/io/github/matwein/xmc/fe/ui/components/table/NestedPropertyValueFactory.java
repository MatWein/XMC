package io.github.matwein.xmc.fe.ui.components.table;

import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.Percentage;
import io.github.matwein.xmc.fe.common.ImageUtil;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

public class NestedPropertyValueFactory implements Callback<CellDataFeatures, ObservableValue> {
    private String property;
    private Double fitToWidth;
    private Double fitToHeight;
    private int fractionDigits = 2;
    private String translationKey;
    private Double maxHeight;
    private boolean useTextArea = false;
    private boolean abbreviate = true;

    @Override
    public ObservableValue call(CellDataFeatures param) {
        return getCellDataReflectively(param.getValue());
    }

    private ObservableValue getCellDataReflectively(Object rowData) {
        if (property == null || property.isEmpty() || rowData == null) return null;

        try {
            Object value = PropertyUtils.getNestedProperty(rowData, property);
	        
            Node nodeForValue = mapValue(value);
            if (nodeForValue instanceof Text && useTextArea) {
	            Text textNode = (Text) nodeForValue;
	            
	            TextArea textArea = new TextArea(textNode.getText());
	            textArea.setPrefHeight(textNode.getLayoutBounds().getHeight() + 50.0);
	            textArea.setPrefWidth(textNode.getLayoutBounds().getWidth() + 50.0);
	            textArea.setEditable(false);
	            
	            if (maxHeight != null) {
		            textArea.setPrefHeight(Math.min(textArea.getPrefHeight(), maxHeight));
		            textArea.setMaxHeight(maxHeight);
	            }
	            
	            nodeForValue = textArea;
            }
	        
            return new ReadOnlyObjectWrapper<>(nodeForValue);
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
		    return createText(text);
	    }
        
        if (value instanceof String) {
            return createText((String) value, value);
        } else if (value instanceof byte[]) {
            return createImageView((byte[]) value);
        } else if (value instanceof Currency) {
            return createText(((Currency) value).getCurrencyCode(), value);
        } else if (value instanceof LocalDateTime) {
	        LocalDateTime localDateTime = (LocalDateTime) value;
	        return createText(MessageAdapter.formatDateTime(localDateTime), value);
        } else if (value instanceof LocalDate) {
	        LocalDate localDate = (LocalDate) value;
	        return createText(MessageAdapter.formatDate(localDate), value);
        } else if (value instanceof Number) {
            return createText(createNumberInstance().format(value), value);
        } else if (value instanceof Money) {
            Money money = (Money) value;
            return createText(createNumberInstance().format(money.getValue()) + " " + money.getCurrency(), value);
        } else if (value instanceof Percentage) {
            Percentage percentage = (Percentage) value;
            return createText(createNumberInstance().format(percentage.getValue()) + " %", value);
        }

        return createText(value.toString(), value);
    }
	
	private Text createText(String textToShow, Object userData) {
		Text text = createText(textToShow);
		text.setUserData(userData);
		return text;
	}
    
    private Text createText(String textToShow) {
    	if (abbreviate) {
		    return new Text(StringUtils.abbreviate(textToShow, 255));
	    } else {
		    return new Text(textToShow);
	    }
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
	
	public Double getMaxHeight() {
		return maxHeight;
	}
	
	public void setMaxHeight(Double maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	public boolean isAbbreviate() {
		return abbreviate;
	}
	
	public void setAbbreviate(boolean abbreviate) {
		this.abbreviate = abbreviate;
	}
	
	public boolean isUseTextArea() {
		return useTextArea;
	}
	
	public void setUseTextArea(boolean useTextArea) {
		this.useTextArea = useTextArea;
	}
}
