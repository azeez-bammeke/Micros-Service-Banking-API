package com.webnet.accounts.service.imp;

import com.webnet.accounts.dto.AccountDto;
import com.webnet.accounts.dto.CardsDto;
import com.webnet.accounts.dto.CustomerDetailsDto;
import com.webnet.accounts.dto.LoansDto;
import com.webnet.accounts.entity.Accounts;
import com.webnet.accounts.entity.Customer;
import com.webnet.accounts.exception.ResourceNotFoundException;
import com.webnet.accounts.mapper.AccountMapper;
import com.webnet.accounts.mapper.CustomerMapper;
import com.webnet.accounts.repository.AccountRepository;
import com.webnet.accounts.repository.CustomerRepository;
import com.webnet.accounts.service.ICustomerService;
import com.webnet.accounts.service.client.CardsFeignClient;
import com.webnet.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements ICustomerService {
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;


    public CustomerDetailsDto fetchCustomerDetails(String correlationId, String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper
                .mapToCustomerDetailsDto(customer, new CustomerDetailsDto());

        customerDetailsDto.setAccountDetails(AccountMapper
                .mapToAccountsDto(accounts, new AccountDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if (null != loansDtoResponseEntity) {
            customerDetailsDto.setLoanDetails(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if (null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardDetails(cardsDtoResponseEntity.getBody());
        }
        return customerDetailsDto;
    }
}
