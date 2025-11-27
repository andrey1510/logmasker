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
public class Credentials {

    private boolean isMasked;

    @MaskedProperty(type = MaskType.CONFIDENTIAL_NUMBER)
    private String confidentialNumber;

    @MaskedProperty(type = MaskType.PIN)
    private String pin;

}
