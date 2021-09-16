package io.github.dzw1113.core.attach;

import java.util.List;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * @description:https://github.com/arodchen/MaxSim
 * @author: dzw
 * @date: 2021/09/14 13:07
 **/
public final class AttachVirtualMachine {
    
    private static AttachVirtualMachine ins = null;
    
    private AttachVirtualMachine() {
    }
    
    public VirtualMachine attachJvm(String pid) {
        VirtualMachine vm = null;
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            if (descriptor.id().equals(pid)) {
                virtualMachineDescriptor = descriptor;
                break;
            }
        }
        try {
            if (null == virtualMachineDescriptor) { // 使用 attach(String pid) 这种方式
                vm = VirtualMachine.attach(pid);
            } else {
                vm = VirtualMachine.attach(virtualMachineDescriptor);
            }
            vm.loadAgent("E:\\git\\asd1\\111\\tomas\\agent\\target\\tomas-agent-jar-with-dependencies.jar", "tomas");
            vm.detach();
        } catch (Exception e) {
            e.printStackTrace();
            return vm;
        }
        return vm;
    }
    
    public List<VirtualMachineDescriptor> listAllJvm() {
        return VirtualMachine.list();
    }
    
    public static AttachVirtualMachine getInstance() {
        if (ins == null) {
            synchronized (AttachVirtualMachine.class) {
                if (ins == null) {
                    ins = new AttachVirtualMachine();
                }
            }
        }
        return ins;
    }
    
}
