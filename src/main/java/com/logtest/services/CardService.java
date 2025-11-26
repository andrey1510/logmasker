package com.logtest.services;

import com.logtest.dto.nestedDto.SecondLevelDto;
import com.logtest.dto.nestedDto.FirstLevelDto;
import com.logtest.dto.nestedDto.ThirdLevelDto;
import com.logtest.dto.patternTypes.AllStringTypesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.logtest.masker.Masker.mask;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    ThirdLevelDto thirdLevelDto1 = new ThirdLevelDto(
        false,
        "first@email.com",
        LocalDate.of(2011, 11, 11),
        "smth"
    );
    ThirdLevelDto thirdLevelDto2 = new ThirdLevelDto(
        false,
        "second@email.com",
        LocalDate.of(2010, 10, 10),
        ""
    );
    HashSet<ThirdLevelDto> thirdLevelDtos = new HashSet<>(Arrays.asList(thirdLevelDto1, thirdLevelDto2));
    HashMap<String, ThirdLevelDto> dtoMap = new HashMap<>(Map.of(
        "key1", thirdLevelDto1,
        "key2", thirdLevelDto2
    ));
    SecondLevelDto secondLevelDto1 = new SecondLevelDto(
        false,
        "2234567890123451",
        "100000",
        thirdLevelDto1,
        thirdLevelDtos,
        dtoMap,
        "descr"
    );
    SecondLevelDto secondLevelDto2 = new SecondLevelDto(
        false,
        "2234567890123452",
        "20000000",
        thirdLevelDto1,
        thirdLevelDtos,
        dtoMap,
        "descr"
    );
    FirstLevelDto firstLevelDto = new FirstLevelDto(
        "Иванов Иван Иванович",
        secondLevelDto1,
        List.of(secondLevelDto1, secondLevelDto2)
    );

    AllStringTypesDto allStringTypesDto = new AllStringTypesDto(
        "4234567890123451",
        "sometext",
        "Петров Петр Петрович",
        "test@mail.com",
        "123456",
        "not to be masked",
        LocalDate.of(2012, 12, 12),
        null,
        LocalDate.of(2001, 10, 10),
        null,
        LocalDate.of(2002, 10, 10),
        null
    );



    public FirstLevelDto createCard(boolean needMasking) {

        if (needMasking) {
            FirstLevelDto maskedDto = mask(firstLevelDto);
            log.info("Masked Dto in logger: {}", maskedDto);
            return maskedDto;
        } else {
            log.info("Not masked Dto in logger: {}", firstLevelDto);
            return firstLevelDto;
        }
    }

    public AllStringTypesDto createCard2(boolean needMasking) {

        if (needMasking) {
            AllStringTypesDto maskedDto = mask(allStringTypesDto);
            log.info("Masked Dto in logger: {}", maskedDto);
            return maskedDto;
        } else {
            log.info("Not masked Dto in logger: {}", allStringTypesDto);
            return allStringTypesDto;
        }
    }

}
