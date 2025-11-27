package com.logtest;

import com.logtest.dto.AllPatternDto;
import com.logtest.dto.nestedDto.Person;
import com.logtest.masker.Masker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MaskerTests extends TestData {

    @Test
    void testAllFields() {
        AllPatternDto allPatternDto = createAllPatternDto();
        AllPatternDto allPatternDtoMasked = createAllPatternDtoMasked();

        assertEquals(allPatternDtoMasked, Masker.mask(allPatternDto));
    }

    @Test
    void testNestedFields() {
        Person person = createPerson();
        Person personMasked = createPersonMasked();

        assertEquals(personMasked, Masker.mask(person));
    }

}