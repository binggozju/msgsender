package org.binggo.msgsender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.binggo.msgsender.generate.mapper")
public class MsgsenderApplication 
{
	private static final Logger logger = LoggerFactory.getLogger(MsgsenderApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MsgsenderApplication.class);
		app.run(args);
		
		logger.info("start msgsender service successfully.");
	}
}
