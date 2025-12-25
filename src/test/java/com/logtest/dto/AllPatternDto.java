package com.logtest.dto;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllPatternDto {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.LOCAL_DATE)
    private LocalDate someDate;

    @MaskedProperty(type = MaskPatternType.OFFSET_DATE_TIME)
    private OffsetDateTime dateTime;

    @MaskedProperty(type = MaskPatternType.INN)
    private String inn;

    private String notForMaskingField;
}
