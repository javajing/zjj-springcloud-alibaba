package com.wh.zjj.client.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wh.zjj.api.dto.param.DeptParam;
import com.wh.zjj.api.dto.result.DeptDto;
import com.wh.zjj.api.service.DeptDubboService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zjj
 */
@RestController
public class ClientController {

    @Reference(check = false, timeout = 10000, retries = 0)
    private DeptDubboService deptDubboService;

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public DeptDto get(@PathVariable("id") Long id) {
        DeptDto deptDto = this.deptDubboService.get(id);
        return deptDto;
    }

    @RequestMapping(value = "/consumer/dept/all")
    public List<DeptDto> list() {
        return this.deptDubboService.list();
    }

    @RequestMapping(value = "/consumer/dept/add")
    public Object add(@RequestBody DeptParam dept) {
        return this.deptDubboService.add(dept);
    }

}
