package com.logtest.dto.dtoToString;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.MaskPatternType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Masked
@Builder
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoToStringOverride {

    private String text;

    @MaskedProperty(type = MaskPatternType.LOCAL_DATE)
    private LocalDate date;

    @MaskedProperty(type = MaskPatternType.OFFSET_DATE_TIME)
    private OffsetDateTime dateTime;

    private int number;

    private DtoNoMasked dtoNoMasked;

    @Override
    public String toString() {
        return "DtoAnotherToStringOverride{" +
            "text='" + text + '\'' +
            ", date=" + (date != null ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : null) +
            ", dateTime=" + (dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : null) +
            ", number=" + number +
            ", dtoNoMasked=" + dtoNoMasked +
            '}';
    }
}
