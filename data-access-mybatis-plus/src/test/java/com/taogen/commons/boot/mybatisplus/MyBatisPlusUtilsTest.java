package com.taogen.commons.boot.mybatisplus;

import com.taogen.commons.boot.mybatisplus.samples.service.AreaService;
import com.taogen.commons.boot.mybatisplus.samples.service.DepartmentService;
import com.taogen.commons.boot.mybatisplus.samples.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class MyBatisPlusUtilsTest extends BaseTest {

    @Test
    void queryExistRelatedFieldIdNames() {
        Map<Class, Set<String>> serviceToFields = new HashMap<>();
        serviceToFields.put(DepartmentService.class, new HashSet<>(Arrays.asList("deptId")));
        serviceToFields.put(AreaService.class, new HashSet<>(Arrays.asList("areaId")));
        List dept = MyBatisPlusUtils.queryExistRelatedFieldIdNames(UserService.class, "deptId", serviceToFields);
        assertNotNull(dept);
        assertTrue(!dept.isEmpty());
        log.debug("dept: {}", dept);
        List area = MyBatisPlusUtils.queryExistRelatedFieldIdNames(UserService.class, "areaId", serviceToFields);
        assertNotNull(area);
        assertTrue(!area.isEmpty());
        log.debug("area: {}", area);
    }
}
