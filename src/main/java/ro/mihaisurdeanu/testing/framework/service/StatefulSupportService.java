package ro.mihaisurdeanu.testing.framework.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public abstract class StatefulSupportService {

    private static final Map<String, Map<String, Object>> OBJECT_CACHE_MAP = new ConcurrentHashMap<>();

    public Object getFromCache(final String key) {
        return ofNullable(OBJECT_CACHE_MAP.get(Thread.currentThread().getName()))
                .map(threadCache -> threadCache.get(key))
                .orElse(null);
    }

    public Object getFromCache(final String[] keys) {
        if (keys == null || keys.length != 1) {
            return null;
        }

        return getFromCache(keys[0]);
    }

    public void putInCache(final String key, final Object value) {
        final var threadName = Thread.currentThread().getName();

        var threadCache = OBJECT_CACHE_MAP.get(threadName);
        if (threadCache == null) {
            threadCache = new HashMap<>();
            threadCache.put(key, value);
            OBJECT_CACHE_MAP.put(threadName, threadCache);
        } else {
            threadCache.put(key, value);
        }
    }

    public void putInCache(final String[] keys, final Object value) {
        final var valueIsNotArray = !value.getClass().isArray();
        if (keys.length == 1 && valueIsNotArray) {
            putInCache(keys[0], value);
            return;
        }
        if (valueIsNotArray) {
            return;
        }
        final var values = (Object[]) value;
        if (keys.length != values.length) {
            return;
        }
        range(0, keys.length).forEach(index -> putInCache(keys[index], values[index]));
    }

    public Map<String, Object> removeAllFromCache() {
        return OBJECT_CACHE_MAP.remove(Thread.currentThread().getName());
    }

}
