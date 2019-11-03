import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies

    //Thread-safe: Using this.synchronized to avoind multiple simultaneous executions. 
    //Error-handling: Either datatype to either return Unit or String(error). 
    def withdraw(amount: Double): Either[Unit, String] = this.synchronized {
        if (this.balance.amount >= amount && amount > 0.0){
            this.balance.amount -= amount
            Left()
        } else {
            Right("Withdrawal failed. Please enter a positive number lower than your current balance. ")
        }
    }
    def deposit (amount: Double): Either[Unit, String] = this.synchronized {
        if(amount >= 0.0) {
            this.balance.amount += amount
            Left()
        } else {
            Right("Deposit failed. Please enter a positive number. ")
        }
    }
    def getBalanceAmount: Double = this.synchronized {
        this.balance.amount
    }

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }
}
