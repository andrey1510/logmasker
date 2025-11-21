package com.logtest.masker;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaskingFilterProvider extends SimpleFilterProvider {

    private final MaskingPropertyFilter maskingPropertyFilter;

    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        if ("maskingFilter".equals(filterId)) {
            return maskingPropertyFilter;
        }
        return super.findFilter(filterId);
    }
}
