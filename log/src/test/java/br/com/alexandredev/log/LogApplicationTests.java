package br.com.alexandredev.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("LogApplication - smoke test de contexto")
class LogApplicationTests {

	@Test
	@DisplayName("deve carregar o contexto da aplicação com sucesso")
	void contextLoads() {
	}

}
