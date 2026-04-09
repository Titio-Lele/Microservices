package br.com.alexandredev.discovery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("DiscoveryApplication - smoke test de contexto")
class DiscoveryApplicationTests {

    @Test
    @DisplayName("deve carregar o contexto da aplicação Eureka com sucesso")
    void contextLoads() {
    }

}
