package io.github.dzw1113.agent.intercept;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MyControllerAdvice {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @AllArguments Object[] allArguments,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("ssss");
//        String id = null;
//        String seq = null;
//        // 获取request,判断是否有header
//        for (int i = 0; i < allArguments.length; i++) {
//            if (allArguments[i] instanceof HttpServletRequest) {
//                HttpServletRequest req = (HttpServletRequest) allArguments[i];
//                id = req.getHeader("id");
//                seq = req.getHeader("seq");
//            }
//        }
//        // 如果id和seq为空,记录到 Threadlocal中
//        Span span = new Span();
//        if (id == null) {
//            span.setId(UUID.randomUUID().toString());
//            span.setSeq("1");
//        } else {
//            span.setId(UUID.randomUUID().toString());
//            span.setPid(id);
//            span.setSeq(seq);
//        }
//        // 添加到当前线程的调用链中
//        TraceContext.push(span);
        try {
            // 原有函数执行
            return callable.call();
        } finally {
//            System.out.println("AGENT 拦截 WEB span = " + span);
            System.out.println("AGENT 拦截 WEB 花费时间:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

}
