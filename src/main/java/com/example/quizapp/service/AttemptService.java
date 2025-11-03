package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quizapp.repository.AttemptRepository;
import com.example.quizapp.model.Attempt;

import java.util.List;

@Service
public class AttemptService {
    @Autowired private AttemptRepository attemptRepository;
    public Attempt save(Attempt a){ return attemptRepository.save(a); }
    public List<Attempt> findByQuizId(Integer quizId){ return attemptRepository.findByQuizId(quizId); }
}
