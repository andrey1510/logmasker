package com.logtest.dep1.dto.nestedDto;

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
public class Passport {

    private boolean isMasked;

    @MaskedProperty(type = "PASSPORT_SERIES_AND_NUMBER")
    private String passportSeriesAndNumber;

    @MaskedProperty(type = "LOCAL_DATE")
    private LocalDate issuanceDate;

}
