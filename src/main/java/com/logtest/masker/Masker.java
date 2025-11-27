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

import static org.springframework.util.ReflectionUtils.findField;

@Slf4j
public class Masker {

    private static final String DATE_FIELD_POSTFIX = "Masked";
    private static final String ISMASKED_FIELD_NAME = "isMasked";

    static {
        CollectionMasker.setMaskFunction(Masker::processRecursively);
    }

    public static <T> T mask(T dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static <T> T processRecursively(T dto, Map<Object, Object> processed) {

        if (dto == null)
            return null;

        if (processed.containsKey(dto))
            return (T) processed.get(dto);

        if (!dto.getClass().isAnnotationPresent(Masked.class))
            return dto;

        try {
            T maskedDto = (T) dto.getClass().getDeclaredConstructor().newInstance();
            processed.put(dto, maskedDto);

            copyAndMaskFields(dto, maskedDto, dto.getClass(), processed);
            setMaskedFlag(maskedDto);

            return maskedDto;

        } catch (Exception e) {
            log.error("Error during masking: {}", e.getMessage());
            return dto;
        }
    }

    private static <T> void copyAndMaskFields(T source, T target, Class<?> dtoClass, Map<Object, Object> processed)
        throws IllegalAccessException {

        List<Field> allFields = getAllFields(dtoClass);

        for (Field field : allFields) {
            field.setAccessible(true);

            MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
            if (annotation != null && annotation.type() == MaskType.DATE && field.getType() == LocalDate.class)
                continue;

            field.set(target, processFieldValue(field, field.get(source), processed));
        }

        for (Field field : allFields) {
            MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
            if (annotation != null && annotation.type() == MaskType.DATE && field.getType() == LocalDate.class)
                processDateField(source, target, field);
        }
    }

    private static void processDateField(Object source, Object target, Field dateField) throws IllegalAccessException {

        dateField.setAccessible(true);
        LocalDate originalDate = (LocalDate) dateField.get(source);

        dateField.set(target, null);

        Field maskedField = findField(target.getClass(), dateField.getName() + DATE_FIELD_POSTFIX);

        if (maskedField != null && maskedField.getType() == String.class) {
            maskedField.setAccessible(true);
            if (originalDate != null) {
                String maskedDate = MaskUtils.maskedLocalDate(originalDate);
                maskedField.set(target, maskedDate);
            } else {
                maskedField.set(target, null);
            }
        } else {
            log.warn("Pair masked field not found in Dto class {} for date field {}",
                target.getClass().getSimpleName(), dateField.getName());
        }
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {

        if (value == null)
            return null;

        MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);

        if (annotation != null && annotation.type() == MaskType.DATE && value instanceof LocalDate)
            return value;

        if (annotation != null && value instanceof String)
            return applyMasking((String) value, annotation);

        if (value instanceof Collection)
            return CollectionMasker.processCollection((Collection<?>) value, field, processed);

        if (value instanceof Map)
            return CollectionMasker.processMap((Map<?, ?>) value, field, processed);

        if (value.getClass().isArray())
            return CollectionMasker.processArray(value, processed);

        if (isCustomObject(value))
            return processRecursively(value, processed);

        return value;
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
            case DATE -> value;
            default -> value;
        };
    }

    private static boolean isCustomObject(Object obj) {
        return !obj.getClass().isPrimitive() &&
            !obj.getClass().isEnum() &&
            !(obj instanceof String) &&
            !(obj instanceof Number) &&
            !(obj instanceof Boolean) &&
            !(obj instanceof Character) &&
            !(obj instanceof LocalDate) &&
            !obj.getClass().getName().startsWith("java.") &&
            !obj.getClass().getName().startsWith("javax.");
    }

    private static List<Field> getAllFields(Class<?> dtoClass) {

        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = dtoClass;

        while (currentClass != null && currentClass != Object.class) {
            Collections.addAll(fields, currentClass.getDeclaredFields());
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    private static void setMaskedFlag(Object dto) {

        try {
            Field maskedField = dto.getClass().getDeclaredField(ISMASKED_FIELD_NAME);

            if (maskedField.getType() == boolean.class || maskedField.getType() == Boolean.class) {
                maskedField.setAccessible(true);
                maskedField.set(dto, true);
            }
        } catch (NoSuchFieldException e) {
            log.warn("isMasked field not found in Dto class {}: {}", dto.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            log.error("Error setting isMasked flag in Dto class {}: {}", dto.getClass().getSimpleName(), e.getMessage());
        }
    }
}