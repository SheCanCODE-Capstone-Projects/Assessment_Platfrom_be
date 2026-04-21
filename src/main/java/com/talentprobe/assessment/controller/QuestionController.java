package com.talentprobe.assessment.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @GetMapping
    public String getAllQuestions() {
        return "List of questions (not implemented yet)";
    }

    @PostMapping
    public String createQuestion() {
        return "Question created (not implemented yet)";
    }
}