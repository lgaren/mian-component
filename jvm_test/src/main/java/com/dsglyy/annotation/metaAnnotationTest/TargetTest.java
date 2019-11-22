package com.dsglyy.annotation.metaAnnotationTest;

import com.dsglyy.common.DSGEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lgaren on 2017/9/24.
 */

//Target 用来描述其它注解，他可以定义一个注解的可用域，（即：被描述的注解可以用在什么地方）

/**
 *  参数（ElementType）的取值有
 *   1.CONSTRUCTOR:用于描述构造器
 *   2.FIELD:用于描述域
 *   3.LOCAL_VARIABLE:用于描述局部变量
 *   4.METHOD:用于描述方法
 *    5.PACKAGE:用于描述包
 *   6.PARAMETER:用于描述参数
 *   7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
*/

public class TargetTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE) // TYPE:用于描述类、接口(包括注解类型) 或enum声明
    public @interface TargetTypeTest {

//      默认的方法，直接以 @TargetTest.TargetTypeTest(1) 上午形式使用
//      当方法无默认值时，在使用该注解时必须赋值，有默认值得无所谓，和其它的方法
        public int value();

//      以这种方式使用 @TargetTest.TargetTypeTest(enumField = DSGEnum.Test3, value = 0)
//      其中 value无默认值值必须指定，enumField 有默认可以不用管
        public String stringField() default "className";

        // 当使用这个注解的时候 需要 DSGEnum 累的参数
        public DSGEnum enumField() default DSGEnum.DSG3;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR) // CONSTRUCTOR:用于描述构造器
    public @interface TargetConstuctorTest {float value() ;}


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)  //FIELD 用来修饰类变量
    public @interface TargetFieldTest{char value() ;}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.LOCAL_VARIABLE) //LOCAL_VARIABLE:用于描述局部变量
    public @interface TargetLocal_VariableTest{ double value() default 6; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER) //.PARAMETER:用于描述参数
    public @interface TargetParameterTest{ String value() default "DSGLYY"; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD) //METHOD:用于描述方法
    public @interface TargetMethodTest{ int value() default 0; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PACKAGE) //METHOD:用于描述方法
    public @interface TargetPackageTest{ boolean value() default true; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE) //ANNOTATION_TYPE:仅用于注解的描述
    public @interface TargetAnnotation_TypeTest{ DSGEnum value() default DSGEnum.DSG3; }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE) //TYPE_USE:用于描述代码中的类符号 ，包括接口，注解，枚举
    public @interface TargetType_UseTest{ DSGEnum value() default DSGEnum.DSG3; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_PARAMETER) //TYPE_PARAMETER:仅用于描述泛型参数 <@TargetTest.TargetType_ParameterTest T>，对于集合类的泛型不适用
                                          //TargetType_UseTest 也可以用在相同的地方
    public @interface TargetType_ParameterTest{ DSGEnum value() default DSGEnum.DSG3; }

}
