package ro.mihaisurdeanu.testing.framework.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ro.mihaisurdeanu.testing.framework.service.StatefulSupportService;

import java.util.Optional;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class CachingOnTheFly {

    private static final String ID = "id";

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.ReadCache)")
    public Object readCache(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return delegateToCache(proceedingJoinPoint, false);
    }

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.ReadWriteCache)")
    public Object readWriteCache(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return delegateToCache(proceedingJoinPoint, true);
    }

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.WriteCache)")
    public Object writeCache(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (isNotStateful(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        final var optionalId = findParamByName(proceedingJoinPoint);
        if (optionalId.isEmpty()) {
            proceedingJoinPoint.proceed();
        }

        final var object = proceedingJoinPoint.proceed();
        if (object != null) {
            getAsStatefulSupportService(proceedingJoinPoint).putInCache(optionalId.get(), object);
        }

        return object;
    }

    private Object delegateToCache(final ProceedingJoinPoint proceedingJoinPoint, boolean writeToCache) throws Throwable {
        if (isNotStateful(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        final var optionalId = findParamByName(proceedingJoinPoint);
        if (optionalId.isEmpty()) {
            return writeToCache ? writeCache(proceedingJoinPoint) : proceedingJoinPoint.proceed();
        }

        final var object = getAsStatefulSupportService(proceedingJoinPoint).getFromCache(optionalId.get());
        if (object != null && ((MethodSignature) proceedingJoinPoint.getSignature()).getReturnType().isAssignableFrom(object.getClass())) {
            return object;
        }

        return writeToCache ? writeCache(proceedingJoinPoint) : proceedingJoinPoint.proceed();
    }

    private boolean isNotStateful(final ProceedingJoinPoint joinPoint) {
        return !(joinPoint.getThis() instanceof StatefulSupportService);
    }

    private StatefulSupportService getAsStatefulSupportService(final ProceedingJoinPoint proceedingJoinPoint) {
        return (StatefulSupportService) proceedingJoinPoint.getThis();
    }

    private Optional<String> findParamByName(final ProceedingJoinPoint proceedingJoinPoint) {
        final var codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();
        final var parameterNames = codeSignature.getParameterNames();
        final var parameterTypes = codeSignature.getParameterTypes();

        if (parameterNames.length != parameterTypes.length) {
            return Optional.empty();
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if (ID.equals(parameterNames[i]) && String.class.equals(parameterTypes[i])) {
                return Optional.ofNullable((String) proceedingJoinPoint.getArgs()[i]);
            }
        }

        return Optional.empty();
    }

}
