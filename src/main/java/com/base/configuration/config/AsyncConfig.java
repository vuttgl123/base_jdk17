// AsyncConfig.java
package com.base.configuration.config;

import com.base.configuration.MdcTaskDecorator;
import com.base.exception.ExceptionHandlingAsyncTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final TaskExecutionProperties props;

    public AsyncConfig(TaskExecutionProperties props) {
        this.props = props;
    }

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();
        delegate.setCorePoolSize(props.getPool().getCoreSize());
        delegate.setMaxPoolSize(props.getPool().getMaxSize());
        delegate.setQueueCapacity(props.getPool().getQueueCapacity());
        delegate.setThreadNamePrefix(props.getThreadNamePrefix());
        delegate.setTaskDecorator(new MdcTaskDecorator());
        delegate.setWaitForTasksToCompleteOnShutdown(true);
        delegate.setAwaitTerminationSeconds(30);
        delegate.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        delegate.initialize();

        return new ExceptionHandlingAsyncTaskExecutor(delegate);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Uncaught @Async error in {} with params {}", method, params, ex);
    }
}
