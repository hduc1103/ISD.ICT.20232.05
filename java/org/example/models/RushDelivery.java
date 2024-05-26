package org.example.models;

import org.example.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RushDelivery extends Delivery {
    /**
     * Single responsibility:
     * RushDelivery should have 1 responsibility for book  is store data,
     * saveRushDelivery should be store in another class
     */
    private String receiveTime;
    private  String rushAddress;

    public RushDelivery(String name, String phoneNumber, String province, String instruction, String address, String email, String receiveTime, String rushAddress) {
        super(name, phoneNumber, email , province, address, instruction);
        this.receiveTime = receiveTime;
        this.rushAddress = rushAddress;
        setRushDelivery(true);
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public String getRushAddress() {
        return rushAddress;
    }

    public static RushDelivery saveRushDelivery(RushDelivery rushDelivery) throws SQLException, ClassNotFoundException {
        Delivery savedDelivery = saveDelivery(rushDelivery);
        // External Coupling
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO rush_delivery_info (delivery_info_id, rd_time, rd_address) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Functional cohesion
                preparedStatement.setInt(1, savedDelivery.getId());
                preparedStatement.setString(2, rushDelivery.getReceiveTime());
                preparedStatement.setString(3, rushDelivery.getRushAddress());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Failed to insert rush delivery, no rows affected.");
                }
            }
        }
        return rushDelivery;
    }

}
