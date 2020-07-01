package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ArticleWorkflowController {
    private ArticleWorkflowService service;
  
    @PostMapping("/submit")
    public void submit(@RequestBody Map article) {
        service.startProcess(article);
    }
  
    @GetMapping("/tasks")
    public List<Map> getTasks(@RequestParam String assignee) {
    	System.out.println("##########"+assignee);
        return service.getTasks(assignee);
    }
  
    @PostMapping("/review")
    public void review(@RequestBody Map approval) {
        service.submitReview(approval);
    }
}