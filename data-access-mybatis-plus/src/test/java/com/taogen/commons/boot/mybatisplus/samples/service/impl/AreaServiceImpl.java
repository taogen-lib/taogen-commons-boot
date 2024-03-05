package com.taogen.commons.boot.mybatisplus.samples.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taogen.commons.boot.mybatisplus.samples.entity.Area;
import com.taogen.commons.boot.mybatisplus.samples.mapper.AreaMapper;
import com.taogen.commons.boot.mybatisplus.samples.service.AreaService;
import org.springframework.stereotype.Service;

/**
 * @author taogen
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
}
