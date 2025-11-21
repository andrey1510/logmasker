package com.logtest.masker;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

@Slf4j
public class MaskingSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        MaskingContext maskingContext = getMaskingContext();

        if (maskingContext == null || !maskingContext.isNeedMask()) {
            provider.defaultSerializeValue(value, gen);
            return;
        }

        gen.writeStartObject();

        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(value);
                String fieldName = field.getName();

                MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

                if (fieldValue != null && maskedProperty != null) {
                    String masked = fieldValue.toString().replaceAll(
                        maskedProperty.pattern(), maskedProperty.replacement());
                    gen.writeStringField(fieldName, masked);
                } else {
                    gen.writeFieldName(fieldName);
                    provider.defaultSerializeValue(fieldValue, gen);
                }
            } catch (Exception e) {
                log.warn("Ошибка при обработке поля {}: {}", field.getName(), e.getMessage());
            }
        }

        gen.writeEndObject();
    }

    private MaskingContext getMaskingContext() {
        try {
            return SpringContextHolder.getApplicationContext().getBean(MaskingContext.class);
        } catch (Exception e) {
            return null;
        }
    }
}