package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.User;
import com.taogen.commons.boot.mybatisplus.samples.mapper.UserMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
