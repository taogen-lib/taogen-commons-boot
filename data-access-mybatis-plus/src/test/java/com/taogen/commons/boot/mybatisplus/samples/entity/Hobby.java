package com.taogen.commons.boot.mybatisplus.samples.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author taogen
 */
@Data
public class Hobby {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
}
