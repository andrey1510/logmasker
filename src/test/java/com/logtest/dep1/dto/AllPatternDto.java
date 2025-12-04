package com.logtest.dep1.dto;

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

    @MaskedProperty(type = "FULL_NAME")
    private String fullname;

    @MaskedProperty(type = "EMAIL")
    private String email;

    @MaskedProperty(type = "TEXT_FIELD")
    private String textField;

    @MaskedProperty(type = "AUTH_DATA")
    private String pan;

    @MaskedProperty(type = "SURNAME")
    private String surname;

    @MaskedProperty(type = "FULL_ADDRESS")
    private String fullAddress;

    @MaskedProperty(type = "PASSPORT_SERIES_AND_NUMBER")
    private String passportSeries;

    @MaskedProperty(type = "LOCAL_DATE")
    private LocalDate someDate;

    @MaskedProperty(type = "OFFSET_DATE_TIME")
    private OffsetDateTime dateTime;

    private String notForMaskingField;
}
