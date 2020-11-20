package org.xmc.common;

public enum FileMimeType {
	PNG("png", "image/png"),
	JPG("jpg", "image/jpeg"),
	GIF("gif", "image/gif"), 

	PDF("pdf", "application/pdf"),
	HTML("html", "text/html"),
	TEXT("txt", "text/plain"),
	CSV("txt", "text/csv"),
	
	MS_WORD("doc", "application/msword"),
	MS_WORDX("docx", "application/msword"),
	MS_EXCEL("xls", "application/excel"),
	MS_EXCELX("xlsx", "application/excel"),
	MS_EXCELO("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	MS_POWERPOINT("ppt", "application/mspowerpoint"),
	MS_POWERPOINTX("pptx", "application/mspowerpoint"),
	
	GZIP1("gzip", "application/x-gzip"),
	GZIP2("gz", "application/x-gzip"),
	ZIP("zip", "application/x-compressed"),
	
	UNKNOWN("bin", "application/octet-stream");

	private final String fileExtension;
	private final String mimeType;

	FileMimeType(String fileExtension, String mimeType) {
		this.fileExtension = fileExtension;
		this.mimeType = mimeType;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getMimeType() {
		return mimeType;
	}
}
