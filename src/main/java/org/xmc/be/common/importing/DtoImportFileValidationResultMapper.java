package org.xmc.be.common.importing;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultErrors;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class DtoImportFileValidationResultMapper {
	public <RESULT_TYPE extends Serializable, COLUMN_TYPE extends Enum<COLUMN_TYPE>> DtoImportFileValidationResult<RESULT_TYPE> map(
			List<List<String>> rawFileContent,
			List<DtoColumnMapping<COLUMN_TYPE>> colmuns,
			BiFunction<List<String>, List<DtoColumnMapping<COLUMN_TYPE>>, RESULT_TYPE> lineToDtoMapper,
			BiFunction<RESULT_TYPE, Integer, List<DtoImportFileValidationResultErrors>> lineValidator) {
		
		DtoImportFileValidationResult<RESULT_TYPE> result = new DtoImportFileValidationResult<>();
		
		int currentLineIndex = 1;
		for (List<String> line : rawFileContent) {
			RESULT_TYPE mappedLine = lineToDtoMapper.apply(line, colmuns);
			List<DtoImportFileValidationResultErrors> errors = lineValidator.apply(mappedLine, currentLineIndex++);
			
			if (errors.isEmpty()) {
				result.getSuccessfullyReadLines().add(mappedLine);
				result.setValidTransactionCount(result.getValidTransactionCount() + 1);
			} else {
				result.getErrors().addAll(errors);
				result.setInvalidTransactionCount(result.getInvalidTransactionCount() + 1);
			}
		}
		
		return result;
	}
}
