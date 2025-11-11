package com.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Wrapper executor để handle exceptions trong async tasks
 * Tối ưu: giảm lambda allocation, cải thiện error logging
 */
@Slf4j
public class ExceptionHandlingAsyncTaskExecutor
        implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    private static final String EXCEPTION_MESSAGE = "Caught async exception";
    private final AsyncTaskExecutor executor;

    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    /**
     * Wrap Runnable để catch và log exceptions
     */
    private Runnable createWrappedRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handleException(e);
            }
        };
    }

    /**
     * Wrap Callable để catch, log và re-throw exceptions
     */
    private <T> Callable<T> createCallable(Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handleException(e);
                throw e; // Re-throw để caller có thể handle
            }
        };
    }

    /**
     * Centralized exception handling với structured logging
     */
    protected void handleException(Exception e) {
        log.error("{}: {} - {}",
                EXCEPTION_MESSAGE,
                e.getClass().getSimpleName(),
                e.getMessage(),
                e);
    }

    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean disposableBean) {
            disposableBean.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean initializingBean) {
            initializingBean.afterPropertiesSet();
        }
    }
}