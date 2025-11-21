package com.logtest.masker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Slf4j
@Component
public class MaskingContext {

    private static final ThreadLocal<Boolean> NEED_MASK = ThreadLocal.withInitial(() -> false);

    public void enableMasking() {
        NEED_MASK.set(true);
        log.info("Маскировка ВКЛЮЧЕНА");
    }

    public void disableMasking() {
        NEED_MASK.set(false);
        log.info("Маскировка ВЫКЛЮЧЕНА");
    }

    public boolean isNeedMask() {
        return NEED_MASK.get();
    }

    public <T> T callWithMasking(Callable<T> task) {
        boolean originalState = isNeedMask();
        try {
            enableMasking();
            return task.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Masking context execution failed", e);
        } finally {
            if (!originalState) disableMasking();
        }
    }
}