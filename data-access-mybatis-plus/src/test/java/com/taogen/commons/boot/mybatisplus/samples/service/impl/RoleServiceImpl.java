package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.Role;
import com.taogen.commons.boot.mybatisplus.samples.mapper.RoleMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
