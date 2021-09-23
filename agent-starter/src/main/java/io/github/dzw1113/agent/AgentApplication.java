package io.github.dzw1113.agent;

import java.lang.instrument.Instrumentation;

import io.github.dzw1113.agent.client.Client;
import io.github.dzw1113.agent.intercept.MyControllerAdvice;
import io.netty.channel.Channel;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 13:31
 **/
public class AgentApplication {
    
    private static AgentApplication agentApplication;
    private ResettableClassFileTransformer resettableClassFileTransformer;
    private Instrumentation instrumentation;
    private String args;
    private Client client;
    private Channel channel;
    
    private AgentApplication() {
    }
    
    private AgentApplication(Instrumentation instrumentation, String args) {
        this.instrumentation = instrumentation;
        this.args = args;
    }
    
    public synchronized static AgentApplication main(Instrumentation instrumentation, String args) {
        if (agentApplication == null) {
            agentApplication = new AgentApplication(instrumentation, args);
        }
        agentApplication.init();
        return agentApplication;
    }
    
    public synchronized boolean init() {
        if (instrumentation != null && client != null) {
            return true;
        }
        installOn();
        //启动通讯服务
        client = Client.getInstance();
        client.startConnect();
        return true;
    }
    
    
    public void installOn() {
        AgentBuilder.Transformer transformerWeb = (builder, typeDescription, classLoader, module) -> {
            return builder
                    .method(ElementMatchers.any()) // 拦截任意方法
                    .intercept(MethodDelegation.to(MyControllerAdvice.class)); // 委托
        };
        
        // 拦截 Web
        AgentBuilder agentBuilder = new AgentBuilder
                .Default()
                .type(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.web.bind.annotation.RestController"))) // 指定需要拦截的类
                .transform(transformerWeb);
        // 注入 inst
        resettableClassFileTransformer = agentBuilder.installOn(instrumentation);
    }
    
}
