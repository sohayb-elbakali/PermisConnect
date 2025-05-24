package com.perm.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect pour mesurer les performances des méthodes.
 */
@Aspect
@Component
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger("com.perm.aop.logging.performance");

    /**
     * Pointcut qui cible tous les controllers REST.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
        // Méthode vide pour le pointcut
    }

    /**
     * Pointcut qui cible tous les services.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
        // Méthode vide pour le pointcut
    }

    /**
     * Pointcut qui cible tous les repositories.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryPointcut() {
        // Méthode vide pour le pointcut
    }

    /**
     * Mesure le temps d'exécution des controllers.
     */
    @Around("controllerPointcut()")
    public Object measureControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint, "CONTROLLER");
    }

    /**
     * Mesure le temps d'exécution des services.
     */
    @Around("servicePointcut()")
    public Object measureServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint, "SERVICE");
    }

    /**
     * Mesure le temps d'exécution des repositories.
     */
    @Around("repositoryPointcut()")
    public Object measureRepositoryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint, "REPOSITORY");
    }

    /**
     * Méthode commune pour mesurer le temps d'exécution.
     */
    private Object measureExecutionTime(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[PERFORMANCE] [{}] {}.{}() exécuté en {} ms",
                type, className, methodName, executionTime);

        // Log une alerte si l'exécution a pris trop de temps
        if (executionTime > 1000) {
            log.warn("[PERFORMANCE] [{}] {}.{}() a pris trop de temps: {} ms",
                    type, className, methodName, executionTime);
        }

        return result;
    }
}
