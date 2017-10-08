package com.dsglyy.jdk8Test.lambdaFunctional;

import com.dsglyy.jdk8Test.methodCall.MethodClass123;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Created by Lgaren on 2017/10/5.
 */
public class FunctionalInter {

    @FunctionalInterface
    public interface Functional {

//
//    T accept(Object t,String args);

        void accept(Object... args);

//    void accept();
    }
}