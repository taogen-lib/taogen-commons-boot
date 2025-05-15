package com.taogen.commons.boot.mybatisplus.annotation;

import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Related {
    RelatedType relatedType();

    /**
     * It's MyBatis Plus service subclass
     * You can use class of service Interface or service implementation
     *
     * @return
     */
    Class<? extends IService> serviceClass();

    /**
     * support {entityClass} or IdName.class
     *
     * @return
     */
    Class<?> returnType();

    String relatedFieldName();

    String idColumn() default "id";

    String nameColumn() default "name";

    String parentIdColumn() default "parent_id";

    enum RelatedType {
        /**
         * one-to-one
         */
        SINGLE,
        /**
         * one-to-many
         * related field join with comma, e.g. value1,value2
         */
        MULTIPLE,
        /**
         * one-to-tree
         * related field has parent
         */
        LEVEL
    }

}
