package com.taogen.commons.boot.mybatisplus.samples.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taogen.commons.boot.mybatisplus.samples.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author taogen
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
