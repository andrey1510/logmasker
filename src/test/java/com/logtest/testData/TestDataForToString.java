package com.logtest.testData;

import com.logtest.dto.dtoToString.DtoNoMasked;
import com.logtest.dto.dtoToString.DtoToStringOverride;
import com.logtest.dto.dtoToString.DtoWithFlaws;
import com.logtest.dto.dtoToString.NestedDto;
import com.logtest.dto.dtoToString.NestedDtoNoToString;
import com.logtest.dto.dtoToString.Dto;
import com.logtest.dto.dtoToString.DtoNoToString;
import com.logtest.dto.dtoToString.NestedDtoWithFlaws;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TestDataForToString {

    private static final String INN = "642125910000";
    private static final LocalDate DATE_1 = LocalDate.of(2023, 5, 2);
    private static final LocalDate DATE_2 = LocalDate.of(2024, 4, 3);
    private static final LocalDate DATE_3 = LocalDate.of(2024, 8, 3);
    private static final LocalDate DATE_4 = LocalDate.of(2025, 8, 3);
    private static final OffsetDateTime DATE_TIME_1 = OffsetDateTime.of(2022, 4, 5, 4, 4, 4, 4, ZoneOffset.UTC);
    private static final OffsetDateTime DATE_TIME_2 = OffsetDateTime.of(2025, 4, 5, 6, 4, 4, 4, ZoneOffset.UTC);

    public static final String DTO_MASKED = "Dto(localDate=****-05-02, offsetDateTime=****-04-05T04:04:04.000000004Z, nestedDto=NestedDto(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), listWithDtos=[NestedDto(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), NestedDto(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)], mapWithDtos={key1=NestedDto(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), key2=NestedDto(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)}, setWithDtos=[NestedDto(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z)], arrayWithDtos=[NestedDto(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), NestedDto(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)], listWithLocalDates=[****-05-02, ****-04-03], setWithLocalDates=[****-05-02], mapWithLocalDates={key1=****-05-02, key2=****-04-03}, listWithOffsetDateTime=[****-04-05T04:04:04.000000004Z, ****-04-05T06:04:04.000000004Z], setWithOffsetDateTime=[****-04-05T04:04:04.000000004Z], mapWithOffsetDateTime={key1=****-04-05T04:04:04.000000004Z, key2=****-04-05T06:04:04.000000004Z})";
    public static final String DTO_NO_TO_STRING_MASKED = "DtoNoToString(localDate=****-05-02, offsetDateTime=****-04-05T04:04:04.000000004Z, nestedDto=NestedDtoNoToString(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), listWithDtos=[NestedDtoNoToString(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), NestedDtoNoToString(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)], mapWithDtos={key1=NestedDtoNoToString(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), key2=NestedDtoNoToString(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)}, setWithDtos=[NestedDtoNoToString(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z)], arrayWithDtos=[NestedDtoNoToString(localDate=****-05-02, dateTime=****-04-05T04:04:04.000000004Z), NestedDtoNoToString(localDate=****-04-03, dateTime=****-04-05T06:04:04.000000004Z)], listWithLocalDates=[****-05-02, ****-04-03], setWithLocalDates=[****-05-02], mapWithLocalDates={key1=****-05-02, key2=****-04-03}, listWithOffsetDateTime=[****-04-05T04:04:04.000000004Z, ****-04-05T06:04:04.000000004Z], setWithOffsetDateTime=[****-04-05T04:04:04.000000004Z], mapWithOffsetDateTime={key1=****-04-05T04:04:04.000000004Z, key2=****-04-05T06:04:04.000000004Z})";
    public static final String DTO_WITH_FLAWS_MASKED = "DtoWithFlaws(isMasked=true, localDate=null, offsetDateTime=null, localDateNoMask=2023-05-02, offsetDateTimeNoMask=2022-04-05T04:04:04.000000004Z, nestedDto=null, mapWithDtos={key1=NestedDtoWithFlaws(isMasked=true, localDate=****-08-03, dateTime=null, listWithLocalDates=null, mapWithLocalDates=null, inn=64*****10000), key2=NestedDtoWithFlaws(isMasked=true, localDate=****-08-03, dateTime=null, listWithLocalDates=null, mapWithLocalDates=null, inn=64*****10000)}, setWithDtos=[NestedDtoWithFlaws(isMasked=true, localDate=****-08-03, dateTime=null, listWithLocalDates=null, mapWithLocalDates=null, inn=64*****10000), NestedDtoWithFlaws(isMasked=true, localDate=****-08-03, dateTime=null, listWithLocalDates=null, mapWithLocalDates=null, inn=64*****10000)], listWithLocalDates=null, mapWithLocalDates={key1=null, key2=null}, setWithLocalDates=[****-08-03, ****-08-03])";
    public static final String DTO_TO_STRING_OVERRIDE = "DtoToStringOverride(text=some text, date=****-05-02, dateTime=****-04-05T04:04:04.000000004Z, number=8, dtoNoMasked=DtoNoMasked(text=some text, date=2023-05-02, number=8))";

    protected DtoNoMasked createDtoNoMasked() {
        return DtoNoMasked.builder()
            .date(DATE_1)
            .text("some text")
            .number(8)
            .build();
    }

    protected DtoToStringOverride createDtoToStringOverride() {
        return DtoToStringOverride.builder()
            .date(DATE_1)
            .dateTime(DATE_TIME_1)
            .text("some text")
            .number(8)
            .dtoNoMasked(createDtoNoMasked())
            .build();
    }

    protected NestedDtoWithFlaws createNullNestedDtoWithFlaws1() {
        return NestedDtoWithFlaws.builder()
            .isMasked(false)
            .localDate(DATE_3)
            .dateTime(null)
            .mapWithLocalDates(null)
            .listWithLocalDates(null)
            .inn(INN)
            .build();
    }

    protected NestedDtoWithFlaws createNullNestedDtoWithFlaws2() {
        return NestedDtoWithFlaws.builder()
            .isMasked(false)
            .localDate(DATE_4)
            .dateTime(null)
            .mapWithLocalDates(null)
            .listWithLocalDates(null)
            .inn(INN)
            .build();
    }

    protected DtoWithFlaws createDtoWithFlaws() {
        return DtoWithFlaws.builder()
            .localDate(null)
            .offsetDateTime(null)
            .localDateNoMask(DATE_1)
            .offsetDateTimeNoMask(DATE_TIME_1)
            .mapWithDtos(new HashMap<>() {{
                put("key1", createNullNestedDtoWithFlaws1());
                put("key2", createNullNestedDtoWithFlaws2());
            }})
            .mapWithLocalDates(new HashMap<>() {{
                put("key1", null);
                put("key2", null);
            }})
            .listWithLocalDates(null)
            .setWithLocalDates(new HashSet<>(Set.of(DATE_3, DATE_4)))
            .setWithDtos(new HashSet<>(Set.of(createNullNestedDtoWithFlaws1(), createNullNestedDtoWithFlaws2())))
            .nestedDto(null)
            .build();
    }

    protected NestedDto createNestedDto1() {
        return NestedDto.builder()
            .localDate(DATE_1)
            .dateTime(DATE_TIME_1)
            .build();
    }

    protected NestedDto createNestedDto2() {
        return NestedDto.builder()
            .localDate(DATE_2)
            .dateTime(DATE_TIME_2)
            .build();
    }

    protected Dto createDto() {
        return Dto.builder()
            .localDate(DATE_1)
            .offsetDateTime(DATE_TIME_1)
            .nestedDto(createNestedDto1())
            .arrayWithDtos(new NestedDto[]{createNestedDto1(), createNestedDto2()})
            .listWithDtos(new ArrayList<>(List.of(createNestedDto1(), createNestedDto2())))
            .setWithDtos(new HashSet<>(Set.of(createNestedDto1())))
            .mapWithDtos(new HashMap<>() {{
                put("key1", createNestedDto1());
                put("key2", createNestedDto2());
            }})
            .setWithLocalDates(new HashSet<>(Set.of(DATE_1)))
            .setWithOffsetDateTime(new HashSet<>(Set.of(DATE_TIME_1)))
            .listWithOffsetDateTime(new ArrayList<>(List.of(DATE_TIME_1, DATE_TIME_2)))
            .listWithLocalDates(new ArrayList<>(List.of(DATE_1, DATE_2)))
            .mapWithLocalDates(new HashMap<>() {{
                put("key1", DATE_1);
                put("key2", DATE_2);
            }})
            .mapWithOffsetDateTime(new HashMap<>() {{
                put("key1", DATE_TIME_1);
                put("key2", DATE_TIME_2);
            }})
            .build();
    }

    protected NestedDtoNoToString createNestedDtoNoToStringOverride1() {
        return NestedDtoNoToString.builder()
            .localDate(DATE_1)
            .dateTime(DATE_TIME_1)
            .build();
    }

    protected NestedDtoNoToString createNestedDtoNoToStringOverride2() {
        return NestedDtoNoToString.builder()
            .localDate(DATE_2)
            .dateTime(DATE_TIME_2)
            .build();
    }

    protected DtoNoToString createDtoNoToString() {
        return DtoNoToString.builder()
            .localDate(DATE_1)
            .offsetDateTime(DATE_TIME_1)
            .nestedDto(createNestedDtoNoToStringOverride1())
            .arrayWithDtos(new NestedDtoNoToString[]{createNestedDtoNoToStringOverride1(), createNestedDtoNoToStringOverride2()})
            .listWithDtos(new ArrayList<>(List.of(createNestedDtoNoToStringOverride1(), createNestedDtoNoToStringOverride2())))
            .setWithDtos(new HashSet<>(Set.of(createNestedDtoNoToStringOverride1())))
            .mapWithDtos(new HashMap<>() {{
                put("key1", createNestedDtoNoToStringOverride1());
                put("key2", createNestedDtoNoToStringOverride2());
            }})
            .setWithLocalDates(new HashSet<>(Set.of(DATE_1)))
            .setWithOffsetDateTime(new HashSet<>(Set.of(DATE_TIME_1)))
            .listWithOffsetDateTime(new ArrayList<>(List.of(DATE_TIME_1, DATE_TIME_2)))
            .listWithLocalDates(new ArrayList<>(List.of(DATE_1, DATE_2)))
            .mapWithLocalDates(new HashMap<>() {{
                put("key1", DATE_1);
                put("key2", DATE_2);
            }})
            .mapWithOffsetDateTime(new HashMap<>() {{
                put("key1", DATE_TIME_1);
                put("key2", DATE_TIME_2);
            }})
            .build();
    }

}
