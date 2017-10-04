package com.dsglyy;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test1 {
    public static void main(String[] args) {
//        new Thread(new Test().new TestThread()).start();
        ;
        //		new Thread(td).start();
        //		new Thread(td).start();
        //		System.out.println(Math.random()*10);
        //
        //		for (int i = 0; i < 100; i++) {
        //			int a = (int) (Math.random()*100);
        //			if (a<5)System.out.println(a);
        //		}
        //
        //		String str = null;
        //		System.out.println(str);
        //		System.out.println(sum(1,100));

//        Runnable ss=new Test().new TestSynchronized();
//        new Thread(ss).start();
//        new Thread(ss).start();
//        new Thread(ss).start();

//        Runnable ss=new Test1().new TestLock();
//        new Thread(ss).start();
//        new Thread(ss).start();
//        new Thread(ss).start();
        print(7);

    }

    public static void print(int i){
        if ( i == 0){
            return ;
        }else{
//            System.out.println(i);  倒序
            print( i - 1 );
//            System.out.println(i);  顺序
        }

    }

    public static int sum(int i, int j) {
        if (i == j)
            return j;
        return i + sum(i + 1, j);
    }

    public class TestThread implements Runnable {
        int a = 100;

        public void run() {
            a = a - 10;
            System.out.println(a);
        }
    }

    public class TestSynchronized implements Runnable{

        public synchronized void get(){
            System.out.println(Thread.currentThread().getId());
            set();
        }

        public synchronized void set(){
            System.out.println(Thread.currentThread().getId());
        }

        public void run() {
            get();
        }

    }
    public class TestLock implements Runnable {
        Lock lock = new ReentrantLock();

        public void get() {
            lock.lock();
            System.out.println(Thread.currentThread().getId());
            set();
            lock.unlock();
        }

        public void set() {

//          if(lock.tryLock()){}
            lock.lock();
            System.out.println(Thread.currentThread().getId());
            lock.unlock();
        }

        public void run() {
            get();
        }

    }
}
