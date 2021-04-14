package com.ttd.demo001;

import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties

//@EnableAutoConfiguration
public class Demo001Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo001Application.class, args);
		// some comment
	}

	@Bean
	public JSONParser getJsonParser() {
		return new JSONParser();
	}

}
