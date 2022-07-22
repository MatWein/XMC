package io.github.matwein.xmc.fe.ui;

import org.springframework.lang.Nullable;

public interface IAfterStageShown<T> {
    void afterStageShown(@Nullable T param);
}
