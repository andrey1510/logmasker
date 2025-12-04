package com.logtest.dep1.dto;

import com.logtest.masker.annotations.Masked;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoFieldsForMaskingDto {

    private boolean isMasked;

    private String someText;
}
