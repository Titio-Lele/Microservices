package br.com.alexandredev.log.repository;

import br.com.alexandredev.core.model.Course;
import br.com.alexandredev.core.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * @DataJpaTest encontra LogApplication como @SpringBootApplication,
 * que já declara @EntityScan e @EnableJpaRepositories apontando para o módulo 'core'.
 * Não é necessário redeclarar essas anotações aqui — fazê-lo causaria
 * BeanDefinitionOverrideException por registro duplicado do CourseRepository.
 */
@DataJpaTest
@DisplayName("CourseRepository - testes de integração com banco em memória H2")
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("deve salvar um curso e recuperá-lo pelo ID")
    void deve_salvar_e_recuperar_curso_pelo_id() {
        // Arrange
        var novoCurso = Course.builder().title("Arquitetura de Microserviços com Spring Boot").build();

        // Act
        Course salvo = courseRepository.save(novoCurso);
        var encontrado = courseRepository.findById(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isNotNull();
        assertThat(encontrado.get().getTitle()).isEqualTo("Arquitetura de Microserviços com Spring Boot");
    }

    @Test
    @DisplayName("deve listar todos os cursos com paginação respeitando tamanho da página")
    void deve_listar_cursos_com_paginacao_respeitando_tamanho_da_pagina() {
        // Arrange
        courseRepository.save(Course.builder().title("Curso 1").build());
        courseRepository.save(Course.builder().title("Curso 2").build());
        courseRepository.save(Course.builder().title("Curso 3").build());

        // Act
        Page<Course> pagina = courseRepository.findAll(PageRequest.of(0, 2));

        // Assert
        assertThat(pagina.getTotalElements()).isEqualTo(3);
        assertThat(pagina.getContent()).hasSize(2);
        assertThat(pagina.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("deve retornar segunda página corretamente quando há mais registros do que o tamanho da página")
    void deve_retornar_segunda_pagina_corretamente() {
        // Arrange
        courseRepository.save(Course.builder().title("Curso 1").build());
        courseRepository.save(Course.builder().title("Curso 2").build());
        courseRepository.save(Course.builder().title("Curso 3").build());

        // Act
        Page<Course> segundaPagina = courseRepository.findAll(PageRequest.of(1, 2));

        // Assert
        assertThat(segundaPagina.getContent()).hasSize(1);
        assertThat(segundaPagina.getContent().get(0).getTitle()).isEqualTo("Curso 3");
    }

    @Test
    @DisplayName("deve retornar lista vazia quando banco não possui registros")
    void deve_retornar_vazio_quando_banco_nao_possui_registros() {
        // Act
        Page<Course> resultado = courseRepository.findAll(PageRequest.of(0, 10));

        // Assert
        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("deve deletar um curso existente e não encontrá-lo após a exclusão")
    void deve_deletar_curso_existente() {
        // Arrange
        var curso = courseRepository.save(Course.builder().title("Curso para Deletar").build());

        // Act
        courseRepository.deleteById(curso.getId());
        var resultado = courseRepository.findById(curso.getId());

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("deve persistir o título do curso sem alteração")
    void deve_persistir_titulo_do_curso_sem_alteracao() {
        // Arrange
        var titulo = "Testes Unitários e Mockito para Desenvolvedores Java";
        var curso = Course.builder().title(titulo).build();

        // Act
        var salvo = courseRepository.save(curso);

        // Assert
        assertThat(salvo.getTitle()).isEqualTo(titulo);
        assertThat(salvo.getId()).isNotNull().isPositive();
    }
}

