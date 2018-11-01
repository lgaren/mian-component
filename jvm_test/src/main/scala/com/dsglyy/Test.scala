package com.dsglyy

import scala.collection.mutable.ArrayBuffer

/**
  * Created by admin on 2017/5/3.
  */
object testFun {
  def main(args: Array[String]) {

    //    把函数赋值给变量注意后面的  _ ，没有 _ 会报错
//        val asd = layout _
//        println(asd(12,13))

    //    最终结果多打出来一个括号的原因是 apply 返回值为空,这里打印一个空返回值的函数，就打赢出来了一个（）tuple
    //    当 apply 返回值不为空的时候，就会不会打印出一个空tuple
    //    println(apply(layout,10))
    //    println(matchTest1(2))


    //    静态返回一个函数
//        val fun = returnFun(5)
//        println(fun(layout,3))

    //    动态包装一个函数

//    val fun1 = returnFun1((x,y) => {
//      val  y = x + 12
//      y*10
//    },5)
//    print(fun1._1(10))
    //    println(fun1(10))

    //    动态返回一个函数包装外壳
//        val fun2 = returnFun2(5)
    //    在使用这个返回函数的时候需要给他传进去一个函数，然后这个外壳会包装传进去的函数
//        println(fun2(layout,10))

    //    函数柯里化的两种使用  注意右面的 _
    //    val kl = add(12) _
    //    println(kl(12))
    //    println(add(12)(12))

    //    spark编程模式演示
    //    var i = 0
    //    val ret = map( x => {
    //      println("在高阶函数里面传入匿名函数")
    //      i = i + 10
    //      (x,i)
    //    })
    //    println(ret)

    //    隐式转换
    //    getStr 是一个必须传参数的 函数 被声明为隐式函数后再调用的时候不必要传参数，他会拿取同一作用域内的隐式变量作为参数，但是注释掉隐式参数后他就会会报错
    //    如果同一作用于域内有多个参数，而函数只要一个参数的时候就会报错
    //    println(getStr)
    //    println(getStr("hahahahahha"))
  }

  //  ===============================基本测试=================================================
  //  匿名函数
  //  var add = (m:Int,n:Int) => m+n

  //  可变参数
  //  printStrings("Runoob", "Scala", "Python");
  //  def printStrings( args:String* ) = {
  //    var i : Int = 0;
  //    for( arg <- args ){
  //      println("Arg value[" + i + "] = " + arg );
  //      i = i + 1;
  //    }
  //  }

  //  柯里化。 把一个两个参数的函数，变成两个至于一个参数的函数，执行两次
  //  def add(x :Int ,y : Int ) = x + y         ********************************************
  def add(x:Int)(y:Int) = x + y  //这个函数式上面那个函数的柯里化后的结果 *****************************
  def strcat(s1: String)(s2: String) = {
    s1 + s2
  }
  //  println( "str1 + str2 = " +  strcat("hello")("dsg") )  ************   上面那个函数的使用


  //  def this() = this(new SparkConf())
  //  用private修饰的时候可以指定package
  //  private[spark] def this(master: String, appName: String) =this(master, appName, null, Nil, Map())

  def test()={
    "asdaff"
  }

  //=============================================高阶函数测试====================================

  // 示例函数
  def layout(x: Int,y: Int) : Int= {
    println(x*100)
    x*10
  }

  // 函数 f 和 值 v 作为参数，
  def apply(f: Int => Int, v: Int) : String = {
    println("====================")
    f(v)  //  把参数v 传递给 f让他计算
    f(9)  //把90传给函数f让他计算
    println("=======================")
    "hahahahha"
  }

  //这个方法将返回一个函数，返回下面的 iAmAReturnFun 函数
  def returnFun(x: Int): (Int => Int ,Int)=>String  ={
    //      val fun = layout(x)
    //  iAmAReturnFun(layout,3)   这个表示执行这个函数
    iAmAReturnFun  // 这个表示返回这个函数
  }

  //这个函数的功能是：传进去一个函数，然后后返回一个经过加工后的新函数；
  //  ****************** 返回值 Int=>String 表示返回一个函数 这个函数 { 的参数是 Int类型的 返回值为String }
  def returnFun1(f:(Int,Int) => Int ,v: Int) : (Int=>String,Int) = {
    println("函数包装之前=======")
    val b = 12
    ((a :Int) => {
      println("包装函数内部的代码")
      val num = f(a,b) + f(b,b)
      "这是包装函数"+ num
    }
      ,100)
  }

  //这个函数的功能是：返回一个函数的包装外壳，具体要包装什么函数，要在这个函数的返回的函数的参数里面指定
  // ****************** (Int => Int ,Int)=>String 表示返回一个 函数  这个返回的函数的第一个参数是 （ Int => Int ）的函数，第二个参数是个 Int 值
  // ******************  Int => Int 同样是个函数 参数和返回值都是 Int
  def returnFun2(v: Int) : (Int => Int ,Int)=>String = {
    println("生成函数包装外壳之前=======")
    val b = 12
    def wrapFun(f:Int => Int ,a :Int) : String = {
      println("函数包装外壳的代码")
      val num = f(a) + f(b)
      "这是包装外壳函数"+ num
    }
    wrapFun
  }

  def iAmAReturnFun(f: Int => Int,a:Int):String ={
    println("hahahahhah")
    f(a)
    "adfgjkkhjhjhj"
  }

  //================================匿名函数的使用和spark编程模式很像=======================================================
  //这个函数已经非常类似spark的map了 ,接受一个函数，并且map会给这函数传入一个String，然后这个函数会把 String映射为（String ,Int）
  //  在这里测试10次
  //  这个map函数最终返回  一个 ArrayBuffer[(String,Int)]
  def map(f: String => (String,Int)) : ArrayBuffer[(String,Int)] ={
    var arr = ArrayBuffer[(String,Int)]()
    //  测试10次
    for( a <- 1 to 10){
      println("调用匿名函数")
      arr += f("test")
    }
    arr
  }

  //=================================隐式转换===================================================
  //  一个 implicit 关键字作用于所有的参数，及所欲的参数都为隐式参数，且所有的参数共享一个变量
  //  当有两个相同类型的隐式变量的时候，函数并不会为每个参数匹配一个，只会报错。
  def getStr(implicit test : String , test2 : String) = test+test2
  //  implicit val str,str2 = "DSGLYY.COM";"www.dsglyy.com"
  implicit val str= "DSGLYY.COM"

  //  隐式装换类型，这个函数需要一个String ，如果直接给一个Int会报错，当再定义一个隐式的函数把Int转换为String函数后，就不会报错
  def typeTest(str:String) = str
  implicit def intToString(x : Int) = x.toString

  //=================================模式匹配测试===============================================
  //  示例class
  case class Person(name: String, age: Int)
  //  在方法上指定模式匹配
  def matchTest1(x: Int): String =  x match{
    case 1 => "one"
    case 2 => "two"
    case _ => "many"
  }

  //  代码块里面指定模式匹配
  def matchTest2(x: Int){
    val alice = new Person("Alice", 25)
    val bob = new Person("Bob", 32)
    val charlie = new Person("Charlie", 32)
    for (perso <- List(alice, bob, charlie)) {
      perso match {
        case Person("Alice", 25) => println("Hi Alice!")
        case Person("Bob", 32) => println("Hi Bob!")
        case Person(name, age) => println("Age: " + age + " year, name: " + name + "?")
      }
    }
  }
}




