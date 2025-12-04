package com.logtest.dep2.dto;

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
public class NestedDto {

    private boolean isMasked;

    @MaskedProperty(type = "PHONE")
    private String phone;
}
