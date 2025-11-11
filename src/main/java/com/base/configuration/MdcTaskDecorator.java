package com.base.configuration;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Decorator để truyền MDC context vào async threads
 * Tối ưu để giảm thiểu object allocation và null checks
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        // Lấy snapshot của MDC context từ parent thread
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            // Backup context hiện tại của worker thread (nếu có)
            final Map<String, String> previous = MDC.getCopyOfContextMap();

            try {
                // Restore parent thread's context
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear();
                }

                runnable.run();
            } finally {
                // Restore worker thread's original context
                restoreMdcContext(previous);
            }
        };
    }

    private void restoreMdcContext(Map<String, String> previous) {
        if (previous != null) {
            MDC.setContextMap(previous);
        } else {
            MDC.clear();
        }
    }
}