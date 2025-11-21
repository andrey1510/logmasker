package com.logtest.masker;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaskingPropertyFilter extends SimpleBeanPropertyFilter {

    private final MaskingContext maskingContext;

    @Override
    public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {

        if (maskingContext.isNeedMask()) {
            MaskedProperty maskedProperty = writer.getAnnotation(MaskedProperty.class);

            if (maskedProperty != null) {
                Object value = writer.get(pojo);
                if (value != null) {
                    String maskedValue = value.toString().replaceAll(
                        maskedProperty.pattern(), maskedProperty.replacement());
                    gen.writeStringField(writer.getName(), maskedValue);
                    return;
                } else {
                    gen.writeNullField(writer.getName());
                    return;
                }
            }
        }

        super.serializeAsField(pojo, gen, provider, writer);
    }
}