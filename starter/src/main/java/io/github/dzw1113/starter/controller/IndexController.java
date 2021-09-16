package io.github.dzw1113.starter.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.tools.attach.VirtualMachineDescriptor;

import io.github.dzw1113.common.model.HttpResult;
import io.github.dzw1113.core.attach.AttachVirtualMachine;
import io.github.dzw1113.starter.controller.vo.VirtualMachineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: index
 * @author: dzw
 * @date: 2021/09/16 10:24
 **/
@Controller
@Api(tags = "首页")
public class IndexController {
    
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        System.out.println("index");
        return "返回index";
    }
    
    
    @RequestMapping(value = {"/attach"})
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "附加监控")
    public HttpResult attachJvm(Integer pid) {
        AttachVirtualMachine.getInstance().attachJvm(String.valueOf(pid));
        return HttpResult.ok();
    }
    
    @RequestMapping(value = {"/list"})
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "获取所有进程")
    public HttpResult list() {
        List<VirtualMachineDescriptor> virtualMachineDescriptors =  AttachVirtualMachine.getInstance().listAllJvm();
        return HttpResult.ok(virtualMachineDescriptors.stream().map(virtualMachineDescriptor -> {
            VirtualMachineVo virtualMachineVo = new VirtualMachineVo();
            virtualMachineVo.setId(virtualMachineDescriptor.id());
            virtualMachineVo.setDisplayName(virtualMachineDescriptor.displayName());
            return virtualMachineVo;
        }));
    }
    
}
