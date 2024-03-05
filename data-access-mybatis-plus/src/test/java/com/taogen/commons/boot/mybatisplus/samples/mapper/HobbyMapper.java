package com.taogen.commons.boot.mybatisplus.samples.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taogen.commons.boot.mybatisplus.samples.entity.Hobby;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author taogen
 */
@Mapper
public interface HobbyMapper extends BaseMapper<Hobby> {
}
