package com.taogen.commons.boot.mybatisplus;

import com.taogen.commons.boot.mybatisplus.samples.entity.User;
import com.taogen.commons.boot.mybatisplus.samples.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest(classes = AppTest.class)
@ExtendWith(SpringExtension.class)
class RelatedDataServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void setRelatedDataForList() {
        List<User> list = userService.list();
        RelatedDataService.setRelatedDataForList(list, User.class);
        System.out.println(list);
    }
}
