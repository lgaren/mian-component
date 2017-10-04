package com.dsglyy.annotation.metaAnnotationTest;

import com.dsglyy.common.DSGEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lgaren on 2017/9/24.
 */
public class RetentionTest {
  /*
   *  @Retention定义了该Annotation被保留的时间长短：某些Annotation仅出现在源代码中，而被编译器丢弃；
   *   而另一些却被编译在class文件中；编译在class文件中的Annotation可能会被虚拟机忽略，而另一些在class
   *  被装载时将被读取（请注意并不影响class的执行，因为Annotation与class在使用上是被分离的）。
   *  使用这个meta-Annotation可以对 Annotation的“生命周期”限制。
   */

  /*
  *   取值（RetentionPoicy）有：
  *
　*　　　1.SOURCE:在源文件中有效（即源文件保留）
　*　　　2.CLASS:在class文件中有效（即class保留）
　*　　　3.RUNTIME:在运行时有效（即运行时保留）
  *
  *
  * 与其他注解使用不同的是这个类型的额注解必须以全路径来使用
  * */

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RetentionRuntimeTest{DSGEnum value() default DSGEnum.DSG3;}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface RetentionSourceTest{DSGEnum value() default DSGEnum.DSG3;}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.CLASS)
    public @interface RetentionClassTest{DSGEnum value() default DSGEnum.DSG3;}
}
