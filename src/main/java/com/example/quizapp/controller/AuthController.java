package com.example.quizapp.controller;

import com.example.quizapp.model.User;
import com.example.quizapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired private UserService userService;

    @GetMapping({"/","/login"})
    public String loginPage(Model model){
        model.addAttribute("error", "");
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session){
        User u = userService.findByUsername(username);
        if(u != null && u.getPassword().equals(password)){
            session.setAttribute("username", username);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("error", "");
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@RequestParam String username, @RequestParam String password, Model model){
        if(userService.findByUsername(username)!=null){
            model.addAttribute("error","Username exists");
            return "signup";
        }
        User u = new User(); u.setUsername(username); u.setPassword(password);
        userService.save(u);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){ session.invalidate(); return "redirect:/login"; }
}
