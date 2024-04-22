package com.example.flowable.controller;

import com.example.flowable.FlowableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TaskController {
    @Autowired
    private FlowableService flowableService;
    @Autowired
    ObjectMapper objectMapper;
    @RequestMapping(value = "/startProcess/{processId}")
    public  Map<String,Object> startProcess(@PathVariable(name = "processId") String processId, @RequestBody Map<String,Object> params) throws JsonProcessingException {
        ProcessInstance processInstance = flowableService.startProcess(processId,params);
        Map<String,Object> map = new HashMap<>();
        map.put("Id",processInstance.getId());
        map.put("name",processInstance.getName());
        map.put("BusinessKey",processInstance.getBusinessKey());
        map.put("BusinessStatus",processInstance.getBusinessStatus());
        map.put("ProcessDefinitionId",processInstance.getProcessDefinitionId());
        map.put("ProcessDefinitionKey",processInstance.getProcessDefinitionKey());
        map.put("ProcessDefinitionName",processInstance.getProcessDefinitionName());
        map.put("ProcessVariables",processInstance.getProcessVariables());
        map.put("Description",processInstance.getDescription());
        map.put("DeploymentId",processInstance.getDeploymentId());
        return map;
    }


    @RequestMapping(value = "/tasks/{candidateGroup}")
    public List<Map<String,Object>> getTasks(@PathVariable(name = "candidateGroup") String candidateGroup){
        List<Task> tasks = flowableService.getTasks(candidateGroup);
        List<Map<String,Object>> list = tasks.stream().map(task->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",task.getId());
            map.put("name",task.getName());
            map.put("Assignee",task.getAssignee());
            map.put("Category",task.getCategory());
            map.put("ProcessVariables",task.getProcessVariables());
            map.put("Description",task.getDescription());
            map.put("ProcessDefinitionId",task.getProcessDefinitionId());
            map.put("ProcessInstanceId",task.getProcessInstanceId());
            return map;
        }).collect(Collectors.toList());
        return list;
    }




}
