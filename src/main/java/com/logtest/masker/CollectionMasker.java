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
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;

@Slf4j
public class CollectionMasker {

    @Setter
    private static BiFunction<Object, Map<Object, Object>, Object> maskFunction;

    public static Collection<?> processCollection(Collection<?> collection, Field field, Map<Object, Object> processed) {

        if (collection.isEmpty())
            return createEmptyCollection(collection);

        Collection<Object> resultCollection = (Collection<Object>) createEmptyCollection(collection);

        for (Object item : collection) {
            if (item == null) {
                resultCollection.add(null);
                continue;
            }

            Object processedItem;

            Class<?> itemType = getCollectionItemType(field);
            if (itemType != null && itemType.isAnnotationPresent(Masked.class)) {
                processedItem = maskFunction.apply(item, processed);
            } else if (isCustomObject(item)) {
                processedItem = maskFunction.apply(item, processed);
            } else {
                processedItem = item;
            }

            resultCollection.add(processedItem);
        }

        return resultCollection;
    }

    public static Map<?, ?> processMap(Map<?, ?> map, Field field, Map<Object, Object> processed) {

        if (map.isEmpty())
            return createEmptyMap(map);

        Map<Object, Object> resultMap = (Map<Object, Object>) createEmptyMap(map);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object processedKey = key;
            Object processedValue = value;

            if (key != null && isCustomObject(key))
                processedKey = maskFunction.apply(key, processed);

            if (value != null) {
                Class<?> valueType = getMapValueType(field);
                if (valueType != null && valueType.isAnnotationPresent(Masked.class)) {
                    processedValue = maskFunction.apply(value, processed);
                } else if (isCustomObject(value)) {
                    processedValue = maskFunction.apply(value, processed);
                }
                else if (value instanceof String) {
                    MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
                    if (annotation != null) {
                        processedValue = ((String) value).replaceAll(annotation.pattern(), annotation.replacement());
                    }
                }
            }

            resultMap.put(processedKey, processedValue);
        }

        return resultMap;
    }

    public static Object processArray(Object array, Map<Object, Object> processed) {

        int length = java.lang.reflect.Array.getLength(array);
        Object newArray = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(array, i);
            if (item != null) {
                Class<?> itemType = array.getClass().getComponentType();
                if (itemType.isAnnotationPresent(Masked.class)) {
                    java.lang.reflect.Array.set(newArray, i, maskFunction.apply(item, processed));
                } else {
                    java.lang.reflect.Array.set(newArray, i, item);
                }
            }
        }
        return newArray;
    }

    private static Collection<?> createEmptyCollection(Collection<?> original) {

        try {
            return original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.info("Cannot create instance of collection class {}. Using default implementation.",
                original.getClass().getSimpleName());

            if (original instanceof Set) {
                if (original instanceof SortedSet) {
                    return new TreeSet<>(((SortedSet<?>) original).comparator());
                } else if (original instanceof LinkedHashSet) {
                    return new LinkedHashSet<>();
                } else {
                    return new HashSet<>();
                }
            } else if (original instanceof List) {
                if (original instanceof LinkedList) {
                    return new LinkedList<>();
                } else {
                    return new ArrayList<>();
                }
            } else if (original instanceof Queue) {
                if (original instanceof PriorityQueue) {
                    return new PriorityQueue<>(((PriorityQueue<?>) original).comparator());
                } else if (original instanceof Deque) {
                    return new LinkedList<>();
                } else {
                    return new LinkedList<>();
                }
            } else {
                return new ArrayList<>();
            }
        }
    }

    private static Map<?, ?> createEmptyMap(Map<?, ?> original) {

        try {
            return original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (original instanceof SortedMap) {
                return new TreeMap<>();
            } else {
                return new HashMap<>();
            }
        }
    }

    private static Class<?> getCollectionItemType(Field field) {

        if (field.getGenericType() instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                return (Class<?>) typeArguments[0];
            }
        }
        return null;
    }

    private static Class<?> getMapValueType(Field field) {

        if (field.getGenericType() instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            if (typeArguments.length > 1 && typeArguments[1] instanceof Class) {
                return (Class<?>) typeArguments[1];
            }
        }
        return null;
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

}
