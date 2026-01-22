package com.logtest.masker.processors;

import com.logtest.masker.annotations.Masked;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public class DtoToStringProcessor {


    public static String maskDates(String toStringResult) {
        Pattern pattern = Pattern.compile("(-0001|0000|0001)(-\\d{2}-\\d{2})");
        return pattern.matcher(toStringResult).replaceAll("****$2");
    }

    public static String convertDtoToString(Object obj) {
        return convertDtoToString(obj, new IdentityHashMap<>());
    }

    private static String convertDtoToString(Object obj, Map<Object, Object> visited) {
        if (obj == null) return "null";

        if (visited.containsKey(obj)) {
            return "cyclic reference";
        }
        visited.put(obj, obj);

        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();

        sb.append(clazz.getSimpleName()).append("(");

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                if (field.isSynthetic()) continue;

                sb.append(field.getName()).append("=");

                Object value = field.get(obj);

                if (value == null) {
                    sb.append("null");
                } else if (value.getClass().isAnnotationPresent(Masked.class)) {
                    sb.append(convertDtoToString(value, visited));
                } else if (value instanceof List) {
                    sb.append(listToString((List<?>) value, visited));
                } else if (value instanceof Set) {
                    sb.append(setToString((Set<?>) value, visited));
                } else if (value instanceof Map) {
                    sb.append(mapToString((Map<?, ?>) value, visited));
                } else if (value.getClass().isArray()) {
                    sb.append(arrayToString(value, visited));
                } else {
                    sb.append(value);
                }

                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
        } catch (IllegalAccessException e) {
            log.warn("Cannot access field for toString: {}", e.getMessage());
            sb.append("...");
        }

        sb.append(")");
        visited.remove(obj);
        return sb.toString();
    }

    private static String listToString(List<?> list, Map<Object, Object> visited) {
        if (list == null) return "null";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            if (item != null && item.getClass().isAnnotationPresent(Masked.class)) {
                sb.append(convertDtoToString(item, visited));
            } else {
                sb.append(item);
            }

            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String setToString(Set<?> set, Map<Object, Object> visited) {
        if (set == null) return "null";

        StringBuilder sb = new StringBuilder("[");
        int i = 0;
        for (Object item : set) {
            if (item != null && item.getClass().isAnnotationPresent(Masked.class)) {
                sb.append(convertDtoToString(item, visited));
            } else {
                sb.append(item);
            }

            if (++i < set.size()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String mapToString(Map<?, ?> map, Map<Object, Object> visited) {
        if (map == null) return "null";

        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=");

            Object value = entry.getValue();
            if (value != null && value.getClass().isAnnotationPresent(Masked.class)) {
                sb.append(convertDtoToString(value, visited));
            } else {
                sb.append(value);
            }

            if (++i < map.size()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String arrayToString(Object array, Map<Object, Object> visited) {
        if (array == null) return "null";

        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            if (item != null && item.getClass().isAnnotationPresent(Masked.class)) {
                sb.append(convertDtoToString(item, visited));
            } else {
                sb.append(item);
            }

            if (i < length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}