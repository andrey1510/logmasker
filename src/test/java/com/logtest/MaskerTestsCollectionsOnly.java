package com.logtest;

import com.logtest.masker.Masker;
import com.logtest.testData.TestDataForCollections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerTestsCollectionsOnly extends TestDataForCollections {

    @Test
    void testArrayList() {
        assertEquals(createArrayListMasked(), Masker.mask(createArrayList()));
    }

    @Test
    void testLinkedList() {
        assertEquals(createLinkedListMasked(), Masker.mask(createLinkedList()));
    }

    @Test
    void testListImmutable() {
        assertEquals(createListImmutableMasked(), Masker.mask(createListImmutable()));
    }

    @Test
    void testArray() {
        assertEquals(createArrayMasked(), Masker.mask(createArray()));
    }

    @Test
    void testHashSet() {
        assertEquals(createHashSetMasked(), Masker.mask(createHashSet()));
    }

    @Test
    void testLinkedHashSet() {
        assertEquals(createLinkedHashSetMasked(), Masker.mask(createLinkedHashSet()));
    }

    @Test
    void testSetImmutable() {
        assertEquals(createSetImmutableMasked(), Masker.mask(createSetImmutable()));
    }

    @Test
    void testHashMap() {
        assertEquals(createHashMapMasked(), Masker.mask(createHashMap()));
    }

    @Test
    void testLinkedHashMap() {
        assertEquals(createLinkedHashMapMasked(), Masker.mask(createLinkedHashMap()));
    }

    @Test
    void testTreeMap() {
        assertEquals(createTreeMapMasked(), Masker.mask(createTreeMap()));
    }

    @Test
    void testMapImmutable() {
        assertEquals(createMapImmutableMasked(), Masker.mask(createMapImmutable()));
    }
}
