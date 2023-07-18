package com.kyozhou.jbooter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileNotFoundException;

@SpringBootApplication
@EnableScheduling
public class MyApplication {

	public static void main(String[] args){
		SpringApplication.run(MyApplication.class, args);
	}

}
