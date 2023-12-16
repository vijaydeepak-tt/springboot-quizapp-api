package com.example.quizapp.controllers;

import com.example.quizapp.models.QuizQuestion;
import com.example.quizapp.models.QuizResponse;
import com.example.quizapp.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String title) {
        return quizService.createQuiz(category, numQ, title);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable("id") Integer quizId) {
        return quizService.getQuizQuestions(quizId);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<String> calculateResult(@PathVariable("id") Integer quizId, @RequestBody List<QuizResponse> quizResponseList) {
        return quizService.calculateResult(quizId, quizResponseList);
    }
}
