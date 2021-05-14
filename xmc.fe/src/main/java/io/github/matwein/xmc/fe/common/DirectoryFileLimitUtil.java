package io.github.matwein.xmc.fe.common;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryFileLimitUtil {
	public static void deleteOldestFiles(File directory, int numberOfFilesToKeep) {
		File[] allFilesInDir = directory.listFiles();
		if (allFilesInDir == null) {
			return;
		}
		
		List<File> filesToKeep = Stream.of(allFilesInDir)
				.sorted(Comparator.comparing(File::lastModified).reversed())
				.limit(numberOfFilesToKeep)
				.collect(Collectors.toList());
		
		for (File file : allFilesInDir) {
			if (filesToKeep.contains(file)) {
				continue;
			}
			
			file.delete();
		}
	}
}
