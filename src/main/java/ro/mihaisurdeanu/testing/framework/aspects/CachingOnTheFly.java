package ro.mihaisurdeanu.testing.framework.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ro.mihaisurdeanu.testing.framework.services.StatefulSupportService;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class CachingOnTheFly {

  private static final String ID = "id";

  @Around("@annotation(ro.mihaisurdeanu.testing.framework.aspects.ReadCache)")
  public Object readCache(final ProceedingJoinPoint joinPoint) throws Throwable {
    return delegateToCache(joinPoint, false);
  }

  @Around("@annotation(ro.mihaisurdeanu.testing.framework.aspects.ReadWriteCache)")
  public Object readWriteCache(final ProceedingJoinPoint joinPoint) throws Throwable {
    return delegateToCache(joinPoint, true);
  }

  @Around("@annotation(ro.mihaisurdeanu.testing.framework.aspects.WriteCache)")
  public Object writeCache(final ProceedingJoinPoint joinPoint) throws Throwable {
    final Optional<StatefulSupportService> optionalStatefulService = getAsStatefulSupportService(joinPoint);
    final Optional<String> optionalId = findParamByName(joinPoint);

    if (optionalStatefulService.isEmpty() || optionalId.isEmpty()) {
      joinPoint.proceed();
    }

    Object object = joinPoint.proceed();
    if (object != null) {
      optionalStatefulService.get().putInCache(optionalId.get(), object);
    }

    return object;
  }

  private Object delegateToCache(final ProceedingJoinPoint joinPoint, boolean writeToCache) throws Throwable {
    final Optional<StatefulSupportService> optionalStatefulService = getAsStatefulSupportService(joinPoint);
    if (optionalStatefulService.isEmpty()) {
      joinPoint.proceed();
    }

    Optional<String> optionalId = findParamByName(joinPoint);
    if (optionalId.isEmpty()) {
      return writeToCache ? writeCache(joinPoint) : joinPoint.proceed();
    }

    Object object = optionalStatefulService.get().getFromCache(optionalId.get());
    if (object != null && object.getClass().equals(((MethodSignature) joinPoint.getSignature()).getReturnType())) {
      return object;
    }

    return writeToCache ? writeCache(joinPoint) : joinPoint.proceed();
  }

  private boolean isNotStateful(final ProceedingJoinPoint joinPoint) {
    return !(joinPoint.getThis() instanceof StatefulSupportService);
  }

  private Optional<StatefulSupportService> getAsStatefulSupportService(final ProceedingJoinPoint joinPoint) {
    if (isNotStateful(joinPoint)) {
      return Optional.empty();
    }

    return Optional.of((StatefulSupportService) joinPoint.getThis());
  }

  @SuppressWarnings({"rawtypes"})
  private Optional<String> findParamByName(final ProceedingJoinPoint joinPoint) {
    final CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
    final String[] parameterNames = codeSignature.getParameterNames();
    final Class[] parameterTypes = codeSignature.getParameterTypes();

    if (parameterNames.length != parameterTypes.length) {
      return Optional.empty();
    }

    for (int i = 0; i < parameterNames.length; i++) {
      if (ID.equals(parameterNames[i]) && String.class.equals(parameterTypes[i])) {
        return Optional.ofNullable((String) joinPoint.getArgs()[i]);
      }
    }

    return Optional.empty();
  }

}
