package com.perm.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect pour centraliser le logging de l'application.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut qui cible tous les services, repositories et controllers
     * dans l'application.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Méthode vide pour le pointcut
    }

    /**
     * Pointcut qui cible tous les beans dans le package principal de l'application.
     */
    @Pointcut("within(com.perm..*)")
    public void applicationPackagePointcut() {
        // Méthode vide pour le pointcut
    }

    /**
     * Récupère le logger associé à la classe jointe.
     * @param joinPoint le point de jointure
     * @return logger
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Aspect qui log les méthodes exécutées avec leurs arguments et résultats.
     * @param joinPoint le point de jointure
     * @return le résultat de la méthode
     * @throws Throwable si une erreur survient
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Entrée: {}.{}() avec arguments = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Sortie: {}.{}() avec résultat = {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Argument illégal: {} dans {}.{}()",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Aspect qui log les exceptions levées.
     * @param joinPoint le point de jointure
     * @param e l'exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger log = logger(joinPoint);
        log.error("Exception dans {}.{}() avec cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL");
        if (log.isDebugEnabled()) {
            log.debug("Exception détaillée:", e);
        }
    }
}
