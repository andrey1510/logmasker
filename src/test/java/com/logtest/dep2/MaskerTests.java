package com.logtest.dep2;

import com.logtest.dep2.data.TestData;
import com.logtest.masker.Masker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerTests extends TestData {

    @Test
    void mask_testAllFields() {
        assertEquals(createFirstLevelDtoMasked(), Masker.mask(createFirstLevelDto()));
    }


}
