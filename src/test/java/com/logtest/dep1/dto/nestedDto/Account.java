package com.logtest.dep1.dto.nestedDto;

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
public class Account {

    private boolean isMasked;

    @MaskedProperty(type = "SURNAME")
    private String surname;

}
