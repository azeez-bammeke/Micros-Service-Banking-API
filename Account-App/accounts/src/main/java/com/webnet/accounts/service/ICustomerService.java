package com.webnet.accounts.service;

import com.webnet.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

    CustomerDetailsDto fetchCustomerDetails(String correlationId, String mobileNumber);
}
