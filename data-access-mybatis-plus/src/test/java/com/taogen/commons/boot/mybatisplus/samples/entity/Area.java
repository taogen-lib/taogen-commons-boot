package com.taogen.commons.boot.mybatisplus.samples.entity;

import lombok.Data;

/**
 * @author taogen
 */
@Data
public class Area {
    private Integer id;
    private String code;
    private String name;
    private Integer parentId;
}
