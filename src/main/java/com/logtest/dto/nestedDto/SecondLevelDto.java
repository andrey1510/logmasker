package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondLevelDto {

    boolean isMasked;

    @MaskedProperty(pattern = "(\\d{6})\\d+(\\d{4})", replacement = "$1***$2")
    private String cardNumber;

    @MaskedProperty(type = MaskType.BALANCE)
    private String balance;

    ThirdLevelDto thirdLevelDto;

    Set<ThirdLevelDto> thirdLevelDtosSet;

    HashMap<String, ThirdLevelDto> thirdLevelDtosMap;

    private String cardDescription; // Won't be masked

}
