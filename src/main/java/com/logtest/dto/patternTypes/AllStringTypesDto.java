package com.logtest.dto.patternTypes;

import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import com.logtest.masker.MaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllStringTypesDto {

    @MaskedProperty(pattern = "(\\d{3})\\d+(\\d{2})", replacement = "$1***$2")
    private String custom;

    @MaskedProperty(type = MaskType.TEXT_FIELD)
    private String textField;

    @MaskedProperty(type = MaskType.NAME)
    private String name;

    @MaskedProperty(type = MaskType.EMAIL)
    private String email;

    @MaskedProperty(type = MaskType.PHONE)
    private String phone;

    private String description; // Won't be masked

}
