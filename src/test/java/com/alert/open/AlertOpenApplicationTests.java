package com.alert.open;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class AlertOpenApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testRateLimiter() throws Exception {
		// Gọi API nhiều lần để trigger rate limit
		for (int i = 0; i < 5; i++) {
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/greeting"))
					.andDo(print())
					.andReturn();

			System.out.println("Response " + i + ": " +
					result.getResponse().getContentAsString());
		}
	}

}
