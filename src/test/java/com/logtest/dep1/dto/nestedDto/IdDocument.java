package com.logtest.dep1.dto.nestedDto;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDocument {

    private boolean isMasked;

    @MaskedProperty(type = "AUTH_DATA")
    private String dulNumber;

    @MaskedProperty(type = "LOCAL_DATE")
    private LocalDate someDate;

}
