package com.taogen.commons.boot.mybatisplus;

import com.taogen.commons.boot.mybatisplus.samples.entity.User;
import com.taogen.commons.boot.mybatisplus.samples.entity.UserRole;
import com.taogen.commons.boot.mybatisplus.samples.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelatedDataServiceTest extends BaseTest {
    @Autowired
    private UserService userService;

    @Test
    void setRelatedDataForList() {
        List<User> list = userService.list();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        RelatedDataService.setRelatedDataForList(list, User.class);
        assertNotNull(list.get(0).getDepartment());
        assertTrue(!CollectionUtils.isEmpty(list.get(0).getHobbyList()));
        assertTrue(!CollectionUtils.isEmpty(list.get(0).getArea()));
        assertTrue(!CollectionUtils.isEmpty(list.get(0).getRoles()));
        System.out.println(list);
    }

    @Test
    void saveOrUpdateMiddleTable() throws NoSuchFieldException {
        Integer userId = 1;
        Set<Integer> roleIds = new HashSet<>(Arrays.asList(1, 2));
        RelatedDataService.saveOrUpdateMiddleTable(userId, roleIds, UserRole.class, User::getRoles);
    }
}
