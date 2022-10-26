package com.infybuzz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infybuzz.feignclients.AddressFeignClient;
import com.infybuzz.response.AddressResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CommonService {
	@Autowired
	AddressFeignClient addressFeignClient;
	
	Logger logger = LoggerFactory.getLogger(CommonService.class);
	int count = 1;
	
	@CircuitBreaker(name = "addressService", fallbackMethod = "fallbackGetAddressById")
	public AddressResponse getAddressById(long addressId) {
		logger.info("Count = " + count);
		count++;
		AddressResponse addressResponse = addressFeignClient.getById(addressId);
		return addressResponse;
	}
	
	public AddressResponse fallbackGetAddressById(long addressId, Throwable th) {
		logger.error("Error = " + th);
		return new AddressResponse();
	}

}
