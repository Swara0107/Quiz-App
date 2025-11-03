package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.model.Question;

import java.util.List;
import java.util.Map;

@Service
public class QuestionService {
    @Autowired private QuestionRepository questionRepository;

    public Question save(Question q){ return questionRepository.save(q); }
    public List<Question> getByQuizId(Integer quizId){ return questionRepository.findByQuizId(quizId); }

    // scoring logic: expects answers map keyed by "q{questionId}"
    public int calculateScore(Integer quizId, Map<String,String> answers){
        List<Question> questions = getByQuizId(quizId);
        int score = 0;
        for(Question q : questions){
            String key = "q" + q.getId();
            String studentAns = answers.getOrDefault(key,"").trim();
            if(studentAns.isEmpty()) continue;
            String type = q.getType() == null ? "MCQ" : q.getType();
            String correct = q.getCorrectAnswer() != null ? q.getCorrectAnswer().trim() : "";
            if(type.equalsIgnoreCase("MCQ")){
                String expected = "";
                switch(studentAns.toUpperCase()){
                    case "A": expected = q.getOptionA() != null ? q.getOptionA().trim() : ""; break;
                    case "B": expected = q.getOptionB() != null ? q.getOptionB().trim() : ""; break;
                    case "C": expected = q.getOptionC() != null ? q.getOptionC().trim() : ""; break;
                    case "D": expected = q.getOptionD() != null ? q.getOptionD().trim() : ""; break;
                }
                if(expected.equalsIgnoreCase(correct)) score++;
            } else if(type.equalsIgnoreCase("TrueFalse") || type.equalsIgnoreCase("ShortAnswer")){
                if(correct.equalsIgnoreCase(studentAns)) score++;
            }
        }
        return score;
    }
}

