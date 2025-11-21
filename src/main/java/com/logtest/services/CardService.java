package com.logtest.services;

import com.logtest.dto.CardDto;
import com.logtest.entities.CardEntity;
import com.logtest.masker.MaskingUtil;
import com.logtest.masker.annotations.EnableMasking;
import com.logtest.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final MaskingUtil maskingUtil;

    @EnableMasking
    public CardDto createCard(CardDto cardDto) {

        log.info("Вызов метода createCard в CardService");

        log.info("В метод createCard в CardService поступило dto: {}", maskingUtil.maskDto(cardDto));
        System.out.println("То же через sout: " + cardDto);

        CardDto editedCardDto = new CardDto(cardDto.getCardNumber(), cardDto.getDescription() + " add text");
        log.info("Симуляция в методе createCard в CardService действий с dto, editedCardDto: {}", maskingUtil.maskDto(editedCardDto));
        System.out.println("То же через sout: " + maskingUtil.maskDto(editedCardDto));

        CardEntity cardEntity = new CardEntity(1, editedCardDto.getCardNumber(), editedCardDto.getDescription());
        log.info("В CardService создано entity (никогда не должно маскироваться): {}", cardEntity);
        System.out.println("То же через sout: " + cardEntity);

        CardEntity savedCard = cardRepository.emulateSave(cardEntity);
        CardDto savedCardDto = new CardDto(savedCard.getCardNumber(), savedCard.getDescription());
        log.info("В CardService поступило из репозитория сохраненное DTO: {}", maskingUtil.maskDto(savedCardDto));
        System.out.println("То же через sout: " + maskingUtil.maskDto(savedCardDto));

        return savedCardDto;

    }
}