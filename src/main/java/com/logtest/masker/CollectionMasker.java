package com.logtest.masker;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class CollectionMasker {

    private static final Set<Class<?>> IMMUTABLE_TYPES = Set.of(
        String.class, Number.class, Boolean.class, Character.class, LocalDate.class
    );

    @Setter
    private static BiFunction<Object, Map<Object, Object>, Object> maskFunction;

    public static Collection<?> processListOrSet(Collection<?> collection, Field field, Map<Object, Object> processed) {
        if (collection.isEmpty()) return createCollectionInstance(collection);

        return collection.stream()
            .map(item -> processListOrSetItem(item, field, processed))
            .collect(Collectors.toCollection(() -> createCollectionInstance(collection)));
    }

    public static Map<?, ?> processMap(Map<?, ?> map, Field field, Map<Object, Object> processed) {
        if (map.isEmpty()) return createMapInstance(map);

        Map<Object, Object> resultMap = createMapInstance(map);

        map.forEach((key, value) -> {
            Object processedKey = processMapKey(key, processed);
            Object processedValue = processMapValue(value, field, processed);
            resultMap.put(processedKey, processedValue);
        });

        return resultMap;
    }

    public static Object processArray(Object array, Map<Object, Object> processed) {
        int length = java.lang.reflect.Array.getLength(array);
        Object newArray = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(array, i);
            Object processedItem = item != null && array.getClass().getComponentType().isAnnotationPresent(Masked.class)
                ? maskFunction.apply(item, processed)
                : item;
            java.lang.reflect.Array.set(newArray, i, processedItem);
        }
        return newArray;
    }

    private static Object processListOrSetItem(Object item, Field field, Map<Object, Object> processed) {
        if (item == null) return null;

        Class<?> itemType = getGenericType(field, 0);
        boolean shouldMask = (itemType != null && itemType.isAnnotationPresent(Masked.class)) ||
            isCustomObject(item);

        return shouldMask ? maskFunction.apply(item, processed) : item;
    }

    private static Object processMapKey(Object key, Map<Object, Object> processed) {
        return (key != null && isCustomObject(key)) ? maskFunction.apply(key, processed) : key;
    }

    private static Object processMapValue(Object value, Field field, Map<Object, Object> processed) {
        if (value == null) return null;

        if (value instanceof String) {
            return Optional.ofNullable(field.getAnnotation(MaskedProperty.class))
                .map(annotation -> ((String) value).replaceAll(annotation.pattern(), annotation.replacement()))
                .orElse((String) value);
        } else {
            Class<?> valueType = getGenericType(field, 1);
            if ((valueType != null && valueType.isAnnotationPresent(Masked.class)) || isCustomObject(value)) {
                return maskFunction.apply(value, processed);
            } else {
                return value;
            }
        }
    }

    private static Collection<Object> createCollectionInstance(Collection<?> original) {
        try {
            return (Collection<Object>) original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.debug("Cannot create instance of collection class {}. Using default implementation.",
                original.getClass().getSimpleName());
            return createDefaultCollection(original);
        }
    }

    private static Map<Object, Object> createMapInstance(Map<?, ?> original) {
        try {
            return (Map<Object, Object>) original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.debug("Cannot create instance of map class {}. Using default implementation.",
                original.getClass().getSimpleName());
            return createDefaultMap(original);
        }
    }

    private static Collection<Object> createDefaultCollection(Collection<?> original) {
        if (original instanceof LinkedHashSet) {
            return new LinkedHashSet<>();
        } else if (original instanceof Set) {
            return new HashSet<>();
        } else if (original instanceof LinkedList) {
            return new LinkedList<>();
        } else if (original instanceof Queue) {
            return new LinkedList<>();
        } else {
            return new ArrayList<>();
        }
    }

    private static Map<Object, Object> createDefaultMap(Map<?, ?> original) {
        return (original instanceof SortedMap) ? new TreeMap<>() : new HashMap<>();
    }

    private static Class<?> getGenericType(Field field, int index) {
        if (field.getGenericType() instanceof ParameterizedType paramType) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            if (typeArguments.length > index && typeArguments[index] instanceof Class) {
                return (Class<?>) typeArguments[index];
            }
        }
        return null;
    }

    private static boolean isCustomObject(Object obj) {
        return !obj.getClass().isPrimitive() &&
            !obj.getClass().isEnum() &&
            IMMUTABLE_TYPES.stream().noneMatch(type -> type.isInstance(obj)) &&
            !obj.getClass().getName().startsWith("java.") &&
            !obj.getClass().getName().startsWith("javax.");
    }
}