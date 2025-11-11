package com.base.configuration.config;

import com.base.configuration.MdcTaskDecorator;
import com.base.exception.ExceptionHandlingAsyncTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final TaskExecutionProperties props;
    private final MdcTaskDecorator mdcTaskDecorator;

    public AsyncConfig(TaskExecutionProperties props) {
        this.props = props;
        // Tái sử dụng instance thay vì tạo mới mỗi lần
        this.mdcTaskDecorator = new MdcTaskDecorator();
    }

    /**
     * Tạo ThreadPoolTaskExecutor với cấu hình tối ưu
     */
    private ThreadPoolTaskExecutor buildThreadPool(String prefix, int core, int max, int queue) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool configuration
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(max);
        executor.setQueueCapacity(queue);
        executor.setThreadNamePrefix(prefix);

        // Task decorator (reuse instance)
        executor.setTaskDecorator(mdcTaskDecorator);

        // Graceful shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        // Rejection policy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Thread lifecycle optimization
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(60);

        executor.initialize();
        return executor;
    }

    @Override
    @Bean(name = "taskExecutor")
    @Primary
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor delegate = buildThreadPool(
                "async-",
                props.getPool().getCoreSize(),
                props.getPool().getMaxSize(),
                props.getPool().getQueueCapacity()
        );
        return new ExceptionHandlingAsyncTaskExecutor(delegate);
    }

    @Bean(name = "mdcTaskExecutor")
    public Executor mdcTaskExecutor() {
        ThreadPoolTaskExecutor delegate = buildThreadPool("mdc-async-", 8, 16, 1000);
        return new ExceptionHandlingAsyncTaskExecutor(delegate);
    }

    /**
     * Raw executor cho các tác vụ không cần exception handling wrapper
     */
    @Bean(name = "mdcThreadPoolRaw")
    public ThreadPoolTaskExecutor mdcThreadPoolRaw() {
        return buildThreadPool("mdc-raw-", 4, 8, 200);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Uncaught @Async exception in {}.{}() with params: {}",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(),
                        params,
                        ex);
    }
}