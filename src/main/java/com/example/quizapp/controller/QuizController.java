package com.example.quizapp.controller;

import com.example.quizapp.model.*;
import com.example.quizapp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class QuizController {
    @Autowired private QuizService quizService;
    @Autowired private QuestionService questionService;
    @Autowired private AttemptService attemptService;

    // show create-quiz page (asks for number of questions / title)
    @GetMapping("/generate")
    public String generatePage(HttpSession session){
        if(session.getAttribute("username") == null) return "redirect:/login";
        return "generate";
    }

    // create quiz then redirect to add questions page (with quizId)
    @PostMapping("/createQuiz")
    public String createQuiz(@RequestParam String title, HttpSession session){
        String username = (String) session.getAttribute("username");
        if(username == null) return "redirect:/login";
        Quiz q = new Quiz();
        q.setTitle(title);
        q.setCreatedBy(username);
        quizService.save(q);
        return "redirect:/addQuestion?quizId=" + q.getId();
    }

    // add question page
    @GetMapping("/addQuestion")
    public String addQuestionPage(@RequestParam Integer quizId, HttpSession session, Model model){
        if(session.getAttribute("username") == null) return "redirect:/login";
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz == null) return "redirect:/dashboard";
        List<Question> questions = questionService.getByQuizId(quizId);
        model.addAttribute("quizId", quizId);
        model.addAttribute("questions", questions);
        return "addQuestion";
    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(@RequestParam Integer quizId,
                               @RequestParam String questionText,
                               @RequestParam String type,
                               @RequestParam(required=false) String optionA,
                               @RequestParam(required=false) String optionB,
                               @RequestParam(required=false) String optionC,
                               @RequestParam(required=false) String optionD,
                               @RequestParam String correctAnswer){
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz == null) return "redirect:/dashboard";

        Question q = new Question();
        q.setQuiz(quiz);
        q.setQuestionText(questionText);
        q.setType(type);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer);
        questionService.save(q);

        return "redirect:/addQuestion?quizId=" + quizId;
    }

    @GetMapping("/previewQuiz")
    public String preview(@RequestParam Integer quizId, HttpSession session, Model model){
        if(session.getAttribute("username") == null) return "redirect:/login";
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz == null) return "redirect:/dashboard";
        List<Question> questions = questionService.getByQuizId(quizId);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        model.addAttribute("quizId", quizId);
        return "previewQuiz";
    }

    @PostMapping("/saveQuiz")
    public String saveQuiz(@RequestParam Integer quizId){
        quizService.markSaved(quizId);
        return "redirect:/quizSaved?quizId=" + quizId;
    }

    @GetMapping("/quizSaved")
    public String quizSaved(@RequestParam Integer quizId, Model model){
        String link = "/attend/" + quizId;
        model.addAttribute("quizLink", link);
        model.addAttribute("quizId", quizId);
        return "quizSaved";
    }

    // teacher view attempts
    @GetMapping("/viewResults")
    public String viewResults(@RequestParam Integer quizId, HttpSession session, Model model){
        if(session.getAttribute("username") == null) return "redirect:/login";
        List<Attempt> attempts = attemptService.findByQuizId(quizId);
        model.addAttribute("attempts", attempts);
        model.addAttribute("quizId", quizId);
        return "view_attempts";
    }

    @GetMapping("/quizAttempts/{quizId}")
    public String quizAttempts(@PathVariable Integer quizId, HttpSession session, Model model){
        if(session.getAttribute("username") == null) return "redirect:/login";
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz == null) return "redirect:/dashboard";
        List<Attempt> attempts = attemptService.findByQuizId(quizId);
        model.addAttribute("quiz", quiz);
        model.addAttribute("attempts", attempts);
        return "attempts";
    }

    @GetMapping("/deleteQuiz")
    public String deleteQuiz(@RequestParam Integer quizId, HttpSession session){
        String username = (String) session.getAttribute("username");
        if(username == null) return "redirect:/login";
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz != null && quiz.getCreatedBy().equals(username)){
            quizService.deleteById(quizId);
        }
        return "redirect:/dashboard";
    }

    // student side - enter name page
    @GetMapping("/attend/{quizId}")
    public String attendEnterName(@PathVariable Integer quizId, Model model){
        model.addAttribute("quizId", quizId);
        return "enterName";
    }

    @PostMapping("/startQuiz")
    public String startQuiz(@RequestParam Integer quizId, @RequestParam String studentName, Model model){
        Quiz quiz = quizService.findById(quizId).orElse(null);
        if(quiz == null) return "redirect:/dashboard";
        List<Question> questions = questionService.getByQuizId(quizId);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        model.addAttribute("quizId", quizId);
        model.addAttribute("studentName", studentName);
        return "attend";
    }

    // submit answers — map contains keys like q{questionId}
    @PostMapping("/submitQuiz")
    public String submitQuiz(@RequestParam Integer quizId,
                             @RequestParam String studentName,
                             @RequestParam Map<String,String> allParams,
                             Model model){
        // calculate score
        int score = questionService.calculateScore(quizId, allParams);
        int total = questionService.getByQuizId(quizId).size();

        // save attempt
        Attempt a = new Attempt();
        a.setQuiz(quizService.findById(quizId).orElse(null));
        a.setStudentName(studentName);
        a.setScore(score);
        attemptService.save(a);

        model.addAttribute("studentName", studentName);
        model.addAttribute("score", score);
        model.addAttribute("total", total);
        return "result";
    }
}
