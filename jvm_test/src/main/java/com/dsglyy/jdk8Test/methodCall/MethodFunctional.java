package com.dsglyy.jdk8Test.methodCall;

/**
 * Created by Lgaren on 2017/10/7.
 */
public class MethodFunctional {

    @FunctionalInterface
    public interface Supplier123<T> {

        T get();

    }

    @FunctionalInterface
    public interface Functional1 {

//
//    T accept(Object t,String args);

        void  accept();

//    void accept();
    }

    @FunctionalInterface
    public interface Functional2<T> {
        void  accept(T o1, T o2);
    }

<<<<<<< HEAD

=======
>>>>>>> f515e2b1fd02cb3af610c9d249efc5732d1c37eb
    @FunctionalInterface
    public interface Functional3<T> {
        void  accept(T o1, String o2);

    }

}
