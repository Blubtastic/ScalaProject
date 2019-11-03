import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger

object Main extends App {
  //Task 2.a
  def initializeThread(body: => Unit): Thread = { //"=>" means the function should return Unit
    val returnedThread = new Thread {
      override def run() = body
    }
    returnedThread
  }
  println("\n Task 2.a: (initialize function as thread): ")
  println(initializeThread(_)) //Test with empty param list



  //Task 2.b
  private var counter: Int = 0
  def increaseCounter(): Unit = {
    counter += 1
  }

  println("\n Task 2.b: (3 threads, print counter vars): ")
  val thread1 = initializeThread(increaseCounter)
  val thread2 = initializeThread(increaseCounter)
  val thread3 = initializeThread({println("Counter value: " + counter + "\n")})
  thread1.start
  thread2.start
  thread3.start
  //In this case, it prints 2 most of the time, but 1 sometimes. (due to call order)
  /*
  Thread 3 prints value of counter. This is 1 or 2, depending on whether
  only one thread or both (of thread1 & thread2) finished before thread3 prints the result.

  The phenomenon is called inconsistent retrieval. This program is non-deterministic.

  This is a problem in applications that needs to be deterministic.
  One example is banking applications. If two withdrawal transactions on an account
  are done simultaneously, it can lead to the money getting lost because the account balance
  is not updated in between, such as if the sum of both transactions results in a negative
  balance in the account. This conflict is called lost update.
  */



  //Task 2.c

  def increaseCounterSynchronized(): Unit = this.synchronized{
    counter += 1
  }
  /*
  by adding this.synchronized, only one thread is allowed to run the function at the same
  time. This makes increaseCounter thread-safe, as race conditions cannot occur.
  */



  //Task 2.d
  /*
  Deadlock happens when two or more computer programs sharing the same resources are
  preventing each other from accessing said resource. Because none of the programs can
  access it, they both cease to function. One of four conditions can be used to
  prevent deadlock:
  1: Mutual Exclusion - no process wil have exclusive access to a resource.
  2: Hold and Wait - processes must be prevented from holding one or more resources while
  simultaneously waiting for one or more others. (finish what you're doing before you start waiting
  for other resources)
  3: No Preemption - Various techniques to ensure preemption of process resource allocation.
  4: Circular Wait - Can be avoided in a number of ways. Requires that processes request
  resources only in strictly increasing or decreasing order.
  */
  def deadlock() = {
    lazy val Resource1 : Int = Resource2
    lazy val Resource2 : Int = Resource1
    val xyThread = new Thread {println(Resource1)}
    xyThread.start()
  }
  //Running the line below puts the program into a deadlock and freezes it.
  //deadlock()

}