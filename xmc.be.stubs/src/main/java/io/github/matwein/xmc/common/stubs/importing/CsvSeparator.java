package io.github.matwein.xmc.common.stubs.importing;

public enum CsvSeparator {
	SEMICOLON(';'),
	COMMA(','),
	TAB('\t');
	
	private final char character;
	
	CsvSeparator(char character) {
		this.character = character;
	}
	
	public char getCharacter() {
		return character;
	}
}
