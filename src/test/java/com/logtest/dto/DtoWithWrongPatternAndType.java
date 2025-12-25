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
public class DtoWithWrongPatternAndType {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD)
    private String correctPatternAndType;

    @MaskedProperty(type = MaskPatternType.EMAIL)
    private String wrongPattern;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD)
    private Integer wrongType;

    private DtoWithWrongPatternAndType nestedDto;

}
