package org.xmc.be.services.importing.controller;

import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

public interface IImportRowMapper<RESULT_DTO_TYPE extends Serializable, IMPORT_COLUMN_ENUM extends Enum<IMPORT_COLUMN_ENUM>> extends BiFunction<
		List<String>,
		List<DtoColumnMapping<IMPORT_COLUMN_ENUM>>,
		RESULT_DTO_TYPE> {
	
}
