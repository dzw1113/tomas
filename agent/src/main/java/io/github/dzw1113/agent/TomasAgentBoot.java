package io.github.dzw1113.agent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * @description:代理启动入口
 * @author: dzw
 * @date: 2021/09/15 09:51
 **/
public class TomasAgentBoot {
    public static final String AGENT_APPLICATION = "io.github.dzw1113.agent.AgentApplication";
    public static final String MAIN = "main";
    
    public static void premain(String args, Instrumentation inst) {
        System.out.println("=============from premain2 ===================");
        main(args, inst);
    }
    
    public static void premain(String args) {
        System.out.println("=============from premain1 ===================");
        main(args, null);
    }
    
    public static void agentmain(String args) {
        System.out.println("=============from agentmain1 ===================");
        main(args, null);
    }
    
    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("=============from agentmain2 ===================");
        main(args, inst);
    }
    
    
    private static synchronized void main(String args, final Instrumentation inst) {
        System.out.println("==============进入代理" + args);
        if (System.getProperty("tomas agent start") != null) {
            return;
        }
        System.setProperty("tomas agent start", "true");
    
        if (inst != null) {
            try {
                inst.appendToBootstrapClassLoaderSearch(new JarFile("E:\\git\\asd1\\111\\tomas\\agent-starter\\target\\tomas-agent-boot-jar-with-dependencies.jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class<?> agentBootClass = TomasAgentBoot.class.getClassLoader().loadClass(AGENT_APPLICATION);
            Object main = agentBootClass.getMethod(MAIN, Instrumentation.class, String.class).invoke(null, inst, args);
            System.out.println(main);
            System.out.println("bootstrap has done!");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    private static String outputClazz(byte[] bytes, String className) {
        FileOutputStream out = null;
        String pathName = null;
        try {
            System.out.println(TomasAgentBoot.class.getResource("/").getPath());
            pathName = TomasAgentBoot.class.getResource("/").getPath() + className + ".class";
            out = new FileOutputStream(pathName);
            System.out.println("类输出路径：" + pathName);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathName;
    }
    
    
}
