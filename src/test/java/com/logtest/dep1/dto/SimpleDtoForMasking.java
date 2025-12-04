package com.logtest.dep1.dto;

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
public class SimpleDtoForMasking {

    private boolean isMasked;

    @MaskedProperty(type = "TEXT_FIELD")
    private String phoneNumber;
}
