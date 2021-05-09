package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;

@Component
public class DesktopUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopUtils.class);
	
	public static void openInBrowser(String uri) {
		try {
			Main.hostServices.showDocument(uri);
		} catch (Throwable e) {
			LOGGER.error("Error on opening browser for URI '{}'.", uri, e);
		}
	}
	
	public static void openInFileExplorer(String directoryPath) {
		try {
			if (Desktop.isDesktopSupported()) {
				// Has to run in a separate thread. Otherwise application will freeze on .open() call.
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(new File(directoryPath));
					} catch (Throwable e) {
						LOGGER.error("Error on opening directory '{}' in file explorer.", directoryPath, e);
					}
				}).start();
			}
		} catch (Throwable e) {
			LOGGER.error("Error on opening directory '{}' in file explorer.", directoryPath, e);
		}
	}
}
