package br.com.alexandredev.core.repository;

import br.com.alexandredev.core.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
