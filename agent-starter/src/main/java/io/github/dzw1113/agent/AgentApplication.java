package io.github.dzw1113.agent;

import java.lang.instrument.Instrumentation;

import io.github.dzw1113.agent.client.NettyClient;
import io.github.dzw1113.common.model.SocketRequest;
import io.netty.channel.Channel;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 13:31
 **/
public class AgentApplication {
    
    private static AgentApplication agentApplication;
    private Instrumentation instrumentation;
    private String args;
    private NettyClient client;
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
        if (instrumentation != null && client != null && channel != null) {
            System.out.println("已建立连接！！");
//            channel.writeAndFlush(“已建立连接！”)
            return true;
        }
        NettyClient client = new NettyClient("127.0.0.1", 12345);
        //启动client服务
        try {
            client.start();
            Channel channel = client.getChannel();
            channel.writeAndFlush(new SocketRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
}
