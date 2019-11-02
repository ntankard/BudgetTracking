package com.ntankard.Tracking.DataBase.Database;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterMap {

    String[] parameterGetters() default {""};
}
