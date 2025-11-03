package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.repository.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired private QuizRepository quizRepository;
    public Quiz save(Quiz q){ return quizRepository.save(q); }
    public List<Quiz> findByCreator(String username){ return quizRepository.findByCreatedBy(username); }
    public List<Quiz> findAll(){ return quizRepository.findAll(); }
    public Optional<Quiz> findById(Integer id){ return quizRepository.findById(id); }
    public void markSaved(Integer id){
        quizRepository.findById(id).ifPresent(q -> { q.setSaved(true); quizRepository.save(q); });
    }
    public void deleteById(Integer id){ quizRepository.deleteById(id); }
}
