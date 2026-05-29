package com.example.BookingHotel;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingHotelApplication {

	public static void main(String[] args) {
		//tự đông load file .evn vào system properties
		Dotenv.configure().systemProperties().load();
		SpringApplication.run(BookingHotelApplication.class, args);
	}

}
