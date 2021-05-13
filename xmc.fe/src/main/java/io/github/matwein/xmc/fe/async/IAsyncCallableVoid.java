package io.github.matwein.xmc.fe.async;

public interface IAsyncCallableVoid {
    void call(AsyncMonitor monitor) throws Exception;
}
