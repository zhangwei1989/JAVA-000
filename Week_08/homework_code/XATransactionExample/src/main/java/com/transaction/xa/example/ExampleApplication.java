package com.transaction.xa.example;

import com.transaction.xa.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ExampleApplication {

	@Autowired
	private OrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

	@PostConstruct
	public void executeOrderService() {
		orderService.insert(3);
		orderService.selectAll();
		orderService.insertFailed(4);
		orderService.selectAll();
	}

}
