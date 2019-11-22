package com.dsglyy.jdk8Test.methodCall;

import com.dsglyy.jdk8Test.methodCall.MethodFunctional.*;

import java.util.*;

/**
 * Created by Lgaren on 2017/10/6.
 */
public class MethodClass123 {

    private String name;

    public static MethodClass123 create( final Supplier123< MethodClass123 > supplier ) {
        return supplier.get();
    }

//    MethodClass123(String name){
//        this.name = name;
//    }

    public static String collide( ) {
        System.out.println( "Collided ");
        return "";
    }

    public void follow(MethodClass123 obj) {
        System.out.println(obj.name);
        System.out.println(this.name);
    }

    public void follow2(String obj) {
        System.out.println(obj);
        System.out.println(this.name);
    }

    public MethodClass123 setName(String name ){
        this.name = name;
        return this;
    }

    private String repair() {
        System.out.println( "Repaired " + this.toString() );
        return "1234";
    }

    public static void main(String[] args) {

//        ClassName :: new 其实返回一个函数接口对象。这个和构造函数有点像
        MethodClass123 met = MethodClass123.create(MethodClass123 :: new);
        Supplier123<MethodClass123> fun = MethodClass123 :: new;
        MethodClass123 insta = ((Supplier123<MethodClass123>) MethodClass123 :: new).get();
        MethodClass123.test(MethodClass123 :: new);
        final List cars = Arrays.asList( insta );
//        instance :: instanceMethodName  这个语法返回的也是一个函数接口对象，作用类似于把一个方法转换为函数
//        值得注意的是如果函数接口定义了参数和返回值得类型，这里的方法也保持一致(类型只能小于函数接口的定义)。
//        奇怪的是当 函数接口里面的返回值为void 的时候这里的方法返回值可是是任何类型,但是此时好像无法拿到返回值。
//        之所以这样是因为方法的参数最终会转化为函数的返回值和参数
        ((Functional1) insta :: repair).accept();

//       ClassName::staticMethodName 静态方法的可以采用这种方式引用 ，对于参数和返回值的约束同上，
        ((Functional1) MethodClass123::collide).accept();

//        ClassNeam :: methodName 这种方式有严格的限制，首先接口函数的第一个参数类型必须是和这里的class一样，其余任意
//        而且参数的个数必须比 class 里面的 方法多一个 ，再者这个方法不是静态的。
//        在这个函数执行的时候，最终走入到类的方法里面，由于非静态的方法只能用对象来调用，那么这种情况的实质就是，
//        用函数的第一个参数来执行函数  ，把其余的参数作为参数传递给类方法
        Functional2<MethodClass123> dsg1 =  MethodClass123::follow;
        dsg1.accept(new MethodClass123().setName("aqfqagh"),new MethodClass123().setName("ageg65"));

        Functional3<MethodClass123> dsg2 =  MethodClass123::follow2;
        dsg2.accept(new MethodClass123().setName("aqfqagh"),"asfghjhdf");
    }

    public static  void test(Supplier123<MethodClass123> ins){
        System.out.println(ins.get().getClass());
    }

    public static  void test1(Functional1 ins){}

}
