package com.taogen.commons.boot.mybatisplus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taogen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MiddleTable {
    public Class middleService();

    public Class targetService();

    public String idColumn() default "id";

    public String targetIdColumn() default "id";

    public String middleFromIdColumn();

    public String middleToIdColumn();
}
