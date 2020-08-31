package org.xmc.fe.importing;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DtoColumnMappingFactory {
    public <T extends Enum<T>> List<DtoColumnMapping<T>> generateEmptyColumns() {
        return (List)IntStream.rangeClosed(1, 26)
                .boxed()
                .map(i -> new DtoColumnMapping(i, null))
                .collect(Collectors.toList());
    }
}
