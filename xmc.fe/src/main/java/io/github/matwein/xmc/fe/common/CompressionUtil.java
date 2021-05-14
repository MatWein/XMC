package io.github.matwein.xmc.fe.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressionUtil {
	public static void zipDirectory(String sourceDirPath, String zipFilePath) {
		try {
			Path zipFile = Files.createFile(Paths.get(zipFilePath));
			
			try (var zs = new ZipOutputStream(Files.newOutputStream(zipFile))) {
				Path sourceDir = Paths.get(sourceDirPath);
				Files.walk(sourceDir)
						.filter(path -> !Files.isDirectory(path))
						.forEach(path -> createZipEntry(zs, sourceDir, path));
			}
		} catch (IOException e) {
			String message = String.format("Error on zipping file '%s'.", sourceDirPath);
			throw new RuntimeException(message, e);
		}
	}
	
	private static void createZipEntry(ZipOutputStream zs, Path sourceDir, Path path) {
		ZipEntry zipEntry = new ZipEntry(sourceDir.getFileName().resolve(sourceDir.relativize(path)).toString());
		try {
			zs.putNextEntry(zipEntry);
			Files.copy(path, zs);
			zs.closeEntry();
		} catch (IOException e) {
			String message = String.format("Error on zipping file '%s'.", path);
			throw new RuntimeException(message, e);
		}
	}
}
