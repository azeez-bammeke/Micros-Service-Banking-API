package com.webnet.accounts.controller;

import com.webnet.accounts.dto.CustomerDetailsDto;
import com.webnet.accounts.service.ICustomerService;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class CustomerController {

    private final ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("webnet-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})",
                    message = "Phone number must be 10 digits") String mobileNumber) {
        log.debug("fetchCustomerDetails method start");

        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(correlationId, mobileNumber);
        log.debug("fetchCustomerDetails method end");

        return ResponseEntity.ok(customerDetailsDto);
    }

}
