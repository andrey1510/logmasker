package com.logtest.dep1.dto.dtoForCollection;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoWithMap {

    private boolean isMasked;

    @MaskedProperty(type = "TEXT_FIELD")
    private String textField;

    private Map<String, CollectionElement> dtos;
}
