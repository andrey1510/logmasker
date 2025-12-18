package com.logtest.dto.dtoForCollection;

import com.logtest.masker.utils.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
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
public class DtoWithSet {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private String textField;

    private Set<CollectionElement> dtos;
}
