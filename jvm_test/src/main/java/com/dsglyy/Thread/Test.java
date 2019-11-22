package com.dsglyy.Thread;

import java.util.concurrent.*;

/**
 * Created on 2018/6/1  Fri PM 13:56
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: Test V 0.0, Jun 01, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(2);

        //创建两个有返回值的任务
        Callable c1 = new MyCallable("A");
        Callable c2 = new MyCallable("B");

        //执行任务并获取Future对象
        Future f1 = pool.submit(c1);
        Future f2 = pool.submit(c2);
        //从Future对象上获取任务的返回值，并输出到控制台
        System.out.println(">>>"+f1.get().toString());
        System.out.println(">>>"+f2.get().toString());
        //关闭线程池
        pool.shutdown();
    }

    static class MyCallable implements Callable{
        private String oid;

        public String getOid() {
            return oid;
        }

        MyCallable(String oid) {
            this.oid = oid;
        }
        @Override
        public Object call() throws Exception {
            return oid+"任务返回的内容";
        }
    }
}
