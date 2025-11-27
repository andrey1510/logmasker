package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Masked
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstLevelDto {

    @MaskedProperty(type = MaskType.NAME)
    private String name;

    SecondLevelDto secondLevelDto;

    List<SecondLevelDto> secondLevelDtos;

}
