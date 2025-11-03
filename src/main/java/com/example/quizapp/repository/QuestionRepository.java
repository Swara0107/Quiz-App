package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quizapp.model.Question;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuizId(Integer quizId);
}

