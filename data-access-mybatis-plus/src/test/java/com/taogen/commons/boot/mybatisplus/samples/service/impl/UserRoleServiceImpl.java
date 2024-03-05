package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.UserRole;
import com.taogen.commons.boot.mybatisplus.samples.mapper.UserRoleMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
