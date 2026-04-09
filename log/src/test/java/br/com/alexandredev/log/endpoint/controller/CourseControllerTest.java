package br.com.alexandredev.log.endpoint.controller;

import br.com.alexandredev.core.model.Course;
import br.com.alexandredev.log.endpoint.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Usa @SpringBootTest + @AutoConfigureMockMvc em vez de @WebMvcTest
 * porque LogApplication declara @EnableJpaRepositories explicitamente,
 * o que conflita com o slice de @WebMvcTest por tentar bootstrapar JPA
 * sem EntityManagerFactory disponível. O application.yml de teste já
 * configura H2 e desabilita Flyway, tornando o contexto completo viável.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("CourseController - testes de integração da camada web")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Test
    @DisplayName("deve retornar HTTP 200 e lista de cursos no corpo da resposta")
    void deve_retornar_200_e_lista_de_cursos() throws Exception {
        // Arrange
        var courses = List.of(
                Course.builder().id(1L).title("Arquitetura de Microserviços com Spring Boot").build(),
                Course.builder().id(2L).title("Docker e Orquestração de Containers").build()
        );
        when(courseService.list(any(Pageable.class))).thenReturn(new PageImpl<>(courses));

        // Act / Assert
        mockMvc.perform(get("/v1/admin/course")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Arquitetura de Microserviços com Spring Boot"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Docker e Orquestração de Containers"));
    }

    @Test
    @DisplayName("deve retornar HTTP 200 com lista vazia quando não há cursos cadastrados")
    void deve_retornar_200_com_lista_vazia_quando_nao_ha_cursos() throws Exception {
        // Arrange
        when(courseService.list(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act / Assert
        mockMvc.perform(get("/v1/admin/course")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("deve retornar Content-Type application/json independente do Accept header")
    void deve_retornar_content_type_json() throws Exception {
        // Arrange
        when(courseService.list(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act / Assert
        mockMvc.perform(get("/v1/admin/course"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("deve aceitar parâmetros de paginação via query string")
    void deve_aceitar_parametros_de_paginacao() throws Exception {
        // Arrange
        when(courseService.list(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act / Assert
        mockMvc.perform(get("/v1/admin/course")
                        .param("page", "0")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

