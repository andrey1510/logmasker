package com.logtest.dto;

import com.logtest.masker.MaskType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllPatternDto {

    private boolean isMasked;

    @MaskedProperty(type = MaskType.NAME)
    private String fullname;

    @MaskedProperty(type = MaskType.EMAIL)
    private String email;

    @MaskedProperty(type = MaskType.PHONE)
    private String phoneNumber;

    @MaskedProperty(type = MaskType.TEXT_FIELD)
    private String textField;

    @MaskedProperty(type = MaskType.PAN)
    private String pan;

    @MaskedProperty(type = MaskType.BALANCE)
    private String balance;

    @MaskedProperty(type = MaskType.CONFIDENTIAL_NUMBER)
    private String confidentialNumber;

    @MaskedProperty(type = MaskType.PIN)
    private String pin;

    @MaskedProperty(type = MaskType.OTHER_DUL_SERIES)
    private String otherDulSeries;

    @MaskedProperty(type = MaskType.OTHER_DUL_NUMBER)
    private String otherDulNumber;

    @MaskedProperty(type = MaskType.PASSPORT)
    private String passport;

    @MaskedProperty(type = MaskType.PASSPORT_SERIES)
    private String passportSeries;

    @MaskedProperty(type = MaskType.PASSPORT_NUMBER)
    private String passportNumber;

    @MaskedProperty(type = MaskType.ISSUER_CODE)
    private String issuerCode;

    @MaskedProperty(type = MaskType.ISSUER_NAME)
    private String issuerName;

    @MaskedProperty(type = MaskType.DATE)
    private LocalDate issuanceDate;

    private String issuanceDateMasked;

    private String notForMaskingField;
}
