package com.logtest.masker.processors;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.MaskPatternType;
import lombok.Setter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

public class CollectionProcessor {

    @Setter
    private static BiFunction<Object, Map<Object, Object>, Object> collectionMaskFunction;
    @Setter
    private static BiFunction<MaskPatternType, Object, Object> valueMaskFunction;

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
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(),
                        processAnnotatedCollectionElement(e.getValue(), maskedProperty.type())
                    ), HashMap::putAll
                );
        } else {
            return map.entrySet().stream()
                .collect(HashMap::new, (m, e) -> m.put(
                        processMapKey(e.getKey(), processed),
                        processMapValue(e.getValue(), field, processed)
                    ), HashMap::putAll
                );
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
                    ? collectionMaskFunction.apply(item, processed)
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
        } else if (item instanceof String || item instanceof Temporal) {
            return valueMaskFunction != null ? valueMaskFunction.apply(type, item) : item;
        } else {
            return item;
        }
    }

    private static Object processCollectionElement(Object item, Field field, Map<Object, Object> processed) {
        return Optional.ofNullable(item)
            .filter(i -> Optional.ofNullable(findElementType(field, 0))
                .map(type -> type.isAnnotationPresent(Masked.class))
                .orElseGet(() -> i.getClass().isAnnotationPresent(Masked.class)))
            .map(i -> collectionMaskFunction.apply(i, processed))
            .orElse(item);
    }

    private static Object processMapKey(Object key, Map<Object, Object> processed) {
        return Optional.ofNullable(key)
            .filter(k -> k.getClass().isAnnotationPresent(Masked.class))
            .map(k -> collectionMaskFunction.apply(k, processed))
            .orElse(key);
    }

    private static Object processMapValue(Object value, Field field, Map<Object, Object> processed) {
        return Optional.ofNullable(value)
            .filter(v -> Optional.ofNullable(findElementType(field, 1))
                .map(type -> type.isAnnotationPresent(Masked.class))
                .orElseGet(() -> v.getClass().isAnnotationPresent(Masked.class)))
            .map(v -> collectionMaskFunction.apply(v, processed))
            .orElse(value);
    }

    private static Class<?> findElementType(Field field, int index) {
        if (!(field.getGenericType() instanceof ParameterizedType paramType)) return null;

        Type[] typeArgs = paramType.getActualTypeArguments();
        if (typeArgs.length <= index) return null;

        Type type = typeArgs[index];
        return type instanceof Class ? (Class<?>) type : null;
    }
}