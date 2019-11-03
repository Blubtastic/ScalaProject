import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

  // project task 1.1
  private val transactionQueue = new mutable.Queue[Transaction]()

  //By using this.synchronized, two threads can't run on the same queue functions simultaneously
  // Remove and return the first element from the queue
  def pop: Transaction = this.synchronized(this.transactionQueue.dequeue())

  // Return whether the queue is empty
  def isEmpty: Boolean = this.synchronized(this.transactionQueue.isEmpty)

  // Add new element to the back of the queue
  def push(t: Transaction): Unit = this.synchronized(this.transactionQueue.enqueue(t));

  // Return the first element from the queue without removing it
  def peek: Transaction = this.synchronized(this.transactionQueue.head)

  // Return an iterator to allow you to iterate over the queue
  def iterator: Iterator[Transaction] = this.synchronized(this.transactionQueue.iterator)
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttempts: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {


    def doTransaction(): Unit = {

      if (this.attempt >= this.allowedAttempts) { //Stops if allowedAttempts is reached
        this.status = TransactionStatus.FAILED
        Unit
      }
      this.from.withdraw(amount) match {
        case Left(success) => {
          to.deposit(amount)
          this.status = TransactionStatus.SUCCESS
        }

        case Right(fail) => {
          this.attempt += 1
        }

      }
    }

    //This code is thread safe because thread.join in bank makes thread finish before next transaction can start
    if (status == TransactionStatus.PENDING) {
      doTransaction
      Thread.sleep(50) // you might want this to make more room for
      // new transactions to be added to the queue
    }


  }
}
