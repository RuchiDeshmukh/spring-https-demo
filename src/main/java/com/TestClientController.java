package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestClientController {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/message")
	public String getMessage() {
				
		String response = restTemplate.getForObject("https://localhost:7777/getData",String.class);
		
		return response;
		
	}

}
