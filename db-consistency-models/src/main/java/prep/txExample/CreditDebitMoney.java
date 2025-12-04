package prep.txExample;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CreditDebitMoney {

    List<Transfer> historyTransferLedger;
    //Connection connection;

    public CreditDebitMoney() {
        this.historyTransferLedger = new LinkedList<>();
        //this.connection = connection;
    }

    class Creditor {
        String full_name;
    }

    class Debitor {
        String full_name;
    }

    class Transfer {
        // unique transfer fields
        LocalDate localDate;
        Creditor creditor;
        Debitor debitor;
        Money money;
    }

    class Money {
        String amount;
        String currency;
        PaymentMethod paymentMethod;

        enum PaymentMethod {
            CARD, CASH
        }

        public void makeTransfer(Creditor creditor, Debitor debitor, Money money, Connection connection) {

            try (var stmnt = connection.createStatement()) {

                // can creditor has on account
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }//
    }

    public static void main(String[] args) {
        CreditDebitMoney creditDebitMoney = new CreditDebitMoney();


    }
}
