package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.Hobby;
import com.taogen.commons.boot.mybatisplus.samples.mapper.HobbyMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.HobbyService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class HobbyServiceImpl extends ServiceImpl<HobbyMapper, Hobby> implements HobbyService {
}
