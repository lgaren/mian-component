package com.dsglyy.annotation.metaAnnotationTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by DSG on 2017/9/29.
 */
public  class Test {
    public static void main(String[] args) throws NoSuchMethodException {

        Class clz = MetaAnnotationTestClass.InheritedImpl.class;
//        Annotation[] s = MetaAnnotationTestClass.InheritedImpl.class.getAnnotations();
        Method method1 = clz.getMethod("testMethod2");
        Annotation[] s = method1.getAnnotations();

        for(Annotation sd : s){
                System.out.println(sd.annotationType().getName());
        }

    }
}
