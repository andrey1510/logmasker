package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.processors.CollectionProcessor;
import com.logtest.masker.processors.ValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class Masker {

    private static final String ISMASKED_FIELD_NAME = "isMasked";

    static {
        CollectionProcessor.setMaskFunction(Masker::processRecursively);
        CollectionProcessor.setStringMaskFunction(ValueProcessor::processStringValue);
        CollectionProcessor.setTemporalMaskFunction(ValueProcessor::processTemporalValue);
    }

    public static <T> T mask(T dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static <T> T processRecursively(T dto, Map<Object, Object> processed) {
        if (dto == null) {
            return null;
        } else if (processed.containsKey(dto)) {
            return (T) processed.get(dto);
        } else if (!dto.getClass().isAnnotationPresent(Masked.class)) {
            return dto;
        } else {
            return createDtoMaskedInstance(dto, processed)
                .orElseGet(() -> {
                    log.error("Error during masking: {}", dto.getClass().getSimpleName());
                    return dto;
                });
        }
    }

    private static <T> Optional<T> createDtoMaskedInstance(T source, Map<Object, Object> processed) {
        Optional<T> result;
        try {
            result = Optional.of(((Class<T>) source.getClass()).getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            log.error("Failed to create instance of dto: {}", e.getMessage());
            result = Optional.empty();
        }
        return result
            .map(maskedInstance -> {
                processed.put(source, maskedInstance);
                copyAndMaskFields(source, maskedInstance, processed);
                setMaskedFlag(maskedInstance);
                return maskedInstance;
            });
    }

    private static void copyAndMaskFields(Object source, Object target, Map<Object, Object> processed) {
        ReflectionUtils.doWithFields(
            source.getClass(),
            field -> {
                ReflectionUtils.makeAccessible(field);
                Object maskedValue = processFieldValue(field, ReflectionUtils.getField(field, source), processed);
                ReflectionUtils.setField(field, target, maskedValue);
            },
            field -> !field.isSynthetic()
        );
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

        if (value == null) {
            return null;
        } else if (value instanceof String && maskedProperty != null) {
            return ValueProcessor.processStringValue(maskedProperty.type(), (String) value);
        } else if (value instanceof Temporal && maskedProperty != null) {
            return ValueProcessor.processTemporalValue(maskedProperty.type(), value);
        } else if (value instanceof List) {
            return CollectionProcessor.processList((List<?>) value, field, processed);
        } else if (value instanceof Set) {
            return CollectionProcessor.processSet((Set<?>) value, field, processed);
        } else if (value instanceof Map) {
            return CollectionProcessor.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return CollectionProcessor.processArray(value, field, processed);
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static void setMaskedFlag(Object dto) {
        Field field = ReflectionUtils.findField(dto.getClass(), ISMASKED_FIELD_NAME);
        if (field == null) return;

        Class<?> fieldType = field.getType();
        if (fieldType != boolean.class && fieldType != Boolean.class) return;

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, dto, true);
    }
}