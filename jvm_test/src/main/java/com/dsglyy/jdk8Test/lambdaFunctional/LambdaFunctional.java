package com.dsglyy.jdk8Test.lambdaFunctional;

import java.util.Arrays;
import java.util.List;
import com.dsglyy.jdk8Test.lambdaFunctional.FunctionalInter.Functional;
/**
 * Created by Lgaren on 2017/10/5.
 */
public class LambdaFunctional <T>{

    public static void main(String[] args) {
        List sd = Arrays.asList("a", "b", "d");
        sd.forEach(e -> System.out.println(e));
//        test((e,c) -> {System.out.println(c);return"";},"out Lambda");
//        test2(new FunctionalInter[]{(e ->System.out.println("saf")),(e ->System.out.println("saf"))});
//      test2(new FunctionalInter[]{(e ->System.out.println("saf")),(e ->System.out.println("saf"))});
//        test((e,c) -> {System.out.println(c),"out Lambda");
//
//        FunctionalInter fun = (() -> System.out.println("saf"));
//        fun.accept();
        test3(e -> {System.out.println("包装函数测试");
            System.out.println(e[0]);
        }).accept("测试参数");

    }


//    public static void test(FunctionalInter<String > t,String str){
//        System.out.println("before Lambda");
//
//        t.accept(t,"invoke Lambda");
//        System.out.println("after Lambda");
//        System.out.println(str);
//    }

    public static void test2(Functional[] args){
//        arg.accept(arg);
//        arg2.accept(arg2);


        Arrays.asList(args).forEach(e -> e.accept(e));
//        for (FunctionalInter lambda: args) {
//            lambda.accept(lambda,"invoke Lambda");
//        }
    }

    public static Functional test3(Functional fun){
        String str = "etfgwrh";
        return (e -> {
            System.out.println(str);
//          在这里把  参数 e 传进去之后 在上面的 test3 方法调用的时候里面  lambda 的参数e
            fun.accept(e[0]+ "二次测试");
            System.out.println(e[0]);
            System.out.println("hahahah");
        });
    }

}

