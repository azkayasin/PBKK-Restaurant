package com.packtpub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.packtpub.RestaurantApplication;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
public class RestaurantApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}
	
	@ResponseBody
	@RequestMapping("/")
	public String sayAloha(){
		return "Aloha";
	}

}
