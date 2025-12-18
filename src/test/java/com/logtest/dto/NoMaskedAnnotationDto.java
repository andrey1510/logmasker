package com.logtest.dto;

import com.logtest.masker.patterns.MaskPatternType;
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

    @MaskedProperty(type = MaskPatternType.AUTH_DATA_ALT)
    private String pin;
}
