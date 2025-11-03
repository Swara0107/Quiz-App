package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quizapp.repository.UserRepository;
import com.example.quizapp.model.User;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    public User save(User u){ return userRepository.save(u); }
    public User findByUsername(String username){ return userRepository.findByUsername(username); }
}

