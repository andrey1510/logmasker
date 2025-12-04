package com.logtest.dep1;

import com.logtest.dep1.dto.dtoForCollection.DtoWithQueue;
import com.logtest.masker.Masker;
import com.logtest.dep1.data.TestDataForCollections;
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

}
