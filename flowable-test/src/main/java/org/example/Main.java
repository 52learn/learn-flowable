package org.example;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.ProcessInstanceHistoryLogImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable-test;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/holiday-request.bpmn20.xml")
                //.addClasspathResource("bpmn/intro.bpmn20.xml")
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("找到流程定义 name:"+processDefinition.getName()+",id:"+processDefinition.getId());


        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> variables = new HashMap<>();
        variables.put("employee","kim");
        variables.put("holidaysCount",3);
        variables.put("reason","请假探亲");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday-request",variables);

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have CandidateGroup[managers] tasks : "+tasks.size());

        for(int i = 0 ;i<tasks.size(); i++){
            System.out.println("CandidateGroup[managers] task "+i+": "+tasks.get(i).getName());
        }


        Map<String,Object> processVariables = taskService.getVariables(tasks.get(0).getId());
        System.out.println(processVariables.get("employee")+" 想要：" + processVariables.get("holidaysCount")+" 天假期，您同意吗？ (y/n)");


        Scanner scanner = new Scanner(System.in);
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<>();
        variables.put("approved",approved);
        taskService.complete(tasks.get(0).getId(),variables);


        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for(HistoricActivityInstance activityInstance : activityInstances){
            System.out.println(activityInstance.getActivityId()+" took "
                    +activityInstance.getDurationInMillis()+" milliseconds");
        }
    }
}