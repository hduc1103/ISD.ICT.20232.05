package org.example.models;

import org.example.utils.DBConnection;

import java.sql.*;

public class Delivery {
    /**
     * Single responsibily:
     * Delivery should have only 1 responsiblity for store data, saveDelvery should be stored in another class
     */
    private int id;
    private String name;
    private String phoneNumber;
    private String province;
    private String instruction;
    private String address;
    private String email;
    private boolean isRushDelivery;

    public boolean isRushDelivery() {
        return isRushDelivery;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Delivery(String name, String phoneNumber, String email , String province , String address, String instruction) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.instruction = instruction;
        this.address = address;
        this.email = email;
        this.isRushDelivery = false;
    }

    public void setRushDelivery(boolean isRush) {
        this.isRushDelivery = isRush;
    }


    public static Delivery saveDelivery(Delivery delivery) throws SQLException, ClassNotFoundException {
        // External coupling
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO delivery_info (name, phone_number, email, province, instruction, address, is_rush) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Sequential cohesion
                preparedStatement.setString(1, delivery.getName());
                preparedStatement.setString(2, delivery.getPhoneNumber());
                preparedStatement.setString(3, delivery.getEmail());
                preparedStatement.setString(4, delivery.getProvince());
                preparedStatement.setString(5, delivery.getInstruction());
                preparedStatement.setString(6, delivery.getAddress());
                preparedStatement.setBoolean(7, delivery.isRushDelivery());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            delivery.setId(generatedId);
                        } else {
                            throw new SQLException("Failed to retrieve generated ID.");
                        }
                    }
                } else {
                    throw new SQLException("Failed to insert delivery, no rows affected.");
                }
            }
        }
        return delivery;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
