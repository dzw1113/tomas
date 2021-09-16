package io.github.dzw1113.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/15 09:51
 **/
public class TomasAgentBoot {
    public static final String AGENT_APPLICATION = "io.github.dzw1113.agent.AgentApplication";
    public static final String MAIN = "main";
    public static TomasClassloader tomasClassloader;
    
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
        if (inst != null) {
            Class[] classes = inst.getAllLoadedClasses();
            inst.addTransformer(new ClassFileTransformer() {
                @Override
                public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                        ProtectionDomain protectionDomain, byte[] classfileBuffer)
                        throws IllegalClassFormatException {
                    if (className.contains("IndexController")) {
                        outputClazz(classfileBuffer, "IndexController");
                        System.out.println("IndexController done!!");
                    }
                    return classfileBuffer;
                }
            }, true);
        }
        File testControllerJar = new File("E:\\git\\asd1\\111\\tomas\\agent-starter\\target\\tomas-agent-boot-jar-with-dependencies.jar");
        final TomasClassloader agentLoader;
        try {
            agentLoader = getClassLoader(inst, testControllerJar);
            Class<?> agentBootClass = agentLoader.loadClass(AGENT_APPLICATION);
            Object main = agentBootClass.getMethod(MAIN, Instrumentation.class, String.class).invoke(null, inst, args);
            System.out.println("bootstrap has done!");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
    }
    
    private static TomasClassloader getClassLoader(Instrumentation inst, File jarFile) throws Throwable {
        return loadOrDefineClassLoader(jarFile);
    }
    
    private static TomasClassloader loadOrDefineClassLoader(File jarFile) throws Throwable {
        if (tomasClassloader == null) {
            tomasClassloader = new TomasClassloader(new URL[]{jarFile.toURI().toURL()});
        }
        return tomasClassloader;
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
