package com.dsglyy.Thread;

public class VolatileTest  extends Thread {

//    volatile 修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。
//    而且，当成员变量发生变化时，强迫线程将变化值回写到共享内存。

//    在Java内存模型中，有main memory，每个线程也有自己的memory (例如寄存器)。为了性能，一个线程会在自己的memory中保持要访问的变量的副本。
//    这样就会出现同一个变量在某个瞬间，在一个线程的memory中的值可能与另外一个线程memory中的值，或者main memory中的值不一致的情况。

//    https://blog.csdn.net/u012723673/article/details/80682208
       private volatile boolean pleaseStop;

       public volatile  int  inc = 0 ;

       public void increase(){
           inc ++ ;
       }

       public void run() {

         while (!pleaseStop) {

            // do some stuff...

           }

         }



  public void tellMeToStop() {

   pleaseStop = true;

  }

    public static void main(String[] args) {

        final VolatileTest test = new VolatileTest();
        for(int i = 0 ; i < 10 ; i ++ ){
            new Thread(){
                public void run(){
                    for (int j = 0 ; j < 1000 ; j ++)
                        test.increase();
                }
            }.start();

        }
       //Thread.activeCount() 返回当前线程组的还在活动的线程数  ，在这里所有被创建的线程都属于main线程组 ， 当Thread.activeCount() 返回1 时，表明上面所有被创建的线程全部执行完成就剩下一个main线程
        while(Thread.activeCount() > 1 )
            //        方法很多人翻译成线程让步，就是说当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，后面等待操作系统重新调度；
            Thread.yield();
        System.out.println(test.inc);
    }


}
