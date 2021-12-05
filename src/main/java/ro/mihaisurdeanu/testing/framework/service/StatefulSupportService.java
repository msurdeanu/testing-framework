package ro.mihaisurdeanu.testing.framework.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public abstract class StatefulSupportService {

    private static final Map<String, Map<String, Object>> OBJECT_CACHE_MAP = new ConcurrentHashMap<>();

    public Object getFromCache(final String key) {
        return Optional.ofNullable(OBJECT_CACHE_MAP.get(Thread.currentThread().getName()))
                .map(threadCache -> threadCache.get(key))
                .orElse(null);
    }

    public void putInCache(final String key, final Object value) {
        final var threadName = Thread.currentThread().getName();

        Map<String, Object> threadCache = OBJECT_CACHE_MAP.get(threadName);
        if (threadCache == null) {
            threadCache = new HashMap<>();
            threadCache.put(key, value);
            OBJECT_CACHE_MAP.put(threadName, threadCache);
        } else {
            threadCache.put(key, value);
        }
    }

    public void removeAllFromCache() {
        OBJECT_CACHE_MAP.remove(Thread.currentThread().getName());
    }

}
