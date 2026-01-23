package com.logtest.masker.processors;

import com.logtest.masker.annotations.Masked;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class DtoToStringProcessor {

    public static String maskDates(String toStringResult) {
        Pattern pattern = Pattern.compile("(-0001|0000|0001)(-\\d{2}-\\d{2})");
        return pattern.matcher(toStringResult).replaceAll("****$2");
    }

    //ToDo делать maskDates напрямую в каждом поле
    public static String convertDtoToStringAndMaskDates(Object dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static String processRecursively(Object dto, Map<Object, Object> processed) {
        if (dto == null) {
            return "null";
        } else if (processed.containsKey(dto)) {
            return "[cyclic reference error]";
        }
        processed.put(dto, dto);

        try {
            Class<?> clazz = dto.getClass();

            String fieldsString = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .map(field -> processField(field, dto, processed))
                .collect(Collectors.joining(", "));

            return clazz.getSimpleName() + "(" + fieldsString + ")";

        } finally {
            processed.remove(dto);
        }
    }

    private static String processField(Field field, Object dto, Map<Object, Object> processed) {
        field.setAccessible(true);

        try {
            return field.getName() + "=" + processFieldValue(field.get(dto), processed);

        } catch (IllegalAccessException e) {
            return field.getName() + "=[field access error]";
        }
    }

    private static String processFieldValue(Object value, Map<Object, Object> processed) {
        if (value == null) {
            return "null";
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else if (value instanceof Map) {
            return mapToString((Map<?, ?>) value, processed);
        } else if (value instanceof List || value instanceof Set) {
            return listOrSetToString((Collection<?>) value, processed);
        } else if (value.getClass().isArray()) {
            return arrayToString(value, processed);
        } else {
            return String.valueOf(value);
        }
    }

    private static String listOrSetToString(Collection<?> collection, Map<Object, Object> processed) {
        return collection.stream()
            .map(item -> elementToString(item, processed))
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String arrayToString(Object array, Map<Object, Object> processed) {
        return IntStream.range(0, Array.getLength(array))
            .mapToObj(i -> elementToString(Array.get(array, i), processed))
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String elementToString(Object element, Map<Object, Object> processed) {
        if (element == null) {
            return "null";
        } else if (element.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(element, processed);
        } else {
            return String.valueOf(element);
        }
    }

    private static String mapToString(Map<?, ?> map, Map<Object, Object> processed) {
        return map.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + processFieldValue(entry.getValue(), processed))
            .collect(Collectors.joining(", ", "{", "}"));
    }
}