package io.github.matwein.xmc.fe.stages.login.logic;

import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.fe.common.CompressionUtil;
import io.github.matwein.xmc.fe.common.DirectoryFileLimitUtil;
import io.github.matwein.xmc.fe.common.HomeDirectoryPathCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupController.class);
	
	private static final int BACKUPS_TO_KEEP = 5;
	
	public static void backupFiles(DtoBootstrapFile dtoBootstrapFile) {
		String databaseDirToBackup = HomeDirectoryPathCalculator.calculateDatabaseDirForUser(dtoBootstrapFile.getUsername());
		File backupDir = new File(HomeDirectoryPathCalculator.calculateBackupDirForUser(dtoBootstrapFile.getUsername()));
		
		String backupFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".zip";
		String backupOutputFile = new File(backupDir, backupFileName).getAbsolutePath();
		
		if (new File(databaseDirToBackup).isDirectory() && (backupDir.isDirectory() || backupDir.mkdirs())) {
			try {
				CompressionUtil.zipDirectory(databaseDirToBackup, backupOutputFile);
			} catch (Throwable e) {
				LOGGER.warn("Error on zipping database dir '{}'.", databaseDirToBackup, e);
			}
		} else {
			LOGGER.warn("Cannot create directory '{}'.", backupDir);
		}
		
		DirectoryFileLimitUtil.deleteOldestFiles(backupDir, BACKUPS_TO_KEEP);
	}
}
