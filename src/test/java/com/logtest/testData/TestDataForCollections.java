package com.logtest.testData;

import com.logtest.dto.dtoForCollection.CollectionElement;
import com.logtest.dto.dtoForCollection.DtoWithArray;
import com.logtest.dto.dtoForCollection.DtoWithList;
import com.logtest.dto.dtoForCollection.DtoWithMap;
import com.logtest.dto.dtoForCollection.DtoWithQueue;
import com.logtest.dto.dtoForCollection.DtoWithSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public abstract class TestDataForCollections {

    private static final String TEXT = "some random text";
    private static final String TEXT_MASKED = "som**********ext";
    private static final String PHONE_1 = "79058453312";
    private static final String PHONE_1_MASKED = "79*******12";
    private static final String PHONE_2 = "89058453318";
    private static final String PHONE_2_MASKED = "89*******18";
    private static final String PHONE_3 = "89058003318";

    protected CollectionElement createCollectionElement1() {
        return CollectionElement.builder()
            .isMasked(false)
            .phoneNumber(PHONE_1)
            .build();
    }

    protected CollectionElement createCollectionElement1Masked() {
        return CollectionElement.builder()
            .isMasked(true)
            .phoneNumber(PHONE_1_MASKED)
            .build();
    }

    protected CollectionElement createCollectionElement2() {
        return CollectionElement.builder()
            .isMasked(false)
            .phoneNumber(PHONE_2 )
            .build();
    }

    protected CollectionElement createCollectionElement3Masked() {
        return CollectionElement.builder()
            .isMasked(true)
            .phoneNumber(PHONE_2_MASKED)
            .build();
    }

    protected CollectionElement createCollectionElement3() {
        return CollectionElement.builder()
            .isMasked(false)
            .phoneNumber(PHONE_3)
            .build();
    }

    protected CollectionElement createCollectionElement2Masked() {
        return CollectionElement.builder()
            .isMasked(true)
            .phoneNumber(PHONE_2_MASKED)
            .build();
    }

    protected DtoWithList createArrayList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new ArrayList<>(List.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithList createArrayListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new ArrayList<>(List.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithList createLinkedList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new LinkedList<>(List.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithList createLinkedListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new LinkedList<>(List.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithList createListImmutable() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(List.of(createCollectionElement1(), createCollectionElement2()))
            .build();
    }

    protected DtoWithList createListImmutableMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(List.of(createCollectionElement1Masked(), createCollectionElement2Masked()))
            .build();
    }

    protected DtoWithArray createArray() {
        return DtoWithArray.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new CollectionElement[]{createCollectionElement1(), createCollectionElement2()})
            .build();
    }

    protected DtoWithArray createArrayMasked() {
        return DtoWithArray.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new CollectionElement[]{createCollectionElement1Masked(), createCollectionElement2Masked()})
            .build();
    }

    protected DtoWithSet createHashSet() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new HashSet<>(Set.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithSet createHashSetMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new HashSet<>(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithSet createHashSet2() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new HashSet<>(Set.of(createCollectionElement2(), createCollectionElement3())))
            .build();
    }

    protected DtoWithSet createLinkedHashSet() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new LinkedHashSet<>(Set.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithSet createLinkedHashSetMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new LinkedHashSet<>(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithSet createSetImmutable() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(Set.of(createCollectionElement1(), createCollectionElement2()))
            .build();
    }

    protected DtoWithSet createSetImmutableMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked()))
            .build();
    }

    protected DtoWithSet createSetImmutable2() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(Set.of(createCollectionElement2(), createCollectionElement3()))
            .build();
    }

    protected DtoWithMap createHashMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new HashMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createHashMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new HashMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createLinkedHashMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new LinkedHashMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createLinkedHashMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new LinkedHashMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createTreeMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new TreeMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createTreeMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new TreeMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createMapImmutable() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(Map.of(
                "key1", createCollectionElement1(),
                "key2", createCollectionElement2()
            ))
            .build();
    }

    protected DtoWithMap createMapImmutableMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(Map.of(
                "key1", createCollectionElement1Masked(),
                "key2", createCollectionElement2Masked()
            ))
            .build();
    }

    protected DtoWithList createEmptyArrayList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(new ArrayList<>())
            .build();
    }

    protected DtoWithList createEmptyArrayListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(new ArrayList<>())
            .build();
    }

    protected DtoWithQueue createQueue() {
        PriorityQueue<CollectionElement> queue = new PriorityQueue<>(
            Comparator.comparing(CollectionElement::getPhoneNumber));
        queue.add(createCollectionElement1());

        return DtoWithQueue.builder()
            .isMasked(false)
            .textField(TEXT)
            .dtos(queue)
            .build();
    }

    protected DtoWithQueue createQueueMasked() {
        PriorityQueue<CollectionElement> queue = new PriorityQueue<>(
            Comparator.comparing(CollectionElement::getPhoneNumber));
        queue.add(createCollectionElement1());

        return DtoWithQueue.builder()
            .isMasked(true)
            .textField(TEXT_MASKED)
            .dtos(queue)
            .build();
    }
}
