package com.taogen.commons.boot.mybatisplus.samples.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taogen.commons.boot.mybatisplus.IdName;
import com.taogen.commons.boot.mybatisplus.Related;
import com.taogen.commons.boot.mybatisplus.samples.service.AreaService;
import com.taogen.commons.boot.mybatisplus.samples.service.DepartmentService;
import com.taogen.commons.boot.mybatisplus.samples.service.HobbyService;
import lombok.Data;

import java.util.List;

/**
 * @author taogen
 */
@Data
@TableName("user")
public class User {
    private Integer id;

    private String name;

    private Integer age;

    private Integer deptId;

    @TableField(exist = false)
    @Related(relatedType = Related.RelatedType.SINGLE, serviceClass = DepartmentService.class, returnType = Department.class, relatedFieldName = "deptId")
    private Department department;

    private String hobbyIds;

    @TableField(exist = false)
    @Related(relatedType = Related.RelatedType.MULTIPLE, serviceClass = HobbyService.class, returnType = IdName.class, relatedFieldName = "hobbyIds")
    private List<IdName> hobbyList;

    private Integer areaId;

    @TableField(exist = false)
    @Related(relatedType = Related.RelatedType.LEVEL, serviceClass = AreaService.class, returnType = Area.class, relatedFieldName = "areaId")
    private List<Area> area;
}
