package com.logtest.repositories;

import com.logtest.entities.CardEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CardRepository {

    public CardEntity emulateSave(CardEntity card) {
        log.info("Репозиторий - пришло entity (никогда не должно маскироваться): {}", card);
        return card;
    }

}
