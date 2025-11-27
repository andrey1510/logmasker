package com.logtest.dto.patternTypes;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.MaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @MaskedProperty(type = MaskType.DATE)
    private LocalDate someDate;

    private String someDateMasked;

    @MaskedProperty(type = MaskType.DATE)
    private LocalDate otherDate;

    private String otherDateMasked;

    @MaskedProperty(type = MaskType.DATE)
    private LocalDate anotherDate;

    private String annnnnotherDateMasked;

}
