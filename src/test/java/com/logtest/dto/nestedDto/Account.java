package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private boolean isMasked;

    @MaskedProperty(type = MaskType.PAN)
    private String pan;

    @MaskedProperty(type = MaskType.BALANCE)
    private String balance;

    Set<Credentials> credentials;

}
