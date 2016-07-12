package org.binggo.msgsender.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Simple Message Service!";
	}
	
}
