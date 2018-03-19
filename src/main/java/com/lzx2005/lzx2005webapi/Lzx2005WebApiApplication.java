package com.lzx2005.lzx2005webapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.lzx2005")
public class Lzx2005WebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lzx2005WebApiApplication.class, args);
	}
}
