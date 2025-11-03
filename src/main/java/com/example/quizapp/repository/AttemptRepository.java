package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quizapp.model.Attempt;
import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {
    List<Attempt> findByQuizId(Integer quizId);
}

