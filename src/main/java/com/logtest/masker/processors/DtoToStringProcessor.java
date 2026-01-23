package com.logtest.masker.processors;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DtoToStringProcessor {

    private static final String FOUR_ASTERISKS = "****";

    public static String maskDates(String toStringResult) {
        Pattern pattern = Pattern.compile("(0000)(-\\d{2}-\\d{2})");
        return pattern.matcher(toStringResult).replaceAll("****$2");
    }

    public static String convertDtoToStringAndMaskDates(Object dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static String processRecursively(Object dto, Map<Object, Object> processed) {
        if (dto == null) {
            return "null";
        } else if (processed.containsKey(dto)) {
            return "[cyclic reference error]";
        } else {
            return processStringToString(dto, processed);
        }
    }

    private static String processStringToString(Object dto, Map<Object, Object> processed) {
        processed.put(dto, dto);

        String fieldsString = Arrays.stream(dto.getClass().getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .map(field -> processField(field, dto, processed))
            .collect(Collectors.joining(", "));

        processed.remove(dto);

        return dto.getClass().getSimpleName() + "(" + fieldsString + ")";
    }

    private static String processField(Field field, Object dto, Map<Object, Object> processed) {
        field.setAccessible(true);
        try {
            return field.getName() + "=" + processFieldValue(field, field.get(dto), processed);
        } catch (IllegalAccessException e) {
            return field.getName() + "=[field access error]";
        }
    }

    private static String processFieldValue(Field field, Object value, Map<Object, Object> processed) {
        if (value == null) {
            return "null";
        } else if (value instanceof Map) {
            return mapToString(field, (Map<?, ?>) value, processed);
        } else if (value instanceof List || value instanceof Set) {
            return listOrSetToString(field, (Collection<?>) value, processed);
        } else if (value.getClass().isArray()) {
            return arrayToString(field, value, processed);
        } else if (value instanceof Temporal && field.getAnnotation(MaskedProperty.class) != null) {
            return processTemporal(value);
        } else if (isCustomObject(value)) {
            return processRecursively(value, processed);
        } else {
            return String.valueOf(value);
        }
    }

    private static String processTemporal(Object temporal) {
        if (temporal instanceof LocalDate date && date.getYear() == 0) {
            return FOUR_ASTERISKS + date.toString().substring(4);
        } else if (temporal instanceof OffsetDateTime dateTime && dateTime.getYear() == 0) {
            return FOUR_ASTERISKS + dateTime.toString().substring(4);
        } else {
            return String.valueOf(temporal);
        }
    }

    private static String listOrSetToString(Field field, Collection<?> collection, Map<Object, Object> processed) {
        return collection.stream()
            .map(item -> elementToString(field, item, processed))
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String arrayToString(Field field, Object array, Map<Object, Object> processed) {
        return IntStream.range(0, Array.getLength(array))
            .mapToObj(i -> elementToString(field, Array.get(array, i), processed))
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String elementToString(Field field, Object element, Map<Object, Object> processed) {
        if (element == null) {
            return "null";
        } else if (element.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(element, processed);
        } else if (element instanceof Temporal && field.getAnnotation(MaskedProperty.class) != null) {
            return processTemporal(element);
        } else {
            return String.valueOf(element);
        }
    }

    private static String mapToString(Field field, Map<?, ?> map, Map<Object, Object> processed) {
        return map.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + processFieldValue(field, entry.getValue(), processed))
            .collect(Collectors.joining(", ", "{", "}"));
    }

    private static boolean isCustomObject(Object obj) {
        return !obj.getClass().isPrimitive() &&
            !obj.getClass().isEnum() &&
            !(obj instanceof String) &&
            !(obj instanceof Number) &&
            !(obj instanceof Boolean) &&
            !(obj instanceof Character) &&
            !(obj instanceof Class) &&
            !obj.getClass().getName().startsWith("java.") &&
            !obj.getClass().getName().startsWith("javax.");
    }

}