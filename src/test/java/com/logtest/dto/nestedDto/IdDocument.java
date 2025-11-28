package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
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

    @MaskedProperty(type = MaskType.OTHER_DUL_SERIES)
    private String otherDulSeries;

    @MaskedProperty(type = MaskType.OTHER_DUL_NUMBER)
    private String otherDulNumber;

    @MaskedProperty(type = MaskType.LOCALDATE_TO_ZERO_DATE)
    private LocalDate someDate;

}
