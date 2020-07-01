package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleWorkflowService {

    private RuntimeService runtimeService;
  
    private TaskService taskService;
 
//    @Transactional
    public void startProcess(final Map article) {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.get("author"));
        variables.put("url", article.get("url"));
        runtimeService.startProcessInstanceByKey("articleReview", variables);
    }
  
//    @Transactional
    public List<Map> getTasks(final String assignee) {
        final List<Task> tasks = taskService.createTaskQuery()
          .taskCandidateGroup(assignee)
          .list();
        return tasks.stream()
          .map(task -> {
              final Map<String, Object> variables = taskService.getVariables(task.getId());
              return variables;
          })
          .collect(Collectors.toList());
    }
  
//    @Transactional
    public void submitReview(final Map approval) {
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approval.get("status"));
        taskService.complete((String) approval.get("id"), variables);
    }
}