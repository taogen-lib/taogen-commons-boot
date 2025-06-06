package com.taogen.commons.boot.mybatisplus.relatedquery;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taogen.commons.boot.SpringUtils;
import com.taogen.commons.boot.mybatisplus.relatedquery.annotation.MiddleTable;
import com.taogen.commons.boot.mybatisplus.relatedquery.annotation.Related;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.taogen.commons.datatypes.string.StringCaseUtils.snakeCaseToCamelCase;
import static java.util.stream.Collectors.toSet;

/**
 * Related Data Service
 * <p>
 * Warning
 * 1. The generic type of Map<K, V> should be <String, Object>.
 * 2. Get value from map should using map.get(String.valueOf(key))
 */
@Slf4j
public class RelatedDataService {

    public static <S, R, T> void saveOrUpdateMiddleTable(S id, Set<S> targetIds, Class<R> middleClass, SFunction<T, ?> annotationFieldName) throws NoSuchFieldException {
        LambdaMeta meta = LambdaUtils.extract(annotationFieldName);
        Class<?> instantiatedClass = meta.getInstantiatedClass();
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        if (CollectionUtils.isEmpty(targetIds)) {
            return;
        }
        Field declaredField = instantiatedClass.getDeclaredField(fieldName);
        MiddleTable middleTable = declaredField.getAnnotation(MiddleTable.class);
        IService<R> service = (IService<R>) SpringUtils.getBean(middleTable.middleService());
        QueryWrapper<R> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(middleTable.middleFromIdColumn(), id);
        List<R> middleList = service.list(queryWrapper);
        Set<S> toAddSet = targetIds;
        Set<S> toRemoveSet = null;
        if (middleList != null && !middleList.isEmpty()) {
            LinkedHashSet<S> oldSet = middleList.stream()
                    .map(item -> getObjectField(item, snakeCaseToCamelCase(middleTable.middleToIdColumn())))
                    .map(item -> (S) item)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            toRemoveSet = oldSet.stream().filter(item -> !targetIds.contains(item)).collect(toSet());
            toAddSet = targetIds.stream().filter(item -> !oldSet.contains(item)).collect(toSet());
        }
        if (toAddSet != null && !toAddSet.isEmpty()) {
            Collection<R> middleEntityList = toAddSet.stream()
                    .map(item -> {
                        try {
                            R middleEntity = middleClass.newInstance();
                            setObjectField(middleEntity, snakeCaseToCamelCase(middleTable.middleFromIdColumn()), id);
                            setObjectField(middleEntity, snakeCaseToCamelCase(middleTable.middleToIdColumn()), item);
                            return middleEntity;
                        } catch (InstantiationException | IllegalAccessException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
            service.saveBatch(middleEntityList);
        }
        if (toRemoveSet != null && !toRemoveSet.isEmpty()) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(middleTable.middleFromIdColumn(), id);
            queryWrapper.in(middleTable.middleToIdColumn(), toRemoveSet);
            service.remove(queryWrapper);
        }
    }

    public static void setRelatedDataForList(List<?> list, Class<?> cls) {
        if (list == null || list.isEmpty() || cls == null) {
            return;
        }

        // get all fields of the class and its parent class
        List<Field> fields = getAllFieldsOfClassAndItsParent(cls);
        if (fields == null || fields.isEmpty()) {
            return;
        }

        // get fields have @Related annotation
        List<Field> relatedFieldList = fields.stream()
                .filter(item -> item.isAnnotationPresent(Related.class))
                .collect(Collectors.toList());

        for (Field field : relatedFieldList) {
            try {
                setRelatedFieldForList(list, field);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        List<Field> middleTableFieldList = fields.stream()
                .filter(item -> item.isAnnotationPresent(MiddleTable.class))
                .collect(Collectors.toList());
        for (Field field : middleTableFieldList) {
            try {
                setMiddleTableFieldForList(list, field);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static void setMiddleTableFieldForList(List<?> list, Field field) {
        MiddleTable annotation = field.getAnnotation(MiddleTable.class);
        Set<Object> ids = new HashSet<>();
        for (Object obj : list) {
            Object id = getObjectField(obj, snakeCaseToCamelCase(annotation.idColumn()));
            ids.add(id);
        }
        IService<?> middleService = (IService) SpringUtils.getBean(annotation.middleService());
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.in(annotation.middleFromIdColumn(), ids);
        List<?> middleList = middleService.list();
        if (middleList == null || middleList.isEmpty()) {
            return;
        }
        Map<String, LinkedHashSet<String>> fromIdToToIds = new HashMap<>();
        Set<String> toIdSet = new HashSet<>();
        for (Object middle : middleList) {
            String fromId = String.valueOf(getObjectField(middle, snakeCaseToCamelCase(annotation.middleFromIdColumn())));
            String toId = String.valueOf(getObjectField(middle, snakeCaseToCamelCase(annotation.middleToIdColumn())));
            toIdSet.add(toId);
            LinkedHashSet<String> toIds = fromIdToToIds.get(fromId);
            if (toIds == null) {
                toIds = new LinkedHashSet<>();
                fromIdToToIds.put(fromId, toIds);
            }
            toIds.add(toId);
        }
        IService<?> targetService = (IService) SpringUtils.getBean(annotation.targetService());
        QueryWrapper targetQueryWrapper = new QueryWrapper<>();
        targetQueryWrapper.in(annotation.targetIdColumn(), toIdSet);
        List<?> targetList = targetService.list(targetQueryWrapper);
        if (targetList == null || targetList.isEmpty()) {
            return;
        }
        Map<String, Object> targetMap = new HashMap<>();
        for (Object target : targetList) {
            String id = String.valueOf(getObjectField(target, snakeCaseToCamelCase(annotation.targetIdColumn())));
            targetMap.put(id, target);
        }
        for (Object obj : list) {
            String fromId = String.valueOf(getObjectField(obj, snakeCaseToCamelCase(annotation.idColumn())));
            LinkedHashSet<String> toIds = fromIdToToIds.get(fromId);
            if (toIds == null || toIds.isEmpty()) {
                continue;
            }
            List<?> targetObjList = toIds.stream()
                    .map(targetMap::get)
                    .collect(Collectors.toList());
            setObjectField(obj, field.getName(), targetObjList);
        }
    }

    private static void setRelatedFieldForList(List<?> list, Field field) {
        Set<String> relatedFieldValueSet = getFieldValuesFromList(list, field);
        List<?> relatedEntityList = getRelatedEntityList(field.getAnnotation(Related.class), relatedFieldValueSet);
        setRelatedFieldDataToList(list, field, relatedEntityList);
    }

    /**
     * @param relatedAnnotation
     * @param relatedFieldValueSet
     * @return if RelatedType is LEVEL only return List<entityClass>, others RelatedType can return List<IdName> or List<entityClass>
     */
    private static List<?> getRelatedEntityList(Related relatedAnnotation, Set<String> relatedFieldValueSet) {
        if (relatedFieldValueSet == null || relatedFieldValueSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<?> relatedEntityList = null;
        IService<?> service = (IService<?>) SpringUtils.getBean(relatedAnnotation.serviceClass());
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if (IdName.class.equals(relatedAnnotation.returnType()) &&
                !Related.RelatedType.LEVEL.equals(relatedAnnotation.relatedType())) {
            queryWrapper.select(relatedAnnotation.idColumn(), relatedAnnotation.nameColumn());
        }
        queryWrapper.in(relatedAnnotation.idColumn(), relatedFieldValueSet);
        relatedEntityList = service.list(queryWrapper);
        if (relatedEntityList == null || relatedEntityList.isEmpty()) {
            return relatedEntityList;
        }
        if (Related.RelatedType.LEVEL.equals(relatedAnnotation.relatedType())) {
            Set<String> totalIds = relatedFieldValueSet;
            Set<String> parentIds = relatedEntityList.stream()
                    .map(item -> getObjectField(item, snakeCaseToCamelCase(relatedAnnotation.parentIdColumn())))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            parentIds.removeAll(totalIds);
            totalIds.addAll(parentIds);
            while (parentIds != null && !parentIds.isEmpty()) {
                queryWrapper = new QueryWrapper<>();
                queryWrapper.in(relatedAnnotation.idColumn(), parentIds);
                List parentEntityList = service.list(queryWrapper);
                if (parentEntityList != null) {
                    relatedEntityList.addAll(parentEntityList);
                    parentIds = (Set<String>) parentEntityList.stream()
                            .map(item -> getObjectField(item, snakeCaseToCamelCase(relatedAnnotation.parentIdColumn())))
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    parentIds.removeAll(totalIds);
                    totalIds.addAll(parentIds);
                }
            }

        }
        return relatedEntityList;
    }

    /**
     * @param list              table data entity list
     * @param field
     * @param relatedEntityList
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private static void setRelatedFieldDataToList(List<?> list, Field field, List<?> relatedEntityList) {
        if (list == null || list.isEmpty() || relatedEntityList == null || relatedEntityList.isEmpty()) {
            return;
        }
        Related relatedAnnotation = field.getAnnotation(Related.class);
        Map<String, Object> relatedEntityMap = new LinkedHashMap<>(relatedEntityList.size());
        for (Object relatedEntity : relatedEntityList) {
            relatedEntityMap.put(String.valueOf(getObjectField(relatedEntity, snakeCaseToCamelCase(relatedAnnotation.idColumn()))), relatedEntity);
        }
        for (Object entity : list) {
            Object relatedFieldValue = getObjectField(entity, relatedAnnotation.relatedFieldName());
            if (relatedFieldValue != null) {
                if (Related.RelatedType.SINGLE.equals(relatedAnnotation.relatedType())) {
                    Object fieldValue = relatedEntityMap.get(String.valueOf(relatedFieldValue));
                    if (IdName.class.equals(relatedAnnotation.returnType())) {
                        IdName idName = new IdName();
                        setIdNameFields(idName, fieldValue, relatedAnnotation);
                        fieldValue = idName;
                    }
                    setObjectField(entity, field.getName(), fieldValue);
                } else if (Related.RelatedType.MULTIPLE.equals(relatedAnnotation.relatedType())) {
                    Set<String> relatedFieldValues = Arrays.stream(relatedFieldValue.toString().split(","))
                            .map(String::trim)
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    if (relatedFieldValues == null || relatedFieldValues.isEmpty()) {
                        return;
                    }
                    List<?> fieldValue = relatedFieldValues.stream()
                            .map(item -> {
                                Object itemValue = relatedEntityMap.get(String.valueOf(item));
                                if (IdName.class.equals(relatedAnnotation.returnType())) {
                                    IdName idName = new IdName();
                                    setIdNameFields(idName, itemValue, relatedAnnotation);
                                    itemValue = idName;
                                }
                                return itemValue;
                            })
                            .collect(Collectors.toList());
                    setObjectField(entity, field.getName(), fieldValue);
                } else if (Related.RelatedType.LEVEL.equals(relatedAnnotation.relatedType())) {
                    List<Object> fieldValue = new ArrayList<>();
                    Object fieldItem = relatedEntityMap.get(String.valueOf(relatedFieldValue));
                    while (fieldItem != null) {
                        fieldValue.add(fieldItem);
                        fieldItem = relatedEntityMap.get(String.valueOf(getObjectField(fieldItem, snakeCaseToCamelCase(relatedAnnotation.parentIdColumn()))));
                    }
                    setObjectField(entity, field.getName(), fieldValue);
                }
            }
        }
    }

    private static void setIdNameFields(IdName idName, Object fieldValue, Related relatedAnnotation) {
        try {
            Field id = IdName.class.getDeclaredField("id");
            if (id.getType().equals(Long.class)) {
                setObjectField(idName, "id",
                        parseIntegerToLong(getObjectField(fieldValue, snakeCaseToCamelCase(relatedAnnotation.idColumn())), null));
            }
            if (id.getType().equals(Integer.class)) {
                setObjectField(idName, "id",
                        parseLongToInteger(getObjectField(fieldValue, snakeCaseToCamelCase(relatedAnnotation.idColumn())), null));
            }
            if (id.getType().equals(String.class)) {
                setObjectField(idName, "id",
                        Objects.toString(getObjectField(fieldValue, snakeCaseToCamelCase(relatedAnnotation.idColumn()))));
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        setObjectField(idName, "name",
                getObjectField(fieldValue, snakeCaseToCamelCase(relatedAnnotation.nameColumn())));

    }


    /**
     * @param list
     * @param field
     * @return You can consider to return two data types:
     * 1) HashSet: for higher efficiency.
     * 2) LinkedHashSet: for keeping insertion order.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private static Set<String> getFieldValuesFromList(List<?> list, Field field) {
        if (list == null || list.isEmpty()) {
            return Collections.emptySet();
        }
        Related relatedAnnotation = field.getAnnotation(Related.class);
        Set<String> ids = new LinkedHashSet<>();
        for (Object obj : list) {
            Object relatedFieldValue = getObjectField(obj, relatedAnnotation.relatedFieldName());
            if (relatedFieldValue != null) {
                if (Related.RelatedType.SINGLE.equals(relatedAnnotation.relatedType()) ||
                        Related.RelatedType.LEVEL.equals(relatedAnnotation.relatedType())) {
                    ids.add(relatedFieldValue.toString());
                } else if (Related.RelatedType.MULTIPLE.equals(relatedAnnotation.relatedType())) {
                    ids.addAll(Arrays.stream(relatedFieldValue.toString().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList()));
                }
            }
        }
        return ids != null && !ids.isEmpty() ? ids : Collections.emptySet();
    }

    public static Object getObjectField(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        Object returnObject = null;
        String methodName = "get" + firstLetterToUpperCase(fieldName);
        Class<?> classNode = obj.getClass();
        boolean isDone = false;
        while (!Object.class.equals(classNode)) {
            List<Method> methodList = Arrays.asList(classNode.getDeclaredMethods());
            for (Method method : methodList) {
                if (method.getName().equals(methodName)) {
                    try {
                        returnObject = method.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    isDone = true;
                    break;
                }
            }
            if (isDone) {
                break;
            }
            classNode = classNode.getSuperclass();
        }
        return returnObject;
    }

    public static void setObjectField(Object obj, String fieldName, Object fieldValue) {
        if (obj == null) {
            return;
        }
        String methodName = "set" + firstLetterToUpperCase(fieldName);
        Class<?> classNode = obj.getClass();
        boolean isDone = false;
        while (!Object.class.equals(classNode)) {
            List<Method> methodList = Arrays.asList(classNode.getDeclaredMethods());
            for (Method method : methodList) {
                if (method.getName().equals(methodName)) {
                    try {
                        method.invoke(obj, fieldValue);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    isDone = true;
                    break;
                }
            }
            if (isDone) {
                break;
            }
            classNode = classNode.getSuperclass();
        }
    }

    public static String firstLetterToUpperCase(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    private static List<Field> getAllFieldsOfClassAndItsParent(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        Class<?> node = cls;
        while (!Object.class.equals(node.getSuperclass())) {
            node = node.getSuperclass();
            fields.addAll(Arrays.asList(node.getDeclaredFields()));
        }
        return fields;
    }

    private static List<Method> getAllMethodsOfClassAndItsParent(Class<?> cls) {
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
        Class<?> node = cls;
        while (!Object.class.equals(node.getSuperclass())) {
            node = node.getSuperclass();
            methods.addAll(Arrays.asList(node.getDeclaredMethods()));
        }
        return methods;
    }

    private static Long parseIntegerToLong(Object intValue, Long defaultLongValue) {
        if (intValue == null) {
            return defaultLongValue;
        }
        return Long.parseLong(String.valueOf(intValue));
    }

    private static Integer parseLongToInteger(Object longValue, Integer defaultIntValue) {
        if (longValue == null) {
            return defaultIntValue;
        }
        return Integer.parseInt(String.valueOf(longValue));
    }
}
