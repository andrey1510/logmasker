package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdLevelDto {

    @MaskedProperty(type = MaskType.EMAIL)
    private String email;

}
