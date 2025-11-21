package com.logtest.repositories;

import com.logtest.entities.CardEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CardRepository {

    public CardEntity emulateSave(CardEntity card) {
        log.info("Вызов метода emulateSave в CardRepository");
        log.info("Это entity будет сохранено в базу через CardRepository (никогда не должно маскироваться): {}", card);
        System.out.println("То же через sout: " + card);
        return card;
    }

}
