package com.taogen.commons.boot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taogen.commons.boot.SpringUtils;
import com.taogen.commons.datatypes.string.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author taogen
 */
public class MyBatisPlusUtils {
    protected static List queryExistRelatedFieldIdNames(Class serviceName,
                                                        String field,
                                                        Map<Class, Set<String>> serviceToFields) {
        IService relatedService = null;
        for (Class key : serviceToFields.keySet()) {
            if (serviceToFields.get(key).contains(field)) {
                relatedService = (IService) SpringUtils.getBean(key);
                break;
            }
        }
        if (relatedService == null) {
            return Collections.emptyList();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("distinct " + field);
        queryWrapper.isNotNull(field);
        IService<?> service = (IService<?>) SpringUtils.getBean(serviceName);
        List<?> entitylist = service.list();
        if (CollectionUtils.isEmpty(entitylist)) {
            return Collections.emptyList();
        }
        List<String> fieldList = new ArrayList<>();
        entitylist.stream()
                .map(item -> RelatedDataService.getObjectField(item, field))
                .map(Objects::toString)
                .filter(StringUtils::isNotBlank)
                .forEach(item -> {
                    List<String> strings = StringUtils.splitToStringList(item.toString(), ",");
                    if (!CollectionUtils.isEmpty(strings)) {
                        fieldList.addAll(strings);
                    }
                });
        List<?> userList = relatedService.list(new QueryWrapper<>()
                .select("id", "name")
                .in("id", fieldList));
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }
        List<IdName> idNames = userList.stream().map(item ->
                        new IdName(Long.valueOf(Objects.toString(RelatedDataService.getObjectField(item, "id"))),
                                (String) RelatedDataService.getObjectField(item, "name")))
                .collect(Collectors.toList());
        return idNames;
    }

}
