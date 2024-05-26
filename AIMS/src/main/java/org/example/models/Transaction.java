package org.example.models;

import org.example.utils.DBConnection;

import java.sql.*;

public class Transaction {
    /**
     * Single responsibility: Should have 1 responsibility for storing data,
     * saveTransaction should be placed in another class
     */
    private int id;
    private double amount;
    private String content;
    private Timestamp time;
    private int refId;


    public void setContent(String content) {
        this.content = content;
    }

    public Transaction() {
    }

    public Transaction(int id, double amount, String content, Timestamp time, int refId) {
        this.id = id;
        this.amount = amount;
        this.content = content;
        this.time = time;
        this.refId = refId;
    }

    public Transaction(double amount, String content, Timestamp time) {
        this.amount = amount;
        this.time = time;
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        Transaction transaction = (Transaction) obj;
        return (this.content.equals(transaction.content)
                && this.amount == transaction.amount);
    }

    public int getRefId() {
        return refId;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public static void saveTransaction(Transaction transaction) {
        //External coupling
        try (Connection connection = new DBConnection().getConnection()) {
            String sql;
            // Logical cohesion
            if (transaction.getTransactionId() != 0) {
                sql = "INSERT INTO transaction (amount, contents, error_message, time, transaction_id, ref_id) VALUES (?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO transaction (amount, contents, error_message, time) VALUES (?, ?, ?, ?)";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Procedural cohesion
                preparedStatement.setFloat(1, (float) transaction.getAmount());
                preparedStatement.setString(2, transaction.getContent());
                preparedStatement.setString(3, "");
                preparedStatement.setTimestamp(4, transaction.getTime());
                // Logical cohesion
                if (transaction.getTransactionId() != 0) {
                    preparedStatement.setInt(5, transaction.getTransactionId());
                    preparedStatement.setInt(6, transaction.getRefId());
                }

                preparedStatement.executeUpdate();

                // Logical cohesion
                if (transaction.getTransactionId() == 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Procedural cohesion
                            transaction.setId(generatedKeys.getInt(1));
                        } else {
                            throw new SQLException("Creating transaction failed, no ID obtained.");
                        }
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTransactionId() {
        return id;
    }

    public static Transaction findById(int transactionId) {
        String sql = "SELECT * FROM transaction WHERE transaction_id = ?";
        Transaction foundTransaction = null;
        // External coupling
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Functional cohesion
            preparedStatement.setInt(1, transactionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Functional cohesion
                    int id = resultSet.getInt("transaction_id");
                    double amount = resultSet.getDouble("amount");
                    String content = resultSet.getString("contents");
                    Timestamp time = resultSet.getTimestamp("time");
                    int refId = resultSet.getInt("ref_id") != 0 ? resultSet.getInt("ref_id") : -1;
                    foundTransaction = new Transaction(id, amount, content, time, refId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return foundTransaction;
    }

}
