import exceptions._
import scala.collection.mutable.Queue


object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO
    // project task 1.1
    // Add datastructure to contain the transactions
    var transactionQueue = Queue.empty[Transaction] //empty transactionqueue

    //By using this.synchronized, two threads can't run the same queue functions simultaneously
    // Remove and return the first element from the queue
    def pop: Transaction = this.synchronized {
        transactionQueue.dequeue
    }

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = this.synchronized {
        transactionQueue.enqueue(t)
    }

    // Return whether the queue is empty
    def isEmpty: Boolean = this.synchronized {
        transactionQueue.length < 1
    }

    // Return the first element from the queue without removing it
    def peek: Transaction = this.synchronized {
        transactionQueue.head
    }

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = this.synchronized {
        transactionQueue.iterator
    }
}

class Transaction(val transactionsQueue: TransactionQueue,
                val processedTransactions: TransactionQueue,
                val from: Account,
                val to: Account,
                val amount: Double,
                val allowedAttemps: Int) extends Runnable {

    var status: TransactionStatus.Value = TransactionStatus.PENDING
    var attempt = 0

    override def run: Unit = {
        // TODO - project task 3
        // Extend this method to satisfy requirements.

        //Transactions can't be processed concurrently as they can mess up each other
        def doTransaction() = this.synchronized{
            //Try transaction up to 3 times
            if (this.attempt < this.allowedAttempts) {
                val withdrawal  = this.from.withdraw(amount) //Try to withdraw, and store result. 
                withdrawal match {
                    case Right(error) => {this.attempt += 1} //Attept faled
                    case Left(success) => {this.to.deposit(amount) //Run deposit frunction from account if success. 
                                            this.status = TransactionStatus.SUCCESS}
                }
            } else {
                //3 attempts failed. Status is set to FAILED. 
                this.status = TransactionStatus.FAILED
            }
        }

        // TODO - project task 3
        // make the code below thread safe
        if (status == TransactionStatus.PENDING) {
            doTransaction
            Thread.sleep(50)
        }


    }
}
