package com.prapps.aws.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailApplicationTests {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSendEmail() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		MessageRequest messageRequest = new MessageRequest();
		EmailAccount emailAccount = new EmailAccount();
		emailAccount.setHost("smtp.gmail.com");
		emailAccount.setPassword("chinat0wn");
		emailAccount.setPort(587);
		emailAccount.setUsername("pratik006@gmail.com");
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setFrom("pioneerstest102@gmail.com");
		emailMessage.setRecipients("pioneerstest101@gmail.com");
		emailMessage.setSubject("Test Subject " + UUID.randomUUID());
		emailMessage.setTextBody("Test Body " + UUID.randomUUID());
		messageRequest.setEmailAccount(emailAccount);
		messageRequest.setEmailMessage(emailMessage);
		String jsonContent = mapper.writeValueAsString(messageRequest);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MessageRequest> request = new HttpEntity<>(messageRequest, headers);
		String response = restTemplate.postForObject(
				String.format("http://localhost:%d/email/send", port),
				request, String.class);
		System.out.println("response: "+response);
	}

}
