package com.taogen.commons.boot;

import com.taogen.commons.boot.samples.MyBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = AppTest.class)
@ExtendWith(SpringExtension.class)
class SpringUtilsTest {

    @Test
    void getBean() {
        MyBean myBean = SpringUtils.getBean("myBean");
        assertEquals("Taogen", myBean.getName());
    }

    @Test
    void getBean2() {
//        MyBean myBean = SpringUtils.getBean(MyBean.class);
//        assertEquals("Taogen", myBean.getName());
        assertThrows(NoUniqueBeanDefinitionException.class, () -> SpringUtils.getBean(MyBean.class));
    }

    @Test
    void containsBean() {
        boolean contains = SpringUtils.containsBean("myBean");
        assertTrue(contains);
    }

    @Test
    void isSingleton() {
        boolean isSingleton = SpringUtils.isSingleton("myBean");
        assertTrue(isSingleton);
    }

    @Test
    void getType() {
        Class<?> type = SpringUtils.getType("myBean");
        assertEquals(MyBean.class, type);
    }

    @Test
    void getAliases() {
        String[] aliases = SpringUtils.getAliases("myBean");
        assertEquals(0, aliases.length);
        String[] alias1s = SpringUtils.getAliases("myBeanAlias2");
        System.out.println(Arrays.toString(alias1s));
        assertEquals(1, alias1s.length);
        assertEquals("myBeanAlias1", alias1s[0]);
    }

    @Test
    void getAopProxy() {
    }

    @Test
    void getActiveProfiles() {
        String[] activeProfiles = SpringUtils.getActiveProfiles();
        assertEquals(1, activeProfiles.length);
        assertEquals("test", activeProfiles[0]);
    }

    @Test
    void getActiveProfile() {
        String activeProfile = SpringUtils.getActiveProfile();
        assertEquals("test", activeProfile);
    }
}
