package org.xmc.be.services.importing.controller;

import org.xmc.common.stubs.importing.DtoImportFileValidationResultError;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

public interface IImportRowValidator<T extends Serializable> extends BiFunction<T, Integer, List<DtoImportFileValidationResultError>> {
}
