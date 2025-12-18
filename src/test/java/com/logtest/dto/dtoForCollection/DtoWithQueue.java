package com.logtest.dto.dtoForCollection;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Queue;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoWithQueue {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private String textField;

    private Queue<CollectionDtoElement> dtos;
}
