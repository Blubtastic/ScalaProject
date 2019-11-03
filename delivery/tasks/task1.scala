import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger

object Main extends App {



  //Task 1.a
  var listOfNumbers = Array.emptyIntArray
  for (w <- 0 to 50){
    listOfNumbers = listOfNumbers :+ w
  }
  println("\n Task 1.a: (generate list from 1 to 50): ")
  println(listOfNumbers.mkString(","))



  //Task 1.b (method)
  def sumOfNumbers(numbers: Array[Int]): Int = {
    var sum = 0
    numbers.foreach(sum += _) //adds each element (_) to sum
    sum
  }
  println("\n Task 1.b: (Product of list): ")
  println(sumOfNumbers(listOfNumbers)) //1275

  //Task 1.b (function instead of method)
  // val sumOfNumbers = (numbers: Array[Int]) => {
  //     var sum = 0
  //     numbers.foreach(sum += _) //adds each element (_) to sum
  //     sum
  // }
  // println("\n Task 2.a: (Product of list): ")
  // println(sumOfNumbers(listOfNumbers)) //1275



  //Task 1.c
  def sumOfNumbersRecursive(numbers: Array[Int]): Int = {
    numbers match {
      case Array(x,_*) => x * sumOfNumbersRecursive(numbers.tail)
      case _ => 1
    }
  }
  println("\n Task 1.c: (Recursive product of list): ")
  println(sumOfNumbersRecursive(Array(1,10,3)))



  //Tastk 1.d
  def nthFibonacci(nthNumber: BigInt): BigInt = {
    if (nthNumber <= 2) 1
    else nthFibonacci(nthNumber-1) + nthFibonacci(nthNumber-2)
  }
  println("\n Task 1.d: (Fibonacci): ")
  println(nthFibonacci(10))
  /*
  Difference between the data types: BigInt stores vales using more bits (as many as needed),
  so it can store larger numbers than Int. (useful for large fib. numbers)
  */



}