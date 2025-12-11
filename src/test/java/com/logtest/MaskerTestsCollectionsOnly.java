package com.logtest;

import com.logtest.dto.dtoForCollection.DtoWithQueue;
import com.logtest.masker.Masker;
import com.logtest.testData.TestDataForCollections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerTestsCollectionsOnly extends TestDataForCollections {

    @Test
    void mask_testArrayList() {
        assertEquals(createArrayListMasked(), Masker.mask(createArrayList()));
    }

    @Test
    void mask_testEmptyArrayList() {
        assertEquals(createEmptyArrayListMasked(), Masker.mask(createEmptyArrayList()));
    }

    @Test
    void mask_testLinkedList() {
        assertEquals(createLinkedListMasked(), Masker.mask(createLinkedList()));
    }

    @Test
    void mask_testListImmutable() {
        assertEquals(createListImmutableMasked(), Masker.mask(createListImmutable()));
    }

    @Test
    void mask_testArray() {
        assertEquals(createArrayMasked(), Masker.mask(createArray()));
    }

    @Test
    void mask_testHashSet() {
        assertEquals(createHashSetMasked(), Masker.mask(createHashSet()));
    }

    @Test
    void mask_testLinkedHashSet() {
        assertEquals(createLinkedHashSetMasked(), Masker.mask(createLinkedHashSet()));
    }

    @Test
    void mask_testSetImmutable() {
        assertEquals(createSetImmutableMasked(), Masker.mask(createSetImmutable()));
    }

    @Test
    void mask_testHashMap() {
        assertEquals(createHashMapMasked(), Masker.mask(createHashMap()));
    }

    @Test
    void mask_testLinkedHashMap() {
        assertEquals(createLinkedHashMapMasked(), Masker.mask(createLinkedHashMap()));
    }

    @Test
    void mask_testTreeMap() {
        assertEquals(createTreeMapMasked(), Masker.mask(createTreeMap()));
    }

    @Test
    void mask_testMapImmutable() {
        assertEquals(createMapImmutableMasked(), Masker.mask(createMapImmutable()));
    }

    @Test
    void mask_testDtoWithUnsuppornedCollection() {
        DtoWithQueue expected = createQueueMasked();
        DtoWithQueue result = Masker.mask(createQueue());
        assertEquals(result.getTextField(), expected.getTextField());
        assertEquals(result.getDtos().element().getPhoneNumber(), expected.getDtos().element().getPhoneNumber());
    }

    @Test
    void mask_testSetEqualMaskedFieldValues() {

        String expected = "DtoWithSet(isMasked=true, textField=som**********ext, dtos=[" +
            "CollectionElement(isMasked=true, phoneNumber=89*******18), " +
            "CollectionElement(isMasked=true, phoneNumber=89*******18)])";

        assertEquals(expected, Masker.mask(createHashSet2()).toString());
        assertEquals(expected, Masker.mask(createSetImmutable2()).toString());


    }

}
