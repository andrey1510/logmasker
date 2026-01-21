package com.logtest.masker;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.processors.CollectionProcessor;
import com.logtest.masker.processors.ValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

@Slf4j
public class Masker {

    private static final String IS_MASKED_FIELD = "isMasked";

    static {
        CollectionProcessor.setCollectionMaskFunction(Masker::processRecursively);
        CollectionProcessor.setValueMaskFunction(ValueProcessor::processValue);
    }

    public static String maskDtoToString(Object dto) {
        try {
            Object maskedDto = mask(dto);

            ToStringStyle style = new StandardToStringStyle() {
                {
                    this.setUseShortClassName(true);
                    this.setUseIdentityHashCode(false);
                    this.setContentStart("(");
                    this.setContentEnd(")");
                    this.setFieldSeparator(", ");
                    this.setFieldSeparatorAtStart(false);
                    this.setFieldSeparatorAtEnd(false);
                }

                @Override
                public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
                    if (value instanceof LocalDate date) {
                        super.append(buffer, fieldName,
                            String.format("%02d.%02d.%04d",
                                date.getDayOfMonth(),
                                date.getMonthValue(),
                                date.getYear()),
                            fullDetail);
                    } else if (value instanceof OffsetDateTime dateTime) {
                        super.append(buffer, fieldName,
                            String.format("%02d.%02d.%04d",
                                dateTime.getDayOfMonth(),
                                dateTime.getMonthValue(),
                                dateTime.getYear()),
                            fullDetail);
                    } else {
                        super.append(buffer, fieldName, value, fullDetail);
                    }


                   // super.append(buffer, fieldName, value, fullDetail);
                }
            };

            String maskedDtoString = ReflectionToStringBuilder.toString(maskedDto, style);

            return replaceYearsWithAsterisks(maskedDtoString);

        } catch (Exception e) {
            log.error("Error during DTO masking to string: {}", e.getMessage(), e);
            return "ERROR_MASKING_DTO";
        }
    }

    private static String replaceYearsWithAsterisks(String toStringResult) {
        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.)(0001|0002)");
        return pattern.matcher(toStringResult).replaceAll("$1****");
    }

    public static <T> T mask(T dto) {
        return processRecursively(dto, new IdentityHashMap<>());
    }

    private static <T> T processRecursively(T dto, Map<Object, Object> processed) {
        if (dto == null) {
            return null;
        } else if (processed.containsKey(dto)) {
            return (T) processed.get(dto);
        } else if (!dto.getClass().isAnnotationPresent(Masked.class)) {
            return dto;
        } else {
            return createDtoMaskedInstance(dto, processed)
                .orElseGet(() -> {
                    log.error("Error during masking: {}", dto.getClass().getSimpleName());
                    return dto;
                });
        }
    }

    private static <T> Optional<T> createDtoMaskedInstance(T source, Map<Object, Object> processed) {
        Optional<T> result;
        try {
            result = Optional.of(((Class<T>) source.getClass()).getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            log.error("Failed to create instance of dto: {}", e.getMessage());
            result = Optional.empty();
        }
        return result
            .map(maskedInstance -> {
                processed.put(source, maskedInstance);
                copyAndMaskFields(source, maskedInstance, processed);
                setMaskedFlag(maskedInstance);
                return maskedInstance;
            });
    }

    private static void copyAndMaskFields(Object source, Object target, Map<Object, Object> processed) {
        doWithFields(
            source.getClass(),
            field -> {
                makeAccessible(field);
                Object maskedValue = processFieldValue(field, getField(field, source), processed);
                setField(field, target, maskedValue);
            },
            field -> !field.isSynthetic()
        );
    }

    private static Object processFieldValue(Field field, Object value, Map<Object, Object> processed) {
        MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

        if (value == null) {
            return null;
        } else if ((value instanceof Temporal || value instanceof String) && maskedProperty != null) {
            return ValueProcessor.processValue(maskedProperty.type(), value);
        } else if (value instanceof List) {
            return CollectionProcessor.processList((List<?>) value, field, processed);
        } else if (value instanceof Set) {
            return CollectionProcessor.processSet((Set<?>) value, field, processed);
        } else if (value instanceof Map) {
            return CollectionProcessor.processMap((Map<?, ?>) value, field, processed);
        } else if (value.getClass().isArray()) {
            return CollectionProcessor.processArray(value, field, processed);
        } else if (value.getClass().isAnnotationPresent(Masked.class)) {
            return processRecursively(value, processed);
        } else {
            return value;
        }
    }

    private static void setMaskedFlag(Object dto) {
        Field field = findField(dto.getClass(), IS_MASKED_FIELD);
        if (field == null) return;

        Class<?> fieldType = field.getType();
        if (fieldType != boolean.class && fieldType != Boolean.class) return;

        makeAccessible(field);
        setField(field, dto, true);
    }
}