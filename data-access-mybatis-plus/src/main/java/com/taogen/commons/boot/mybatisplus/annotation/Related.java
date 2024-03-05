package com.taogen.commons.boot.mybatisplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Related {
    public RelatedType relatedType();

    /**
     * It's MyBatis Plus service subclass
     * You can use class of service Interface or service implementation
     *
     * @return
     */
    public Class serviceClass();

    /**
     * support {entityClass} or IdName.class
     *
     * @return
     */
    public Class returnType();

    public String relatedFieldName();

    public String idColumn() default "id";

    public String nameColumn() default "name";

    public String parentIdColumn() default "parent_id";

    public String parentIdFieldName() default "parentId";

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
