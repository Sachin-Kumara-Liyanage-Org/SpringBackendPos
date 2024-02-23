package com.techbirdssolutions.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;

@SpringBootApplication
public class PosApplication
{

	public static void main(String[] args) {
		SpringApplication.run(PosApplication.class, args);
	}

	public void data()
	{
		String asd="Hellow World";
		System.out.println(asd);
	}
}
