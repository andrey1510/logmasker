package com.logtest.dto.dtoForCollection;

import com.logtest.masker.annotations.Masked;
import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.MaskPatternType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Masked
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoWithCollectionsRegularElements {

    private Boolean isMasked;

    @MaskedProperty(type = MaskPatternType.INN)
    private List<String> inns;

    @MaskedProperty(type = MaskPatternType.KPP)
    private Set<String> kpps;

    @MaskedProperty(type = MaskPatternType.LOCAL_DATE)
    private Map<String, LocalDate> dates;

}
