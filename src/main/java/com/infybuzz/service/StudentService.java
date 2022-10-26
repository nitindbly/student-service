package com.infybuzz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.infybuzz.entity.Student;
import com.infybuzz.feignclients.AddressFeignClient;
import com.infybuzz.repository.StudentRepository;
import com.infybuzz.request.CreateStudentRequest;
import com.infybuzz.response.AddressResponse;
import com.infybuzz.response.StudentResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;
	@Autowired
	WebClient webClient;
	@Autowired
	AddressFeignClient addressFeignClient;
	@Autowired
	CommonService commonService;
	
	Logger logger = LoggerFactory.getLogger(StudentService.class);

	public StudentResponse createStudent(CreateStudentRequest createStudentRequest) {
		Student student = new Student();
		student.setFirstName(createStudentRequest.getFirstName());
		student.setLastName(createStudentRequest.getLastName());
		student.setEmail(createStudentRequest.getEmail());
		
		student.setAddressId(createStudentRequest.getAddressId());
		student = studentRepository.save(student);
		
		StudentResponse studentResponse = new StudentResponse(student);
		//studentResponse.setAddressResponse(getAddressById(student.getAddressId()));
		//studentResponse.setAddressResponse(addressFeignClient.getById(student.getAddressId()));
		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));

		return studentResponse;
	}
	
	public StudentResponse getById (long id) {
		logger.info("Inside StudentService getById");
		Student student = studentRepository.findById(id).get();
		StudentResponse studentResponse = new StudentResponse(student);
		//studentResponse.setAddressResponse(getAddressById(student.getAddressId()));
		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));
		return studentResponse;
	}
	
//	@CircuitBreaker(name = "addressService", fallbackMethod = "fallbackGetAddressById")
//	public AddressResponse getAddressById(long addressId) {
//		//Mono<AddressResponse> addressResponse = webClient.get().uri("/getById/" + id).retrieve().bodyToMono(AddressResponse.class);
//		//return addressResponse.block();
//		AddressResponse addressResponse = addressFeignClient.getById(addressId);
//		return addressResponse;
//	}
//	
//	public AddressResponse fallbackGeAddressById(long addressId, Throwable th) {
//		return new AddressResponse();
//	}
}
