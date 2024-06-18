package entity.media;

import entity.db.AIMSDB;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The general media class, for other media types, it can be extended by inheriting this class.
 */
public class Media {

    private static Logger LOGGER = Utils.getLogger(Media.class.getName());

    protected Statement stm;
    protected int id;
    protected String title;
    protected String category;
    protected int value; // the real price of the product (e.g., 450)
    protected int price; // the price displayed to customers (e.g., 500)
    protected int quantity;
    protected String type;
    protected String imageURL;
    protected boolean isSupportedPlaceRushOrder = new Random().nextBoolean();
    protected double weight = (new Random().nextDouble() * 0.9) + 0.1;

    public Media() throws SQLException {
        stm = AIMSDB.getConnection().createStatement();
    }

    public Media(int id, String title, String category, int price, int quantity, String type) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public Media(int id, String title, String category, int value, int price, int quantity, String type, String imageURL) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.imageURL = imageURL;
    }

    // Get quantity from the database for the current media item
    public int getQuantity() throws SQLException {
        int updated_quantity = getMediaById(this.id).quantity;
        this.quantity = updated_quantity;
        return updated_quantity;
    }

    // Get a specific media item by its ID
    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM Media WHERE id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet res = stm.executeQuery();
        if (res.next()) {
            return new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setMediaURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
        }
        return null;
    }

    // Get all media items from the database
    public List<Media> getAllMedia() throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("SELECT * FROM Media");
        return _extractMediaFromResultSet(res);
    }

    // Get media items by their type
    public List<Media> getMediaByType(String type) throws SQLException {
        String sql = "SELECT * FROM Media WHERE type = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, type);
        ResultSet res = stm.executeQuery();
        return _extractMediaFromResultSet(res);
    }

    // Update a specific field for a media item by its ID
    public void updateMediaFieldById(String tableName, int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        if (value instanceof String) {
            value = "\"" + value + "\"";
        }
        String sql = String.format("UPDATE %s SET %s = %s WHERE id = %d", tableName, field, value, id);
        stm.executeUpdate(sql);
    }

    // Add a new media item to the database
    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        String sql = "INSERT INTO Media (title, type, category, imageUrl, price, quantity, value) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, type);
        stm.setString(3, category);
        stm.setString(4, imgUrl);
        stm.setDouble(5, price);
        stm.setInt(6, quantity);
        stm.setInt(7, 1); // Assuming 'value' is defaulting to 1, could be parameterized if needed

        stm.executeUpdate();
    }

    // Delete a media item by its ID
    public void deleteMediaById(int id) throws SQLException {
        String sql = "DELETE FROM Media WHERE id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        stm.executeUpdate();
    }

    // Helper method to extract media items from a ResultSet
    private ArrayList<Media> _extractMediaFromResultSet(ResultSet res) throws SQLException {
        ArrayList<Media> items = new ArrayList<>();
        while (res.next()) {
            Media media = new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setMediaURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
            items.add(media);
        }
        return items;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public Media setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Media setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getPrice() {
        return this.price;
    }

    public Media setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Media setMediaURL(String url) {
        this.imageURL = url;
        return this;
    }

    public Media setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Media setType(String type) {
        this.type = type;
        return this;
    }

    public boolean getIsSupportedPlaceRushOrder() {
        return this.isSupportedPlaceRushOrder;
    }

    public void setIsSupportedPlaceRushOrder(boolean isSupportedPlaceRushOrder) {
        this.isSupportedPlaceRushOrder = isSupportedPlaceRushOrder;
    }

    public double getWeight() {
        return this.weight;
    }

    public Media setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", title='" + title + "'" +
                ", category='" + category + "'" +
                ", price='" + price + "'" +
                ", quantity='" + quantity + "'" +
                ", type='" + type + "'" +
                ", imageURL='" + imageURL + "'" +
                "}";
    }
}
