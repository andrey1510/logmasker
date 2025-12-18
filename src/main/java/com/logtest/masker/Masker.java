package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.utils.NestedDtoCollectionProcessor;
import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.patterns.MaskPatterns;
import com.logtest.masker.patterns.MaskPatternsAlt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public class Masker {

    private static final String ISMASKED_FIELD_NAME = "isMasked";

    static {
        NestedDtoCollectionProcessor.setMaskFunction(Masker::processRecursively);
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
            return processStringValue(maskedProperty.type(), (String) value);
        } else if (value instanceof Temporal && maskedProperty != null) {
            return processTemporalValue(maskedProperty.type(), value);
        } else if (value instanceof List && maskedProperty != null) {
            return processAnnotatedList((List<?>) value, maskedProperty.type());
        } else if (value instanceof Set && maskedProperty != null ) {
            return processAnnotatedSet((Set<?>) value, maskedProperty.type());
        } else if (value instanceof Map && maskedProperty != null) {
            return processAnnotatedMap((Map<?, ?>) value, maskedProperty.type());
        } else if (value.getClass().isArray() && maskedProperty != null) {
            return processAnnotatedArray(value, maskedProperty.type());
        } else if (value instanceof List) {
            return NestedDtoCollectionProcessor.processList((List<?>) value, field, processed);
        } else if (value instanceof Set) {
            return NestedDtoCollectionProcessor.processSet((Set<?>) value, field, processed);
        } else if (value instanceof Map) {
            return NestedDtoCollectionProcessor.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return NestedDtoCollectionProcessor.processArray(value, processed);
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static Object processTemporalValue(MaskPatternType type, Object value) {
        return switch (type) {
            case LOCAL_DATE -> value instanceof LocalDate date ?
                MaskPatternsAlt.maskLocalDate(date) : value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatternsAlt.maskOffsetDateTime(dateTime) : value;
            default -> value;
        };
    }

    private static String processStringValue(MaskPatternType type, String value) {
        return switch (type) {
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
            case SNILS -> MaskPatterns.maskSnils(value);
            case PHONE -> MaskPatterns.maskPhoneNumber(value);
            default -> value;
        };
    }

    private static List<?> processAnnotatedList(List<?> collection, MaskPatternType type) {
        return collection.stream()
            .map(item -> processAnnotatedCollectionElement(item, type))
            .toList();
    }

    private static Set<?> processAnnotatedSet(Set<?> collection, MaskPatternType type) {
        return collection.stream()
            .map(item -> processAnnotatedCollectionElement(item, type))
            .collect(Collectors.toCollection(() -> Collections.newSetFromMap(new IdentityHashMap<>())));
    }

    private static Map<?, ?> processAnnotatedMap(Map<?, ?> map, MaskPatternType type) {
        return map.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> processAnnotatedCollectionElement(entry.getValue(), type),
                (existing, replacement) -> replacement,
                java.util.HashMap::new
            ));
    }

    private static Object processAnnotatedArray(Object array, MaskPatternType type) {
        int length = Array.getLength(array);
        Object newArray = Array.newInstance(array.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            Object processedItem = processAnnotatedCollectionElement(item, type);
            Array.set(newArray, i, processedItem);
        }

        return newArray;
    }

    private static Object processAnnotatedCollectionElement(Object item, MaskPatternType type) {
        if (item == null) {
            return null;
        } else if (item instanceof String stringValue) {
            return processStringValue(type, stringValue);
        } else if (item instanceof Temporal temporalValue) {
            return processTemporalValue(type, temporalValue);
        } else {
            return item;
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