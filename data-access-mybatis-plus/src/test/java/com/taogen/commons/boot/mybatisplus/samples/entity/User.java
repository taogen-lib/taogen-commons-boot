package com.taogen.commons.boot.mybatisplus.samples.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taogen.commons.boot.mybatisplus.relatedquery.IdName;
import com.taogen.commons.boot.mybatisplus.relatedquery.annotation.MiddleTable;
import com.taogen.commons.boot.mybatisplus.relatedquery.annotation.Related;
import com.taogen.commons.boot.mybatisplus.samples.service.*;
import lombok.Data;

import java.util.List;

/**
 * @author taogen
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
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

    @TableField(exist = false)
    @MiddleTable(middleService = UserRoleService.class, targetService = RoleService.class, middleFromIdColumn = "user_id", middleToIdColumn = "role_id")
    private List<Role> roles;
}
