package com.logtest.masker;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class Masker {

    public static <T> T mask(T dto) {
        return mask(dto, new IdentityHashMap<>());
    }

    @SuppressWarnings("unchecked")
    private static <T> T mask(T dto, Map<Object, Object> processed) {
        if (dto == null) return null;

        if (processed.containsKey(dto)) {
            return (T) processed.get(dto);
        }

        Class<?> dtoClass = dto.getClass();

        if (!dtoClass.isAnnotationPresent(Masked.class)) {
            return dto;
        }

        try {
            T maskedDto = (T) dtoClass.getDeclaredConstructor().newInstance();
            processed.put(dto, maskedDto);

            copyAndMaskFields(dto, maskedDto, dtoClass, processed);
            return maskedDto;

        } catch (Exception e) {
            System.err.println("Error during masking: " + e.getMessage());
            return dto;
        }
    }

    private static <T> void copyAndMaskFields(T source, T target, Class<?> clazz, Map<Object, Object> processed)
        throws IllegalAccessException {

        for (Field field : getAllFields(clazz)) {
            field.setAccessible(true);
            Object originalValue = field.get(source);
            Object maskedValue = processFieldValue(field, originalValue, processed);
            field.set(target, maskedValue);
        }
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {

        if (value == null)
            return null;

        MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);

        if (annotation != null && value instanceof String)
            return applyMasking((String) value, annotation);

        if (value instanceof Collection) {
            return processCollection((Collection<?>) value, field, processed);
        }

        if (value instanceof Map)
            return processMap((Map<?, ?>) value, field, processed);

        if (value.getClass().isArray())
            return processArray(value, processed);

        if (isCustomObject(value))
            return mask(value, processed);

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
            default -> value;
        };
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> processCollection(Collection<?> collection, Field field, Map<Object, Object> processed) {

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
                processedItem = mask(item, processed);
            } else if (isCustomObject(item)) {
                processedItem = mask(item, processed);
            } else {
                processedItem = item;
            }

            resultCollection.add(processedItem);
        }

        return resultCollection;
    }

    @SuppressWarnings("unchecked")
    private static Map<?, ?> processMap(Map<?, ?> map, Field field, Map<Object, Object> processed) {

        if (map.isEmpty())
            return createEmptyMap(map);

        Map<Object, Object> resultMap = (Map<Object, Object>) createEmptyMap(map);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object processedKey = key;
            Object processedValue = value;

            if (key != null && isCustomObject(key))
                processedKey = mask(key, processed);

            if (value != null) {
                Class<?> valueType = getMapValueType(field);
                if (valueType != null && valueType.isAnnotationPresent(Masked.class)) {
                    processedValue = mask(value, processed);
                } else if (isCustomObject(value)) {
                    processedValue = mask(value, processed);
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

    private static Object processArray(Object array, Map<Object, Object> processed) {
        int length = java.lang.reflect.Array.getLength(array);
        Object newArray = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(array, i);
            if (item != null) {
                Class<?> itemType = array.getClass().getComponentType();
                if (itemType.isAnnotationPresent(Masked.class)) {
                    java.lang.reflect.Array.set(newArray, i, mask(item, processed));
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
            return new ArrayList<>();
        }
    }

    private static Map<?, ?> createEmptyMap(Map<?, ?> original) {
        try {
            return original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private static Class<?> getCollectionItemType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                return (Class<?>) typeArguments[0];
            }
        }
        return null;
    }

    private static Class<?> getMapValueType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
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
}