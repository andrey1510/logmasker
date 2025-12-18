package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.utils.CollectionProcessor;
import com.logtest.masker.utils.MaskPatterns;
import com.logtest.masker.utils.MaskPatternsAlt;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
        org.springframework.util.ReflectionUtils.doWithFields(
            source.getClass(),
            field -> {
                org.springframework.util.ReflectionUtils.makeAccessible(field);
                Object value = org.springframework.util.ReflectionUtils.getField(field, source);
                Object maskedValue = processFieldValue(field, value, processed);
                org.springframework.util.ReflectionUtils.setField(field, target, maskedValue);
            },
            field -> !field.isSynthetic()
        );
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
                MaskPatternsAlt.maskLocalDate(date) : value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatternsAlt.maskOffsetDateTime(dateTime) : value;
            default -> value;
        };
    }

    private static String processStringValue(Field field, String value) {
        return switch (field.getAnnotation(MaskedProperty.class).type()) {
            case EMAIL -> MaskPatterns.maskEmail(value);
            case INN -> MaskPatterns.maskInn(value);
            case KPP -> MaskPatterns.maskKpp(value);
            case OKPO -> MaskPatterns.maskOkpo(value);
            case OGRNUL_OR_OGRNIP -> MaskPatterns.maskOgrnUlOrOgrnIp(value);
            case TEXT_FIELD_ALT -> MaskPatternsAlt.maskTextField(value);
            case FULL_NAME_ALT -> MaskPatternsAlt.maskFullName(value);
            case FULL_ADDRESS_ALT -> MaskPatternsAlt.maskFullAddress(value);
            case EMAIL_ALT -> MaskPatternsAlt.maskEmail(value);
            case SURNAME_ALT -> MaskPatternsAlt.maskSurname(value);
            case AUTH_DATA_ALT -> MaskPatternsAlt.maskAuthData(value);
            case PASSPORT_SERIES_AND_NUMBER_ALT -> MaskPatternsAlt.maskPassportSeriesAndNumber(value);
            case JWT_TYK_API_KEY_IP_ADDRESS -> MaskPatterns.maskJwtTykApiKeyIpAddress(value);
            default -> value;
        };
    }

    private static void setMaskedFlag(Object dto) {
        Field field = org.springframework.util.ReflectionUtils.findField(dto.getClass(), ISMASKED_FIELD_NAME);
        if (field == null) return;

        Class<?> fieldType = field.getType();
        if (fieldType != boolean.class && fieldType != Boolean.class) return;

        org.springframework.util.ReflectionUtils.makeAccessible(field);
        org.springframework.util.ReflectionUtils.setField(field, dto, true);
    }
}