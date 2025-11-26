package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdLevelDto {

    Boolean isMasked;

    @MaskedProperty(type = MaskType.EMAIL)
    private String email;

    @MaskedProperty(type = MaskType.DATE)
    private LocalDate anyDate;

    private String anyDateMasked;
}
