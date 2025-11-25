package com.logtest.controllers;

import com.logtest.dto.nestedDto.FirstLevelDto;
import com.logtest.dto.patternTypes.AllStringTypesDto;
import com.logtest.services.CardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping("/maskednested")
    public ResponseEntity<FirstLevelDto> createNestedCard(
        @Parameter(description = "Флаг маскировки данных. Если true - чувствительные данные будут замаскированы")
        @RequestParam(defaultValue = "true") boolean needMasking)
    {
        return ResponseEntity.ok(cardService.createCard(needMasking));
    }

    @PostMapping("/masked")
    public ResponseEntity<AllStringTypesDto> createCard(
        @Parameter(description = "Флаг маскировки данных. Если true - чувствительные данные будут замаскированы")
        @RequestParam(defaultValue = "true") boolean needMasking)
    {
        return ResponseEntity.ok(cardService.createCard2(needMasking));
    }

}
