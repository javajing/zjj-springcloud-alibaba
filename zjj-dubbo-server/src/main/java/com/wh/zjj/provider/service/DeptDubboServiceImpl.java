package com.wh.zjj.provider.service;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.wh.zjj.api.dto.param.DeptParam;
import com.wh.zjj.api.dto.result.DeptDto;
import com.wh.zjj.api.service.DeptDubboService;
import com.wh.zjj.common.base.entity.Dept;
import com.wh.zjj.common.base.service.IDeptService;
import com.wh.zjj.common.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author zjj
 */
@Service(interfaceClass = DeptDubboService.class)  //属于Dubbo的@Service注解，非Spring  作用：暴露服务, interfaceClass可以省略不写
@Component
public class DeptDubboServiceImpl implements DeptDubboService {

    @Autowired
    private IDeptService deptService;

    @Override
    public DeptDto get(long id) {
        DeptDto deptDto = new DeptDto();
        Dept dept = deptService.getById(id);
        BeanUtil.copyProperties(dept, deptDto);
        return deptDto;
    }

    @Override
    public List<DeptDto> list() {
        List<Dept> list = deptService.list();
        List<DeptDto> trans = BeanUtils.trans(DeptDto.class, list);
        return trans;
    }

    @Override
    public boolean add(DeptParam dept) {
        Dept trans = BeanUtils.trans(Dept.class, dept);
        return deptService.save(trans);
    }
}
