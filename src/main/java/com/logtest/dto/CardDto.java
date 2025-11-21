package com.logtest.dto;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @MaskedProperty(pattern = "(\\d{6})\\d+(\\d{4})", replacement = "$1***$2")
    private String cardNumber;

    private String description;

}
