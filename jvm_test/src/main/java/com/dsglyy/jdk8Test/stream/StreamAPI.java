package com.dsglyy.jdk8Test.stream;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Lgaren on 2017/10/7.
 */
public class StreamAPI {

    public static void main(String[] args){
        List<Person> list =  Person.createRoster();
        Long  stream = list.stream()
                .filter(e -> e.getAge()> 10 ).count();
                System.out.println(stream);
    }

}
