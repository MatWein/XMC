package org.xmc.be.services.cashaccount.controller.importing;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;

import java.io.Serializable;
import java.util.List;

@Component
public class DtoImportFileValidationResultMapper {
	public <T extends Serializable> DtoImportFileValidationResult<T> map(
			List<List<String>> rawFileContent,
			Class<T> resultType) {
		
		return null;
	}
}
