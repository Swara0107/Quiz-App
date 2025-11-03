package com.example.quizapp.controller;

import com.example.quizapp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.example.quizapp.model.Quiz;

@Controller
public class DashboardController {
    @Autowired private QuizService quizService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        if(username == null) return "redirect:/login";
        List<Quiz> quizzes = quizService.findByCreator(username);
        model.addAttribute("username", username);
        model.addAttribute("quizzes", quizzes);
        return "dashboard";
    }
}

