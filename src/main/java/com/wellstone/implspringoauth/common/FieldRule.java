package com.wellstone.implspringoauth.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldRule {
    String group() default "Null"; // 그룹 이름..

    Rule rule() default Rule.NOT_ALL_NULL;

    enum Rule {
        NOT_ALL_NULL, EXIST_OTHERS
    }
}
