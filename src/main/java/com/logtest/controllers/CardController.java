package com.logtest.controllers;

import com.logtest.dto.CardDto;
import com.logtest.masker.MaskingUtil;
import com.logtest.masker.annotations.EnableMasking;
import com.logtest.services.CardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    private final MaskingUtil maskingUtil;

    @EnableMasking      //постановка аннотации включает маскировку
    @Operation(description = "Маскировка через аннотацию.")
    @PostMapping("/masked")
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) {

        log.info("проверка маскировки DTO в логгере - должна работать при указании @EnableMasking: {}",
            maskingUtil.maskDto(cardDto));
        System.out.println("проверка маскировки DTO в sout - должна работать при указании @EnableMasking: " +
            maskingUtil.maskDto(cardDto));
        log.info("проверка маскировки DTO в логгере - НЕ должна работать никогда {}", cardDto);
        System.out.println("проверка маскировки DTO в sout - НЕ должна работать никогда: " + cardDto);

        log.info("Контроллер - пришло DTO для сохранения: {}", maskingUtil.maskDto(cardDto));

        CardDto savedDto = cardService.createCard(cardDto);
        log.info("Контроллер - пришло сохраненное DTO: {}", maskingUtil.maskDto(savedDto));

        return ResponseEntity.ok(savedDto);
    }

}
