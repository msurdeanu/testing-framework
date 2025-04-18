package ro.mihaisurdeanu.testing.framework.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ro.mihaisurdeanu.testing.framework.service.StatefulSupportService;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class CachingOnTheFly {

    private static final String ID = "id";

    private static final String IDS = "ids";

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.ReadCache)")
    public Object readCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return delegateToCache(proceedingJoinPoint, false);
    }

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.ReadWriteCache)")
    public Object readWriteCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return delegateToCache(proceedingJoinPoint, true);
    }

    @Around("@annotation(ro.mihaisurdeanu.testing.framework.aop.WriteCache)")
    public Object writeCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (isNotStateful(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        final var optionalId = findParamById(proceedingJoinPoint);
        if (optionalId.isEmpty()) {
            proceedingJoinPoint.proceed();
        }

        final var object = proceedingJoinPoint.proceed();
        if (object != null) {
            getThisAsStatefulSupportService(proceedingJoinPoint).putInCache(optionalId.get(), object);
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    private Object delegateToCache(ProceedingJoinPoint proceedingJoinPoint, boolean writeToCache) throws Throwable {
        if (isNotStateful(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        final var optionalId = findParamById(proceedingJoinPoint);
        if (optionalId.isEmpty()) {
            return writeToCache ? writeCache(proceedingJoinPoint) : proceedingJoinPoint.proceed();
        }

        final var object = getThisAsStatefulSupportService(proceedingJoinPoint).getFromCache(optionalId.get());
        if (object != null && ((MethodSignature) proceedingJoinPoint.getSignature()).getReturnType().isAssignableFrom(object.getClass())) {
            return object;
        }

        return writeToCache ? writeCache(proceedingJoinPoint) : proceedingJoinPoint.proceed();
    }

    private boolean isNotStateful(ProceedingJoinPoint joinPoint) {
        return !(joinPoint.getThis() instanceof StatefulSupportService);
    }

    private StatefulSupportService getThisAsStatefulSupportService(ProceedingJoinPoint proceedingJoinPoint) {
        return (StatefulSupportService) proceedingJoinPoint.getThis();
    }

    private Optional<String[]> findParamById(ProceedingJoinPoint proceedingJoinPoint) {
        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final var parameterNames = methodSignature.getParameterNames();
        final var parameterTypes = methodSignature.getParameterTypes();

        if (parameterNames.length != parameterTypes.length) {
            return empty();
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if (ID.equals(parameterNames[i]) && !parameterTypes[i].isArray() && String.class.equals(parameterTypes[i])) {
                return of(new String[]{(String) proceedingJoinPoint.getArgs()[i]});
            } else if (IDS.equals(parameterNames[i]) && parameterTypes[i].isArray() && String.class.equals(parameterTypes[i].getComponentType())) {
                return of((String[]) proceedingJoinPoint.getArgs()[i]);
            }
        }

        return empty();
    }

}
