package com.logtest.dep2.dto;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstLevelDto {

    private boolean isMasked;

    @MaskedProperty(type = "NAME")
    private String fullname;

    @MaskedProperty(type = "EMAIL")
    private String email;

    @MaskedProperty(type = "TEXT_FIELD")
    private String textField;

    @MaskedProperty(type = "PASSPORT")
    private String passport;

    @MaskedProperty(type = "LOCAL_DATE")
    private LocalDate someDate;

    @MaskedProperty(type = "OFFSET_DATE_TIME")
    private OffsetDateTime dateTime;

    private List<NestedDto> dtos;

    private NestedDto nestedDto;

    private String notForMaskingField;
}
