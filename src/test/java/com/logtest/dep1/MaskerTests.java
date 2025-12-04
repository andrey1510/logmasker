package com.logtest.dep1;

import com.logtest.dep1.dto.DtoForRecursion;
import com.logtest.masker.Masker;
import com.logtest.dep1.data.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class MaskerTests extends TestData {

    @Test
    void mask_testAllFields() {
        assertEquals(createAllPatternDtoMasked(), Masker.mask(createAllPatternDto()));
    }

    @Test
    void mask_testNestedFields() {
        assertEquals(createPersonMasked(), Masker.mask(createPerson()));
    }

    @Test
    void mask_testDeepRecursion() {

        int dtoStructureLevels = 100;

        DtoForRecursion result = Masker.mask(createDeepRecursionDto(dtoStructureLevels));

        assertNotNull(result);

        Stream.iterate(result, DtoForRecursion::getDto)
            .limit(dtoStructureLevels)
            .takeWhile(Objects::nonNull)
            .forEach(dto -> {
                assertTrue(dto.isMasked());
                assertNotEquals("1111", dto.getPin());
            });
    }

    @Test
    void mask_testDtoWithNoFieldsForMasking() {
        assertEquals(createNoFieldsForMaskingDtoMasked(), Masker.mask(createNoFieldsForMaskingDto()));
    }

    @Test
    void mask_testNoMaskedAnnotationDto() {
        assertEquals(createNoMaskedAnnotationDto(), Masker.mask(createNoMaskedAnnotationDto()));
    }

    @Test
    void mask_testDtoWithWrongIsMaskedField() {
        assertEquals(createDtoWithWrongIsMaskedFieldMasked(), Masker.mask(createDtoWithWrongIsMaskedField()));
    }

    @Test
    void mask_testDtoWithNoIsMaskedField() {
        assertEquals(createDtoWithNoIsMaskedFieldMasked(), Masker.mask(createDtoWithNoIsMaskedField()));
    }

    @Test
    void mask_testIsMaskedFieldTrue() {
        assertEquals(createSimpleDtoForMaskingMasked(), Masker.mask(createSimpleDtoForMaskingIsMaskedTrue()));
    }

    @Test
    void mask_testIsMaskedFieldNull() {
        assertEquals(createSimpleDtoForMaskingMasked(), Masker.mask(createSimpleDtoForMaskingIsMaskedNull()));
    }

    @Test
    void mask_testFieldWrongPatternAndType() {
        assertEquals(createDtoWithWrongPatternAndTypeMasked(), Masker.mask(createDtoWithWrongPatternAndType()));
    }

}
