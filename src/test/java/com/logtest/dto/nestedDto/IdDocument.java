package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
