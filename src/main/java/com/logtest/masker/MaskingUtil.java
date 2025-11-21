package com.logtest.masker;

import com.logtest.masker.annotations.MaskedProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class MaskingUtil {

    private final MaskingContext maskingContext;

    public String maskDto(Object object) {
        if (object == null) return "null";

        if (!maskingContext.isNeedMask()) {
            return object.toString();
        }

        return maskObject(object);
    }

    private String maskObject(Object object) {
        if (object == null) return "null";

        try {
            Class<?> clazz = object.getClass();
            StringBuilder result = new StringBuilder(clazz.getSimpleName()).append("{");

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                String fieldName = field.getName();
                Object fieldValue = field.get(object);

                if (fieldValue != null) {
                    MaskedProperty maskedProperty = field.getAnnotation(MaskedProperty.class);

                    if (maskedProperty != null) {
                        String maskedValue = maskString(fieldValue.toString(),
                            maskedProperty.pattern(), maskedProperty.replacement());
                        result.append(fieldName).append("=").append(maskedValue);
                    } else {
                        result.append(fieldName).append("=").append(fieldValue);
                    }
                } else {
                    result.append(fieldName).append("=null");
                }

                if (i < fields.length - 1) {
                    result.append(", ");
                }
            }

            result.append("}");
            return result.toString();

        } catch (Exception e) {
            return object.toString();
        }
    }

    private String maskString(String value, String pattern, String replacement) {
        return value.replaceAll(pattern, replacement);
    }
}