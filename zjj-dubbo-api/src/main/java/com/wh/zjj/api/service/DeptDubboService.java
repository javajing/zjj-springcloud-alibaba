package com.wh.zjj.api.service;


import com.wh.zjj.api.dto.param.DeptParam;
import com.wh.zjj.api.dto.result.DeptDto;
import java.util.List;

/**
 * @author zjj
 */
public interface DeptDubboService {

    DeptDto get(long id);

    List<DeptDto> list();

    boolean add(DeptParam dept);

}
