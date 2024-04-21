package org.example.process;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;


public class CallExternalSystemDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("调用外部接口 , 请假信息---- 请假人："+execution.getVariable("employee")
                +" 请假天数："+execution.getVariable("holidaysCount")
                +" 理由："+execution.getVariable("reason"));
    }
}
