package com.logtest.dto;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.utils.MaskPatternType;
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

    @MaskedProperty(type = MaskPatternType.EMAIL_ALT)
    private Object email;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private Object textField;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private Object textFieldMap;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private Object textFieldMapDto;
}
