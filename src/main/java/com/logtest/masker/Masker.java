package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.maskers.CollectionMasker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
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
        CollectionMasker.setMaskFunction(Masker::processRecursively);
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
            log.error("Failed to create instance: {}", e.getMessage());
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

        processAllFields(source, target, fields, processed);
    }

    private static void processAllFields(
        Object source, Object target, List<Field> fields, Map<Object, Object> processed
    ) {
        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                field.set(target, processFieldValue(field, field.get(source), processed));
            } catch (IllegalAccessException e) {
                log.warn("Failed to access field {}: {}", field.getName(), e.getMessage());
            }
        });
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {

        if (value == null) {
            return null;
        } else if (value instanceof LocalDate && hasMaskedProperty(field)) {
            return MaskUtils.changeLocalDate((LocalDate) value);
        } else if (value instanceof String) {
            return processStringValue(field, (String) value);
        } else if (value instanceof List) {
            return CollectionMasker.processList((List<?>) value, field, processed);
        } else if (value instanceof Set) {
            return CollectionMasker.processSet((Set<?>) value, field, processed);
        } else if (value instanceof Map) {
            return CollectionMasker.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return CollectionMasker.processArray(value, processed);
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static boolean hasMaskedProperty(Field field) {
        MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
        return annotation != null && annotation.type() == MaskPatternType.LOCALDATE;
    }

    private static String processStringValue(Field field, String value) {
        return Optional.ofNullable(field.getAnnotation(MaskedProperty.class))
            .map(annotation -> switch (annotation.type()) {
                case CUSTOM -> value.replaceAll(annotation.pattern(), annotation.replacement());
                case TEXT_FIELD -> MaskUtils.maskedTextField(value);
                case NAME -> MaskUtils.maskedName(value);
                case EMAIL -> MaskUtils.maskedEmail(value);
                case PHONE -> MaskUtils.maskedPhoneNumber(value);
                case CONFIDENTIAL_NUMBER -> MaskUtils.maskedConfidentialNumber(value);
                case PIN -> MaskUtils.maskedPIN(value);
                case PAN -> MaskUtils.maskedPAN(value);
                case BALANCE -> MaskUtils.maskedBalance(value);
                case PASSPORT_SERIES -> MaskUtils.maskedPassportSeries(value);
                case PASSPORT_NUMBER -> MaskUtils.maskedPassportNumber(value);
                case PASSPORT -> MaskUtils.maskedPassport(value);
                case ISSUER_CODE -> MaskUtils.maskedIssuerCode(value);
                case ISSUER_NAME -> MaskUtils.maskedIssuerName(value);
                case OTHER_DUL_SERIES -> MaskUtils.maskedOtherDulSeries(value);
                case OTHER_DUL_NUMBER -> MaskUtils.maskedOtherDulNumber(value);
                default -> value;
            })
            .orElse(value);
    }

    private static void setMaskedFlag(Object dto) {
        try {
            Field maskedField = dto.getClass().getDeclaredField(ISMASKED_FIELD_NAME);
            maskedField.setAccessible(true);

            if (maskedField.getType() == boolean.class || maskedField.getType() == Boolean.class) {
                maskedField.set(dto, true);
            }
        } catch (NoSuchFieldException e) {
            log.debug("isMasked field not found in class {}", dto.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error setting isMasked flag: {}", e.getMessage());
        }
    }
}