package com.logtest.dto.nestedDto;

import com.logtest.masker.patterns.MaskPatternType;
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

    @MaskedProperty(type = MaskPatternType.AUTH_DATA_ALT)
    private String dulNumber;

    @MaskedProperty(type = MaskPatternType.LOCAL_DATE)
    private LocalDate someDate;

}
