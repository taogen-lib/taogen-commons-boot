package com.taogen.commons.boot.mybatisplus.samples.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taogen.commons.boot.mybatisplus.samples.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author taogen
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
