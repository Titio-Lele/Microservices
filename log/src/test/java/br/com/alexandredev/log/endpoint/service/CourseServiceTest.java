package br.com.alexandredev.log.endpoint.service;

import br.com.alexandredev.core.model.Course;
import br.com.alexandredev.core.repository.CourseRepository;
import org.bouncycastle.util.Iterable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService - testes unitários")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("deve retornar lista de cursos quando repositório possui dados")
    void deve_retornar_lista_de_cursos_quando_repositorio_possui_dados() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var courses = List.of(
                Course.builder().id(1L).title("Arquitetura de Microserviços com Spring Boot").build(),
                Course.builder().id(2L).title("Docker e Orquestração de Containers").build()
        );
        Page<Course> page = new PageImpl<>(courses, pageable, courses.size());
        when(courseRepository.findAll(pageable)).thenReturn(page);

        // Act
        var result = courseService.list(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("deve retornar lista vazia quando repositório não possui dados")
    void deve_retornar_lista_vazia_quando_repositorio_nao_possui_dados() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        Page<Course> emptyPage = new PageImpl<>(Collections.emptyList());
        when(courseRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        var result = courseService.list(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(courseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("deve repassar o pageable recebido ao repositório sem alteração")
    void deve_repassar_pageable_recebido_ao_repositorio_sem_alteracao() {
        // Arrange
        var pageable = PageRequest.of(2, 5);
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // Act
        courseService.list(pageable);

        // Assert
        verify(courseRepository).findAll(pageable);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    @DisplayName("deve chamar o repositório exatamente uma vez por requisição")
    void deve_chamar_repositorio_exatamente_uma_vez_por_requisicao() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // Act
        courseService.list(pageable);

        // Assert
        verify(courseRepository, times(1)).findAll(any(Pageable.class));
    }
}


