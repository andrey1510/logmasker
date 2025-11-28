package com.logtest.testData;

import com.logtest.dto.dtoForCollection.CollectionElement;
import com.logtest.dto.dtoForCollection.DtoWithArray;
import com.logtest.dto.dtoForCollection.DtoWithList;
import com.logtest.dto.dtoForCollection.DtoWithMap;
import com.logtest.dto.dtoForCollection.DtoWithSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class TestDataForCollections {

    protected CollectionElement createCollectionElement1() {
        return CollectionElement.builder()
            .isMasked(false)
            .phoneNumber("79058453312")
            .build();
    }

    protected CollectionElement createCollectionElement1Masked() {
        return CollectionElement.builder()
            .isMasked(true)
            .phoneNumber("79*******12")
            .build();
    }

    protected CollectionElement createCollectionElement2() {
        return CollectionElement.builder()
            .isMasked(false)
            .phoneNumber("89058453318")
            .build();
    }

    protected CollectionElement createCollectionElement2Masked() {
        return CollectionElement.builder()
            .isMasked(true)
            .phoneNumber("89*******18")
            .build();
    }

    protected DtoWithList createArrayList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new ArrayList<>(List.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithList createArrayListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new ArrayList<>(List.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithList createLinkedList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new LinkedList<>(List.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithList createLinkedListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new LinkedList<>(List.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithList createListImmutable() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(List.of(createCollectionElement1(), createCollectionElement2()))
            .build();
    }

    protected DtoWithList createListImmutableMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(List.of(createCollectionElement1Masked(), createCollectionElement2Masked()))
            .build();
    }

    protected DtoWithArray createArray() {
        return DtoWithArray.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new CollectionElement[]{createCollectionElement1(), createCollectionElement2()})
            .build();
    }

    protected DtoWithArray createArrayMasked() {
        return DtoWithArray.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new CollectionElement[]{createCollectionElement1Masked(), createCollectionElement2Masked()})
            .build();
    }

    protected DtoWithSet createHashSet() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new HashSet<>(Set.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithSet createHashSetMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new HashSet<>(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithSet createLinkedHashSet() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new LinkedHashSet<>(Set.of(createCollectionElement1(), createCollectionElement2())))
            .build();
    }

    protected DtoWithSet createLinkedHashSetMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new LinkedHashSet<>(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked())))
            .build();
    }

    protected DtoWithSet createSetImmutable() {
        return DtoWithSet.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(Set.of(createCollectionElement1(), createCollectionElement2()))
            .build();
    }

    protected DtoWithSet createSetImmutableMasked() {
        return DtoWithSet.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(Set.of(createCollectionElement1Masked(), createCollectionElement2Masked()))
            .build();
    }

    protected DtoWithMap createHashMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new HashMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createHashMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new HashMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createLinkedHashMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new LinkedHashMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createLinkedHashMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new LinkedHashMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createTreeMap() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new TreeMap<>() {{
                put("key1", createCollectionElement1());
                put("key2", createCollectionElement2());
            }})
            .build();
    }

    protected DtoWithMap createTreeMapMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new TreeMap<>() {{
                put("key1", createCollectionElement1Masked());
                put("key2", createCollectionElement2Masked());
            }})
            .build();
    }

    protected DtoWithMap createMapImmutable() {
        return DtoWithMap.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(Map.of(
                "key1", createCollectionElement1(),
                "key2", createCollectionElement2()
            ))
            .build();
    }

    protected DtoWithMap createMapImmutableMasked() {
        return DtoWithMap.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(Map.of(
                "key1", createCollectionElement1Masked(),
                "key2", createCollectionElement2Masked()
            ))
            .build();
    }

    protected DtoWithList createEmptyArrayList() {
        return DtoWithList.builder()
            .isMasked(false)
            .textField("some random text")
            .dtos(new ArrayList<>())
            .build();
    }

    protected DtoWithList createEmptyArrayListMasked() {
        return DtoWithList.builder()
            .isMasked(true)
            .textField("som*****ext")
            .dtos(new ArrayList<>())
            .build();
    }


}
