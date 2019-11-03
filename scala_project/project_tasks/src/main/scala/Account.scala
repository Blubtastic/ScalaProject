import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

  class Balance(var amount: Double) {}

  val balance = new Balance(initialBalance)

  //Thread-safe: Using this.synchronized to avoind multiple simultaneous executions.
  //Error-handling: Either datatype to either return Unit or String(error).
  def withdraw(amount: Double): Either[Unit, String] = this.synchronized(
    if (this.balance.amount - amount < 0 || amount < 0) {
      return Right("Not enough available on account or invalid input")
    } else {
      this.balance.amount -= amount
      return Left()
    }
  )

  def deposit(amount: Double): Either[Unit, String] = this.synchronized(
    if (amount < 0) {
      return Right("You can't deposit a negative number")
    } else {
      this.balance.amount += amount;
      return Left()
    })

  def getBalanceAmount: Double = this.synchronized(return this.balance.amount)

  def transferTo(account: Account, amount: Double) = {
    bank addTransactionToQueue(this, account, amount)
  }


}
