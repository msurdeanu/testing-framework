package ro.mihaisurdeanu.testing.framework.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StatefulSupportService {

  private static final Map<String, Map<String, Object>> OBJECT_CACHE_MAP = new ConcurrentHashMap<>();

  public Object getFromCache(String key) {
    String threadName = Thread.currentThread().getName();

    Map<String, Object> threadCache = OBJECT_CACHE_MAP.get(threadName);
    if (threadCache == null) {
      return null;
    }

    return threadCache.get(key);
  }

  public void putInCache(String key, Object value) {
    String threadName = Thread.currentThread().getName();

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
    String threadName = Thread.currentThread().getName();

    OBJECT_CACHE_MAP.remove(threadName);
  }
}
