package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class CollectionMasker {

    @Setter
    private static BiFunction<Object, Map<Object, Object>, Object> maskFunction;

    public static List<?> processList(List<?> list, Field field, Map<Object, Object> processed) {
        return list.stream()
            .map(item -> processElement(item, field, processed))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Set<?> processSet(Set<?> set, Field field, Map<Object, Object> processed) {
        return set.stream()
            .map(item -> processElement(item, field, processed))
            .collect(Collectors.toCollection(HashSet::new));
    }

    public static Map<?, ?> processMap(Map<?, ?> map, Field field, Map<Object, Object> processed) {
        return map.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> processMapKey(entry.getKey(), processed),
                entry -> processMapValue(entry.getValue(), field, processed),
                (existing, replacement) -> replacement,
                HashMap::new
            ));
    }

    public static Object processArray(Object array, Map<Object, Object> processed) {
        int length = java.lang.reflect.Array.getLength(array);
        Class<?> componentType = array.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(componentType, length);

        IntStream.range(0, length)
            .forEach(i -> {
                Object item = java.lang.reflect.Array.get(array, i);
                Object processedItem = (item != null && componentType.isAnnotationPresent(Masked.class))
                    ? maskFunction.apply(item, processed)
                    : item;
                java.lang.reflect.Array.set(newArray, i, processedItem);
            });

        return newArray;
    }

    private static Object processElement(Object item, Field field, Map<Object, Object> processed) {
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
        if (field.getGenericType() instanceof ParameterizedType paramType) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            if (typeArguments.length > index && typeArguments[index] instanceof Class) {
                return (Class<?>) typeArguments[index];
            }
        }
        return null;
    }

}