package br.com.alexandredev.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/*
 * Usa RANDOM_PORT para que o servidor reativo do Spring Cloud Gateway
 * inicialize completamente. Os testes verificam o comportamento das rotas
 * via WebTestClient:
 * - 5xx confirma que a rota existe e o gateway tentou encaminhar (sem serviço log no ar)
 * - 404 indicaria que a rota não foi configurada
 *
 * O application.yml de teste desabilita Eureka e configura uma instância
 * local simulada do serviço 'log' via SimpleDiscoveryClient.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GatewayApplication - testes de contexto e configuração de rotas")
class GatewayApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("deve carregar o contexto da aplicação com sucesso")
    void contextLoads() {
    }

    @Test
    @DisplayName("deve ter rota configurada para /gateway/log/** (5xx = rota existe, serviço log indisponível no teste)")
    void deve_ter_rota_configurada_para_log_service() {
        /*
         * Se a rota NÃO existisse, o gateway retornaria 404.
         * 5xx confirma que a rota está configurada e o gateway tentou
         * encaminhar para lb://log, mas o serviço não está no ar neste contexto de teste.
         */
        webTestClient.get()
                .uri("/gateway/log/v1/admin/course")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("deve retornar 404 para caminho não mapeado em nenhuma rota")
    void deve_retornar_404_para_caminho_nao_mapeado() {
        webTestClient.get()
                .uri("/rota-inexistente/qualquer-coisa")
                .exchange()
                .expectStatus().isNotFound();
    }
}
