package org.binggo.msgsender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Client command: curl -H "Accept: application/json" -H "Content-type: application/json" -X POST 
 * -d '{"subject":"test msgsender", "to": "ybzhan@ibenben.com", "content": "greetings from msgsender"}' 
 * http://localhost:8090/mail/sync
 */
@SpringBootApplication
public class MsgsenderApplication 
{
	private static final Logger logger = LoggerFactory.getLogger(MsgsenderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MsgsenderApplication.class, args);
		
		logger.info("msgsender service has been started successfully.");
	}
}
