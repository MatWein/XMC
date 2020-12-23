package org.xmc.fe.ui;

import javax.annotation.Nullable;

public interface IAfterStageShown<T> {
    void afterStageShown(@Nullable T param);
}
