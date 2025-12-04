package com.logtest.dep1.dto;

import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoMaskedAnnotationDto {

    private boolean isMasked;

    @MaskedProperty(type = "AUTH_DATA")
    private String pin;
}
