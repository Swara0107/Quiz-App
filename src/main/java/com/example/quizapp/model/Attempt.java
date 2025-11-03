package com.example.quizapp.model;

import jakarta.persistence.*;

@Entity
public class Attempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String studentName;
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

