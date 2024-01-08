package com.webnet.accounts.service.client;

import com.webnet.accounts.dto.CardsDto;
import com.webnet.accounts.dto.LoansDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", fallback = LoansFallBack.class)
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("webnet-correlation-id") String correlationId,
                                                     @RequestParam String mobileNumber);

}
