package org.example.controllers;

import org.example.models.Media;
import org.example.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    /**
     * Retrieves a list of all media items.
     * 
     * @return a list of Media objects
     */
    public List<Media> getMediaItems() {
        String query = "SELECT * FROM media";
        return executeMediaQuery(query);
    }

    /**
     * Searches for media items based on the specified type and information.
     * 
     * @param type the type of search (NAME, CATEGORY, PRICE)
     * @param info the search information
     * @return a list of Media objects matching the search criteria
     */
    public List<Media> searchMediaList(String type, String info) {
        String query = "";
        switch (type) {
            case "NAME":
                query = "SELECT * FROM media WHERE name LIKE ?";
                info = "%" + info + "%";
                break;
            case "CATEGORY":
                query = "SELECT * FROM media WHERE category = ?";
                break;
            case "PRICE":
                query = "SELECT * FROM media WHERE price <= ?";
                break;
            default:
                return new ArrayList<>();
        }
        return executeMediaQuery(query, info);
    }

    /**
     * Executes a media query with the given parameters.
     * 
     * @param query the SQL query string
     * @param params the parameters for the SQL query
     * @return a list of Media objects resulting from the query
     */
    private List<Media> executeMediaQuery(String query, String... params) {
        List<Media> mediaItems = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Media media = createMediaFromResultSet(resultSet);
                    mediaItems.add(media);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Error executing media query", e);
        }

        return mediaItems;
    }

    /**
     * Creates a Media object from the current row of the given ResultSet.
     * 
     * @param resultSet the ResultSet object
     * @return a Media object
     * @throws SQLException if a database access error occurs
     */
    private Media createMediaFromResultSet(ResultSet resultSet) throws SQLException {
        return new Media(
            resultSet.getInt("media_id"),
            resultSet.getString("name"),
            resultSet.getDouble("price"),
            resultSet.getBoolean("support_rush_delivery"),
            resultSet.getDouble("weight"),
            resultSet.getInt("available"),
            resultSet.getString("imageURL"),
            resultSet.getString("category")
        );
    }
}
