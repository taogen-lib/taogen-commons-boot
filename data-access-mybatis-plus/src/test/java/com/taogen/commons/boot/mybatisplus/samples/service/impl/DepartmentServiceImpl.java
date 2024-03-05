package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.Department;
import com.taogen.commons.boot.mybatisplus.samples.mapper.DepartmentMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {
}
