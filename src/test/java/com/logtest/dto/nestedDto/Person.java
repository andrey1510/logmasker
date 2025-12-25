package com.logtest.dto.nestedDto;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.EMAIL)
    private String email;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD)
    private String textField;

    private Passport passport;

    private List<Account> accounts;

    private Map<String, IdDocument> idDocuments;

}




