package com.logtest;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LogPrint {

    @Scheduled(fixedRate = 10000)
    public void doSmth() {
        log.info("Nothing will be masked here");
        log.info("This will mask the email test.email@domain.com");
        log.info("This is a password: testpass. That will be masked");
        log.info("Another password:testpass2. That will be masked");
        log.info("Another password testpass2. That will NOT be masked as keyword is incorrect");
        log.info("this is a card 4916246076443617 and it should be masked");
        log.info("this is a card 377235414017308 and it should be masked");
        log.info("this is a iban IE64IRCE92050112345678 and it should be masked");
        log.info("this is a iban GT20AGRO00000000001234567890 and it should be masked");
        log.info("this is IP 84.232.150.27 and it should be masked");
    }

}
