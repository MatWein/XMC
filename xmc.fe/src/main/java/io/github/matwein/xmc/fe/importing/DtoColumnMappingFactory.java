package io.github.matwein.xmc.fe.importing;

import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("rawtypes")
@Component
public class DtoColumnMappingFactory {
	private static final int FIRST = 1;
	private static final int LAST = 26;
	
	public <T extends Enum<T>> List<DtoColumnMapping<T>> generateEmptyColumns() {
        return (List)IntStream.rangeClosed(FIRST, LAST)
                .boxed()
                .map(i -> new DtoColumnMapping(i, null))
                .collect(Collectors.toList());
    }
	
	public <T extends Enum<T>> List<DtoColumnMapping<T>> generateMissingColumns(List<DtoColumnMapping<T>> existingColumns) {
		Map<Integer, DtoColumnMapping<T>> index = existingColumns.stream()
				.collect(Collectors.toMap(DtoColumnMapping::getColumn, Function.identity()));
		
		return (List)IntStream.rangeClosed(FIRST, LAST)
				.boxed()
				.map(i -> Optional.ofNullable(index.get(i)).orElse(new DtoColumnMapping(i, null)))
				.collect(Collectors.toList());
	}
}
