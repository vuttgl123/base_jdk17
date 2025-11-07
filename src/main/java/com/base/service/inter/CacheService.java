package com.base.service.inter;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    void saveValue(String key, Object value);
    void saveValue(String key, Object value, long timeout, TimeUnit unit);
    Object getValue(String key);
    void delete(String key);
    void delete(Collection<String> keys);
    Boolean hasKey(String key);
    Set<String> getKeys(String pattern);
}