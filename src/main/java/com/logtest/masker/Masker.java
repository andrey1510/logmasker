package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Slf4j
public class Masker {
    private static final String DATE_FIELD_POSTFIX = "Masked";
    private static final String ISMASKED_FIELD_NAME = "isMasked";
    private static final String DATE_REPLACEMENT = "0000-01-01";
    private static final Set<Class<?>> IMMUTABLE_TYPES = Set.of(
        String.class, Number.class, Boolean.class, Character.class, LocalDate.class
    );

    static {
        CollectionMasker.setMaskFunction(Masker::processRecursively);
    }

    public static <T> T mask(T dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static <T> T processRecursively(T dto, Map<Object, Object> processed) {
        if (dto == null) return null;
        if (processed.containsKey(dto)) return (T) processed.get(dto);
        if (!dto.getClass().isAnnotationPresent(Masked.class)) return dto;

        return createDtoMaskedInstance(dto, processed)
            .orElseGet(() -> {
                log.error("Error during masking: {}", dto.getClass().getSimpleName());
                return dto;
            });
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

        processRegularFields(source, target, fields, processed);
        processDateFields(source, target, fields);
    }

    private static void processRegularFields(
        Object source, Object target, List<Field> fields, Map<Object, Object> processed
    ) {
        fields.stream()
            .filter(field -> !isDateField(field))
            .forEach(field -> {
                field.setAccessible(true);
                try {
                    field.set(target, processFieldValue(field, field.get(source), processed));
                } catch (IllegalAccessException e) {
                    log.warn("Failed to access field {}: {}", field.getName(), e.getMessage());
                }
            });
    }

    private static void processDateFields(Object source, Object target, List<Field> fields) {
        fields.stream()
            .filter(Masker::isDateField)
            .forEach(field -> {
                MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
                if (annotation.type() == MaskType.DATE_COPY) {
                    processDateField(source, target, field);
                } else if (annotation.type() == MaskType.DATE_REPLACE) {
                    processDateReplaceField(source, target, field);
                }
            });
    }

    private static boolean isDateField(Field field) {
        MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
        return annotation != null &&
            (annotation.type() == MaskType.DATE_COPY || annotation.type() == MaskType.DATE_REPLACE)
            && field.getType() == LocalDate.class;
    }

    private static void processDateReplaceField(Object source, Object target, Field dateField) {
        dateField.setAccessible(true);
        try {
            LocalDate nullDate = LocalDate.of(0, 1, 1);
            dateField.set(target, nullDate);
        } catch (IllegalAccessException e) {
            log.warn("Failed to process date replacement field {}: {}", dateField.getName(), e.getMessage());
        }
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {
        if (value == null) return null;

        if (value instanceof String) {
            return processStringValue(field, (String) value);
        } else if (value instanceof Collection) {
            return CollectionMasker.processListOrSet((Collection<?>) value, field, processed);
        } else if (value instanceof Map) {
            return CollectionMasker.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return CollectionMasker.processArray(value, processed);
        } else if (isCustomObject(value)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static String processStringValue(Field field, String value) {
        return Optional.ofNullable(field.getAnnotation(MaskedProperty.class))
            .map(annotation -> {
                if (annotation.type() == MaskType.DATE_REPLACE) {
                    return DATE_REPLACEMENT;
                }
                return applyMasking(value, annotation);
            })
            .orElse(value);
    }

    private static String applyMasking(String value, MaskedProperty annotation) {
        if (annotation.type() == MaskType.CUSTOM)
            return value.replaceAll(annotation.pattern(), annotation.replacement());

        return switch (annotation.type()) {
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
            case DATE_COPY -> value;
            default -> value;
        };
    }

    private static void processDateField(Object source, Object target, Field dateField) {
        dateField.setAccessible(true);
        try {
            dateField.set(target, null);
            setMaskedDateField(target, dateField.getName(), (LocalDate) dateField.get(source));
        } catch (IllegalAccessException e) {
            log.warn("Failed to process date field {}: {}", dateField.getName(), e.getMessage());
        }
    }

    private static void setMaskedDateField(Object target, String fieldName, LocalDate originalDate) {
        findMaskedDateField(target.getClass(), fieldName + DATE_FIELD_POSTFIX)
            .ifPresent(maskedField -> {
                if (maskedField.getType() != String.class) return;
                maskedField.setAccessible(true);
                try {
                    String maskedValue = originalDate != null ? MaskUtils.maskedLocalDate(originalDate) : null;
                    maskedField.set(target, maskedValue);
                } catch (IllegalAccessException e) {
                    log.warn("Failed to set masked date field: {}", e.getMessage());
                }
            });
    }

    private static Optional<Field> findMaskedDateField(Class<?> clazz, String fieldName) {
        Field field = org.springframework.util.ReflectionUtils.findField(clazz, fieldName);
        return Optional.ofNullable(field);
    }

    private static boolean isCustomObject(Object obj) {
        return !obj.getClass().isPrimitive() &&
            !obj.getClass().isEnum() &&
            IMMUTABLE_TYPES.stream().noneMatch(type -> type.isInstance(obj)) &&
            !obj.getClass().getName().startsWith("java.") &&
            !obj.getClass().getName().startsWith("javax.");
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