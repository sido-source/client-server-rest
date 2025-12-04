package prep.txExample;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;

public class SeatBooking {

    public void reserveSeat(Connection connection) throws SQLException {
        // We SELECT the seat (no locking)
        try {
            connection.setAutoCommit(false);
            String select = "SELECT taken FROM cinema_seat WHERE id = 1 and taken=false FOR UPDATE";

            System.out.println(Thread.currentThread().getName() + " starting sql statement");
            try (PreparedStatement ps = connection.prepareStatement(select);
                 ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println(Thread.currentThread().getName() + " FOUND NO ROW");
                    connection.rollback();
                    return;
                }

                boolean taken = rs.getBoolean("taken");

                System.out.println(Thread.currentThread().getName() +
                        " read seat: taken=" + taken);

                // Simulate slow business logic
                Thread.sleep(2000);

                // Try to set taken = true
                String update = "UPDATE cinema_seat SET taken = TRUE WHERE id = 1";

                try (PreparedStatement updatePs = connection.prepareStatement(update)) {
                    updatePs.executeUpdate();
                    System.out.println(Thread.currentThread().getName() +
                            " updated seat to taken=TRUE");
                }
            }

            connection.commit();
        } catch (InterruptedException e) {
            connection.rollback();
            e.printStackTrace();
            //throw new RuntimeException(e);
        }

    }

    public static void createSeat(Connection connection) throws SQLException {
        String insert = "INSERT INTO cinema_seat (taken) VALUES (FALSE)";
        try (PreparedStatement ps = connection.prepareStatement(insert);
             PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM cinema_seat")) {
            ps.executeUpdate();

            var rs = ps1.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        // ---- CREATE CONNECTION POOL ----
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(5);

        HikariDataSource ds = new HikariDataSource(config);

        SeatBooking booking = new SeatBooking();

        // Create seat (only once)
        try (Connection conn = ds.getConnection()) {
            //conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            booking.createSeat(conn);
        }

        // ----- Two parallel tasks -----
        Callable<Void> t1 = () -> {
            try (Connection conn = ds.getConnection()) {
                booking.reserveSeat(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        Callable<Void> t2 = () -> {
            try (Connection conn = ds.getConnection()) {
                booking.reserveSeat(conn);
            }
            return null;
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.invokeAll(List.of(t1, t2));
        executor.shutdown();
    }
}