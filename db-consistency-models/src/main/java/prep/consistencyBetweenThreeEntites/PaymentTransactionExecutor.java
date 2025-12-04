// java
package prep.consistencyBetweenThreeEntites;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentTransactionExecutor implements CommandLineRunner {

    private final DataSource dataSource;

    public void createFullPaymentTransaction(
            String referenceNo,
            double amount,
            String currency,
            String ledgerType,
            double ledgerAmount,
            String paymentMethodType,
            String paymentDetails,
            String bankName,
            String swiftCode,
            double feeAmount
    ) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            // show tables
            try (Statement showStmt = connection.createStatement();
                 ResultSet tablesRs = showStmt.executeQuery(
                         "SELECT table_name FROM information_schema.tables WHERE table_schema='public'")) {
                while (tablesRs.next()) {
                    System.out.println("Found table: " + tablesRs.getString(1));
                }
            }

            connection.setAutoCommit(false); // begin transaction

            long transactionId;

            // 1. INSERT into payment_transaction and get id (Postgres RETURNING)
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO payment_transaction(reference_no, amount, currency) VALUES (?, ?, ?) RETURNING id"
            )) {
                ps.setString(1, referenceNo);
                ps.setDouble(2, amount);
                ps.setString(3, currency);
                try (var rs = ps.executeQuery()) {
                    if (!rs.next()) throw new RuntimeException("Could not retrieve transaction id");
                    transactionId = rs.getLong(1);
                }
            }

            System.out.println("Inserted payment_transaction id=" + transactionId);

            // 2. INSERT into ledger_entry (use correct table and columns)
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ledger_entry(transaction_id, entry_type, amount, account_code) VALUES (?, ?, ?, ?)"
            )) {
                ps.setLong(1, transactionId);
                ps.setString(2, ledgerType);
                ps.setDouble(3, ledgerAmount);
                ps.setString(4, "DEFAULT_ACCOUNT"); // replace with real account code if needed
                ps.executeUpdate();
            }

            System.out.println("Inserted ledger_entry for transaction id=" + transactionId);

            // 3. INSERT into payment_method
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO payment_method(transaction_id, method_type, details) VALUES (?, ?, ?)"
            )) {
                ps.setLong(1, transactionId);
                ps.setString(2, paymentMethodType);
                ps.setString(3, paymentDetails);
                ps.executeUpdate();
            }

            // 4. INSERT into agent_bank
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO agent_bank(transaction_id, bank_name, swift_code, fee_amount) VALUES (?, ?, ?, ?)"
            )) {
                ps.setLong(1, transactionId);
                ps.setString(2, bankName);
                ps.setString(3, swiftCode);
                ps.setDouble(4, feeAmount);
                ps.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            try { connection.rollback(); } catch (Exception ignored) {}
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            try { connection.setAutoCommit(true); } catch (Exception ignored) {}
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


        System.out.println("Testing phase");
        try (Connection connection1 = DataSourceUtils.getConnection(dataSource)) {
            try (Statement showStmt = connection1.createStatement(); var rs = showStmt.executeQuery("SELECT * FROM payment_method")) {
                var md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    StringBuilder sb = new StringBuilder("Row: ");
                    for (int i = 1; i <= cols; i++) {
                        if (i > 1) sb.append(", ");
                        sb.append(md.getColumnName(i)).append("=").append(rs.getString(i));
                    }
                    System.out.println(sb.toString());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.createFullPaymentTransaction(
                String.valueOf(UUID.randomUUID()),
                10.0,
                "EUR",
                "DEBIT",
                20.50,
                "CARD",
                "Transfer money to grandpa",
                "1234-5433-1234-7654",
                "pacs_008",
                0.9
        );
    }
}