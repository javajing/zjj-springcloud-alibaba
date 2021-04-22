package com.wh.zjj.provider.controller;

import com.wh.zjj.common.base.entity.Dept;
import com.wh.zjj.common.base.service.IDeptService;
import com.wh.zjj.common.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zjj
 */
@RestController
@Slf4j
public class DeptServerController {

    @Autowired
    private IDeptService deptService;
    private static final String DB = "1";

    @PostMapping(value = "/dept/add")
    public boolean add(@RequestBody Dept dept) {
        dept.setDbSource(DB);
        return deptService.save(dept);
    }

    @GetMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        log.info("i am 8001, id:{}", id);
        return deptService.getById(id);
    }

    @GetMapping(value = "/dept/list")
    public List<Dept> list() {
        try {
            UUIDUtil.insertTid();
            log.info("查询所有");
            return deptService.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UUIDUtil.removeTid();
        }
        return null;
    }
}
