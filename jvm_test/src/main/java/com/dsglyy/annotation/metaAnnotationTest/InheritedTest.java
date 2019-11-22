package com.dsglyy.annotation.metaAnnotationTest;

import com.dsglyy.common.DSGEnum;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by DSG on 2017/9/27.
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InheritedTest{

//  使用 Inherited 声明的注解可以被自类继承，但是这个注解对类有效
    DSGEnum value() default DSGEnum.DSG3;
}
