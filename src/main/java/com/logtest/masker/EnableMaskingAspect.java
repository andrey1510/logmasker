package com.logtest.masker;

import com.logtest.masker.annotations.EnableMasking;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class EnableMaskingAspect {

    private final MaskingContext maskingContext;

    @Around("@annotation(enableMasking)")
    public Object aroundAnnotatedMethod(ProceedingJoinPoint joinPoint, EnableMasking enableMasking) throws Throwable {
        return maskingContext.callWithMasking(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Around("@within(enableMasking)")
    public Object aroundAnnotatedClass(ProceedingJoinPoint joinPoint, EnableMasking enableMasking) throws Throwable {
        return maskingContext.callWithMasking(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}

