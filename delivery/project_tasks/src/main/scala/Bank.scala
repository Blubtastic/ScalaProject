class Bank(val allowedAttempts: Integer = 3) {

  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()

  //This function does not have to be synchronized, as two transactions can be added
  //concurrently without causing problems, since concurrency is handled in Transaction.scala
  def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {

    val transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
    transactionsQueue.push(transaction)

    //Create thread which runs processTransactions
    val transactionThread = new Thread {
      override def run() = processTransactions
    }
    transactionThread.start
  }

  //First process the first transaction in the queue.
  //Execute transaction run method
  private def processTransactions: Unit = {
    //First transaction in queue
    val transaction = transactionsQueue.pop
    val transactionThread = new Thread(transaction);
    transactionThread.start
    //Will make the current thread wait for transactionThread to finish
    transactionThread.join

    // Finally do the appropriate thing, depending on whether
    // the transaction succeeded or not
    if (transaction.status == TransactionStatus.PENDING) { //The transaction is not finished.
      transactionsQueue.push(transaction)
      processTransactions
    }
    else {
      processedTransactions.push(transaction)
    }


  }


  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }

}
