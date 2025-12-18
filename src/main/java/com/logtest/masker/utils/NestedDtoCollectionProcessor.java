package com.logtest.masker.utils;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.MaskPatternType;
import lombok.Setter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NestedDtoCollectionProcessor {

    @Setter
    private static BiFunction<Object, Map<Object, Object>, Object> maskFunction;
    @Setter
    private static BiFunction<MaskPatternType, String, String> stringMaskFunction;
    @Setter
    private static BiFunction<MaskPatternType, Temporal, Temporal> temporalMaskFunction;

    public static List<?> processList(List<?> list, Field field, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

        if (maskedProperty != null) {
            return list.stream()
                .map(item -> processAnnotatedCollectionElement(item, maskedProperty.type()))
                .toList();
        } else {
            return list.stream()
                .map(item -> processCollectionElement(item, field, processed))
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static Set<?> processSet(Set<?> set, Field field, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

        if (maskedProperty != null) {
            return set.stream()
                .map(item -> processAnnotatedCollectionElement(item, maskedProperty.type()))
                .collect(Collectors.toCollection(() -> Collections.newSetFromMap(new IdentityHashMap<>())));
        } else {
            return set.stream()
                .map(item -> processCollectionElement(item, field, processed))
                .collect(Collectors.toCollection(() -> Collections.newSetFromMap(new IdentityHashMap<>())));
        }
    }

    public static Map<?, ?> processMap(Map<?, ?> map, Field field, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

        if (maskedProperty != null) {
            return map.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> processAnnotatedCollectionElement(entry.getValue(), maskedProperty.type()),
                    (existing, replacement) -> replacement,
                    HashMap::new
                ));
        } else {
            return map.entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> processMapKey(entry.getKey(), processed),
                    entry -> processMapValue(entry.getValue(), field, processed),
                    (existing, replacement) -> replacement,
                    HashMap::new
                ));
        }
    }

    public static Object processArray(Object array, Field field, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field != null ? field.getAnnotation(MaskedProperty.class) : null;

        if (maskedProperty != null) {
            return processAnnotatedArray(array, maskedProperty.type());
        } else {
            return processDtoArray(array, processed);
        }
    }

    private static Object processDtoArray(Object array, Map<Object, Object> processed) {
        int length = Array.getLength(array);
        Class<?> componentType = array.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, length);

        IntStream.range(0, length)
            .forEach(i -> {
                Object item = Array.get(array, i);
                Object processedItem = (item != null && componentType.isAnnotationPresent(Masked.class))
                    ? maskFunction.apply(item, processed)
                    : item;
                Array.set(newArray, i, processedItem);
            });

        return newArray;
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
            return stringMaskFunction != null ? stringMaskFunction.apply(type, stringValue) : item;
        } else if (item instanceof Temporal temporalValue) {
            return temporalMaskFunction != null ? temporalMaskFunction.apply(type, temporalValue) : item;
        } else {
            return item;
        }
    }

    private static Object processCollectionElement(Object item, Field field, Map<Object, Object> processed) {
        return Optional.ofNullable(item)
            .filter(i -> Optional.ofNullable(findElementType(field, 0))
                .map(type -> type.isAnnotationPresent(Masked.class))
                .orElseGet(() -> i.getClass().isAnnotationPresent(Masked.class)))
            .map(i -> maskFunction.apply(i, processed))
            .orElse(item);
    }

    private static Object processMapKey(Object key, Map<Object, Object> processed) {
        return Optional.ofNullable(key)
            .filter(k -> k.getClass().isAnnotationPresent(Masked.class))
            .map(k -> maskFunction.apply(k, processed))
            .orElse(key);
    }

    private static Object processMapValue(Object value, Field field, Map<Object, Object> processed) {
        return Optional.ofNullable(value)
            .filter(v -> Optional.ofNullable(findElementType(field, 1))
                .map(type -> type.isAnnotationPresent(Masked.class))
                .orElseGet(() -> v.getClass().isAnnotationPresent(Masked.class)))
            .map(v -> maskFunction.apply(v, processed))
            .orElse(value);
    }

    private static Class<?> findElementType(Field field, int index) {
        return field.getGenericType() instanceof ParameterizedType paramType
            && paramType.getActualTypeArguments().length > index
            && paramType.getActualTypeArguments()[index] instanceof Class<?> clazz
            ? clazz
            : null;
    }
}