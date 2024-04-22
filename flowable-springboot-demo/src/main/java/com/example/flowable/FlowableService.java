package com.example.flowable;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FlowableService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public ProcessInstance startProcess(String processId,Map<String,Object> variables){
        return runtimeService.startProcessInstanceByKey(processId,variables);
    }

    @Transactional
    public List<Task> getTasks(String candidateGroup){
        return taskService.createTaskQuery().taskCandidateGroup(candidateGroup).list();
    }
}
