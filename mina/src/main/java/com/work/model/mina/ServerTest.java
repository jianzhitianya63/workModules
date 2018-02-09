package com.work.model.mina;


import org.springframework.context.support.ClassPathXmlApplicationContext;
public class ServerTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
	}
}
