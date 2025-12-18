package com.logtest.dto;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoForRecursion {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.AUTH_DATA_ALT)
    private String pin;

    private DtoForRecursion dto;

}
