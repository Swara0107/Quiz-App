package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quizapp.model.Quiz;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByCreatedBy(String createdBy);
}
