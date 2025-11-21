package com.logtest.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonFilter("maskingFilter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @MaskedProperty(pattern = "(\\d{3})\\d{10}(\\d{3})", replacement = "$1**********$2")
    private String cardNumber;

    private String description;

}
