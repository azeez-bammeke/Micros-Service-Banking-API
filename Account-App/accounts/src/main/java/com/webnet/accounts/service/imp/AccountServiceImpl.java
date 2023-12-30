package com.webnet.accounts.service.imp;

import com.webnet.accounts.constants.AccountConstants;
import com.webnet.accounts.dto.AccountDto;
import com.webnet.accounts.dto.CustomerDto;
import com.webnet.accounts.entity.Accounts;
import com.webnet.accounts.entity.Customer;
import com.webnet.accounts.exception.CustomerAlreadyExistException;
import com.webnet.accounts.exception.ResourceNotFoundException;
import com.webnet.accounts.mapper.AccountMapper;
import com.webnet.accounts.mapper.CustomerMapper;
import com.webnet.accounts.repository.AccountRepository;
import com.webnet.accounts.repository.CustomerRepository;
import com.webnet.accounts.service.IAccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> existCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (existCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already exist with the given mobile number " + customer.getMobileNumber());
        }
        customerRepository.save(customer);
        Accounts account = this.createNewAccount(customer);
        accountRepository.save(account);
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts account = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountDetails(AccountMapper.mapToAccountsDto(account, new AccountDto()));

        return customerDto;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountDto = customerDto.getAccountDetails();
        if (accountDto != null) {
            Accounts account = accountRepository.findById(accountDto.getAccountNumber())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Account", "AccountNumber", accountDto.getAccountNumber().toString()));
            AccountMapper.mapToAccounts(accountDto, account);
            account = accountRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    public Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        return newAccount;
    }
}
