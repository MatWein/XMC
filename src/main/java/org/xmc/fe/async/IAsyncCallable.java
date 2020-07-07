package org.xmc.fe.async;

public interface IAsyncCallable<RESULT_TYPE> {
    RESULT_TYPE call(AsyncMonitor monitor) throws Exception;
}
