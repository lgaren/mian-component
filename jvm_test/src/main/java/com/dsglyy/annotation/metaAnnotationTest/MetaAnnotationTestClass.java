package com.dsglyy.annotation.metaAnnotationTest;

import com.dsglyy.common.DSGAnnotation;
import com.dsglyy.common.DSGEnum;
import com.dsglyy.common.DSGException;
import com.dsglyy.common.DSGClass;

import java.util.Map;

/**
 * Created by Lgaren on 2017/9/24.
 */

public class MetaAnnotationTestClass {

//   注解TargetTest里面的参数为 ElementType.TYPE时，用于描述类、接口(包括注解类型) 或enum声明
//  这个类如果不声明为static， 则在里面无法定义 interface， enum， @interface
    public static class Target_TypeTest {

        @TargetTest.TargetTypeTest(enumField = DSGEnum.DSG2, stringField = "MetaAnnotationTestClass.Target_TypeTest.TargetTypeClassTest", value = 0)
        public class TargetClassTest < @TargetTest.TargetType_ParameterTest  T> extends DSGClass {

            @TargetTest.TargetFieldTest('d')
            String field;

            @TargetTest.TargetConstuctorTest(1.3f)
            TargetClassTest (){}

            @TargetTest.TargetMethodTest
           <@TargetTest.TargetType_ParameterTest T>  @TargetTest.TargetType_UseTest DSGClass testMethod(@TargetTest.TargetParameterTest T  field) throws DSGException {
                @TargetTest.TargetLocal_VariableTest
                String var =  "DSGClass";
//                可以早包名右边类名左边
                java.util.@TargetTest.TargetType_UseTest Map a;
                DSGClass lyy =   new MetaAnnotationTestClass.Target_TypeTest().new @TargetTest.TargetType_UseTest TargetClassTest();
                @TargetTest.TargetType_UseTest Map<TargetTest.TargetFieldTest, @TargetTest.TargetType_UseTest DSGEnum> dsg;
                return (@TargetTest.TargetType_UseTest DSGClass) field;
            }

        }

        @TargetTest.TargetTypeTest(value = 1, stringField = "MetaAnnotationTestClass.Target_TypeTest.TargetTypeTnterfaceTest")
        public interface TargetTypeTnterfaceTest {
        }

        @TargetTest.TargetTypeTest(0)
        public enum TargetTypeEnumTest {
        }

        @TargetTest.TargetAnnotation_TypeTest
        @TargetTest.TargetTypeTest(0)
        public @interface TargetTypeAnnotationTest {
        }
    }

    public class  RetentionTest{

        @com.dsglyy.annotation.metaAnnotationTest.RetentionTest.RetentionClassTest
        int a ;

        @com.dsglyy.annotation.metaAnnotationTest.RetentionTest.RetentionRuntimeTest
        int b ;

        @com.dsglyy.annotation.metaAnnotationTest.RetentionTest.RetentionSourceTest
        int c ;

    }

    @DSGAnnotation
    @InheritedTest
    public class InheritedAbstract{

        @DSGAnnotation
        @InheritedTest
        int a = 0;

        @DSGAnnotation
        int b = 0;

        @DSGAnnotation
        @InheritedTest
        public void testMethod(){}

        @DSGAnnotation
        public void testMethod2(){}

    }

//   继承了InheritedAbstract类，类上的 InheritedTest会被继承下来
    public class InheritedImpl extends InheritedAbstract{

        int a = 0;

//        因为重写
        public void testMethod(){}

    }

}
