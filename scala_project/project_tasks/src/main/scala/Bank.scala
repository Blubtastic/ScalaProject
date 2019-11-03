class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    //This function does not have to be synchronized, as two transactions can be added 
    //concurrently without causing problems. 
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        // TODO
        // project task 2
        // create a new transaction object and put it in the queue
        // spawn a thread that calls processTransactions

        //Create a new transaction object from the transaction class. 
        val transaction = new Transaction(transactionsQueue,
                                            this.processedTransactions,
                                            from,
                                            to,
                                            amount,
                                            this.allowedAttempts)
            transactionsQueue.push(transaction) //Push this transaction to the queue
            //Create thread which runs processTransactions
            val transactionThread = new Thread{override def run() = processTransactions}
            transactionThread.start //Run it
    }
    
    //First process the first transaction in the queue. 
    //Try to execute transaction
    private def processTransactions: Unit = ???
        // TOO
        // project task 2
        // Function that pops a transaction from the queue
        // and spawns a thread to execute the transaction.
        val transaction = transactionsQueue.pop //get new transaction
        val newTransaction = new Thread(transaction) //Create new thread from transaction
        newTransaction.start
        newTransaction.join
        // Finally do the appropriate thing, depending on whether
        // the transaction succeeded or not
        if (transaction.status == TransactionStatus.PENDING){ //The transaction is not finished. 
            transactionsQueue.push(transaction)
            processTransactions //Try again 
        } else { //push to finished transactions. 
            processedTransactions.push(transaction) 
        }



    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
