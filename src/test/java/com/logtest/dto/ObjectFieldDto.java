package com.logtest.dto;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.MaskPatternType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectFieldDto {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.EMAIL)
    private Object email;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD)
    private Object textField;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD)
    private Object textFieldMap;

    private Object textFieldMapDto;
}
