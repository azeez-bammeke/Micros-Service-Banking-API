package com.webnet.accounts.functions;

import com.webnet.accounts.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AccountsFunctions {

    @Bean
    public Consumer<Long> updateCommunication(IAccountService accountService) {
        return accountNumber -> {
         log.info("Updating communication status for the account number: {}", accountNumber.toString());
         accountService.updateCommunicationStatus(accountNumber);
        };
    }

}
