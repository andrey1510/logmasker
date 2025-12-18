package com.logtest.dto;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllPatternDto {

    private boolean isMasked;

    @MaskedProperty(type = MaskPatternType.FULL_NAME_ALT)
    private String fullname;

    @MaskedProperty(type = MaskPatternType.EMAIL_ALT)
    private String email;

    @MaskedProperty(type = MaskPatternType.TEXT_FIELD_ALT)
    private String textField;

    @MaskedProperty(type = MaskPatternType.AUTH_DATA_ALT)
    private String pan;

    @MaskedProperty(type = MaskPatternType.SURNAME_ALT)
    private String surname;

    @MaskedProperty(type = MaskPatternType.FULL_ADDRESS_ALT)
    private String fullAddress;

    @MaskedProperty(type = MaskPatternType.PASSPORT_SERIES_AND_NUMBER_ALT)
    private String passportSeries;

    @MaskedProperty(type = MaskPatternType.LOCAL_DATE)
    private LocalDate someDate;

    @MaskedProperty(type = MaskPatternType.OFFSET_DATE_TIME)
    private OffsetDateTime dateTime;

    @MaskedProperty(type = MaskPatternType.INN)
    private String inn;

    private String notForMaskingField;
}
