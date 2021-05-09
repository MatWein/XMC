package io.github.matwein.xmc.fe.async;

public interface IAsyncCallable<RESULT_TYPE> {
    RESULT_TYPE call(AsyncMonitor monitor) throws Exception;
}
