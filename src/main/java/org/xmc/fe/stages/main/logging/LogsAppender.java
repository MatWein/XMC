package org.xmc.fe.stages.main.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import javafx.application.Platform;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class LogsAppender extends AppenderBase<ILoggingEvent> {
	public static final CircularFifoQueue<String> LOGS = new CircularFifoQueue<>(5000);
	
	protected Encoder<ILoggingEvent> encoder;
	
	@Override
	public void start() {
		if (this.encoder == null) {
			addError("No encoder set for the appender named [" + name + "].");
			return;
		}
		
		encoder.start();
		super.start();
	}
	
	public void append(ILoggingEvent event) {
		synchronized (LOGS) {
			LOGS.add(new String(encoder.encode(event)));
		}
		
		if (event.getLevel() == Level.ERROR
				&& MainController.errorAlertImageViewRef != null
				&& MainController.statusLabelRef != null) {
			
			Platform.runLater(() -> {
				MainController.errorAlertImageViewRef.setVisible(true);
				MainController.statusLabelRef.setText(MessageAdapter.getByKey(MessageKey.STATUS_ERROR));
			});
		}
	}
	
	public Encoder<ILoggingEvent> getEncoder() {
		return encoder;
	}
	
	public void setEncoder(Encoder<ILoggingEvent> encoder) {
		this.encoder = encoder;
	}
}
