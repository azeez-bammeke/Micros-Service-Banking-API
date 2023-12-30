package com.webnet.accounts.service;


import com.webnet.accounts.dto.CustomerDto;
import com.webnet.accounts.entity.Accounts;
import com.webnet.accounts.entity.Customer;

public interface IAccountService {
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);
    boolean deleteAccount(String mobileNumber);
}
