package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.utils.CollectionProcessor;
import com.logtest.masker.utils.MaskPatterns;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
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
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = source.getClass();

        while (currentClass != null && currentClass != Object.class) {
            Collections.addAll(fields, currentClass.getDeclaredFields());
            currentClass = currentClass.getSuperclass();
        }

        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                field.set(target, processFieldValue(field, field.get(source), processed));
            } catch (IllegalAccessException e) {
                log.error("Failed to access field {}: {}", field.getName(), e.getMessage());
            }
        });
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {
        if (value == null) {
            return null;
        } else if (value instanceof String && field.getAnnotation(MaskedProperty.class) != null) {
            return processStringValue(field, (String) value);
        } else if (value instanceof Temporal && field.getAnnotation(MaskedProperty.class) != null) {
            return processTemporalValue(field, value);
        } else if (value instanceof List) {
            return CollectionProcessor.processList((List<?>) value, field, processed);
        } else if (value instanceof Set) {
            return CollectionProcessor.processSet((Set<?>) value, field, processed);
        } else if (value instanceof Map) {
            return CollectionProcessor.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return CollectionProcessor.processArray(value, processed);
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static Object processTemporalValue(Field field, Object value) {
        return switch (field.getAnnotation(MaskedProperty.class).type()) {
            case LOCAL_DATE -> value instanceof LocalDate date ?
                MaskPatterns.maskLocalDate(date) : value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatterns.maskOffsetDateTime(dateTime) : value;
            default -> value;
        };
    }

    private static String processStringValue(Field field, String value) {
        return switch (field.getAnnotation(MaskedProperty.class).type()) {
            case TEXT_FIELD -> MaskPatterns.maskTextField(value);
            case FULL_NAME -> MaskPatterns.maskFullName(value);
            case FULL_ADDRESS -> MaskPatterns.maskFullAddress(value);
            case EMAIL -> MaskPatterns.maskEmail(value);
            case SURNAME -> MaskPatterns.maskSurname(value);
            case AUTH_DATA -> MaskPatterns.maskAuthData(value);
            case PASSPORT_SERIES_AND_NUMBER -> MaskPatterns.maskPassportSeriesAndNumber(value);
            default -> value;
        };
    }

    @SneakyThrows
    private static void setMaskedFlag(Object dto) {

        Field maskedField = dto.getClass().getDeclaredField(ISMASKED_FIELD_NAME);
        maskedField.setAccessible(true);

        if (maskedField.getType() == boolean.class || maskedField.getType() == Boolean.class)
            maskedField.set(dto, true);
    }
}