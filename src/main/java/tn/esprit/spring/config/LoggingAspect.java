package tn.esprit.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

import java.util.logging.Logger;


@Slf4j
public class LoggingAspect {
    @Before("execution( public * tn.esprit.spring.service.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("Debut methode : " + joinPoint.getSignature().getName() + " : ");
    }
}
