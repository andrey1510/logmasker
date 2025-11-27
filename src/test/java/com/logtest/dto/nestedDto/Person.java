package com.logtest.dto.nestedDto;

import com.logtest.masker.MaskType;
import com.logtest.masker.Masked;
import com.logtest.masker.MaskedProperty;
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

    @MaskedProperty(type = MaskType.NAME)
    private String fullname;

    @MaskedProperty(type = MaskType.EMAIL)
    private String email;

    @MaskedProperty(type = MaskType.PHONE)
    private String phoneNumber;

    @MaskedProperty(type = MaskType.TEXT_FIELD)
    private String textField;

    private Passport passport;

    List<Account> accounts;

    Map<String, IdDocument> idDocuments;

}




