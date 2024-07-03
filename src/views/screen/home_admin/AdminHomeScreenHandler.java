package views.screen.home_admin;

import controller.AdminCRUDController;
import controller.HomeController;
import controller.InvoiceListController;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenAdminHandler;
import views.screen.invoicelist.InvoiceListAdminHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import javafx.event.ActionEvent;

public class AdminHomeScreenHandler extends BaseScreenAdminHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(AdminHomeScreenHandler.class.getName());

    @FXML
    private ImageView aimsImage;

    @FXML
    private Label currentPageLabel;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    @FXML
    private TextField searchField;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;
    @FXML
    private Button manageInvoiceBtn;
    @FXML
    private Button addItemBtn;
    @FXML
    private Button manageUserBtn;

    private List<MediaAdminHandler> homeItems;
    private List<MediaAdminHandler> displayedItems;

    public static AdminHomeScreenHandler _instance;

    private SearchHandler searchHandler;
    private ManageUserHandler manageUserHandler;
    private AddItemHandler addItemHandler;

    public AdminHomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        this.searchHandler = new SearchHandler(this, searchField);
        this.manageUserHandler = new ManageUserHandler(this);
        this.addItemHandler = new AddItemHandler(this);
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    private int currentPage = 0;
    private final int itemsPerPage = 12;

    @FXML
    private void showNextMedia(MouseEvent event) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, displayedItems.size());

        if (endIndex < displayedItems.size()) {
            currentPage++;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    @FXML
    private void showPreviousMedia(MouseEvent event) {
        if (currentPage > 0) {
            currentPage--;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());

        // Refresh media list right after setting the controller to ensure items are displayed
        refreshMediaList();

        // Home button
        aimsImage.setOnMouseClicked(e -> {
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        });

        manageInvoiceBtn.setOnMouseClicked(e -> {
            InvoiceListAdminHandler invoiceListAdminHandler;
            try {
                invoiceListAdminHandler = new InvoiceListAdminHandler(this.stage, Configs.INVOICE_LIST_ADMIN_PATH);
                invoiceListAdminHandler.setHomeScreenHandler(this);
                invoiceListAdminHandler.setBController(new InvoiceListController());
                invoiceListAdminHandler.requestToInvoiceList(this);
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        manageUserBtn.setOnMouseClicked(e -> manageUserHandler.handleManageUser(new ActionEvent()));

        addItemBtn.setOnMouseClicked(e -> {
            try {
                addItemHandler.handleAddItem(new ActionEvent());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        searchField.setOnAction(e -> {
            try {
                searchHandler.searchButtonClicked(searchField.getText());
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });

        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
    }


    @FXML
    private void handleSearchButtonClicked(MouseEvent event) {
        try {
            searchHandler.searchButtonClicked(searchField.getText());
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleAddItemClicked(ActionEvent event) {
        try {
            addItemHandler.handleAddItem(event);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<MediaAdminHandler> updateMediaDisplay(List<MediaAdminHandler> items) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        List<MediaAdminHandler> displayedItems = new ArrayList<>(items.subList(startIndex, endIndex));

        int totalPages = (int) Math.ceil((double) items.size() / itemsPerPage);
        int currentDisplayPage = currentPage + 1;
        currentPageLabel.setText("Page " + currentDisplayPage + " of " + totalPages);
        return displayedItems;
    }
    public List<MediaAdminHandler> filterMediaByType(String type) {
        List<MediaAdminHandler> filteredItems = new ArrayList<>();
        for (MediaAdminHandler mediaHandler : homeItems) {
            Media media = mediaHandler.getMedia();
            if (media.getType().equalsIgnoreCase(type)) {
                filteredItems.add(mediaHandler);
            }
        }
        return filteredItems;
    }

    public void refreshMediaList() {
        setBController(new HomeController());
        try {
            List<Media> medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media) object;
                MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this);
                this.homeItems.add(m1);
            }

            this.currentPage = 0;
            this.displayedItems = this.homeItems;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        } catch (SQLException | IOException e) {
            LOGGER.info("Errors occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setImage() {
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);
    }

    public void addMediaHome(List<MediaAdminHandler> items) {
        ArrayList<MediaAdminHandler> mediaItems = (ArrayList<MediaAdminHandler>) ((ArrayList<MediaAdminHandler>) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while (!mediaItems.isEmpty()) {
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < 3 && !mediaItems.isEmpty()) {
                    MediaAdminHandler media = mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }

    public void addMenuItem(int position, String text, MenuButton menuButton) {
        // Create a menu item
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);

        // Set action
        menuItem.setOnAction(e -> {
            // Filter and sort by the selected type
            List<MediaAdminHandler> filteredItems = filterMediaByType(text);
            checkEmpty(filteredItems);
        });

        // Add to button
        menuButton.getItems().add(position, menuItem);
    }


    public void checkEmpty(List<MediaAdminHandler> filteredItems) {
        if (filteredItems.isEmpty()) {
            try {
                PopupScreen.error("No matching products.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            currentPage = 0;
            this.displayedItems = filteredItems;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(filteredItems);
            addMediaHome(displayedItems);
        }
    }


    public List<MediaAdminHandler> filterMediaByKeyWord(String keyword, List<MediaAdminHandler> items) {
        List<MediaAdminHandler> filteredItems = new ArrayList<>();
        for (Object item : items) {
            MediaAdminHandler media = (MediaAdminHandler) item;
            if (media.getMedia().getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(media);
            }
        }
        return filteredItems;
    }

    public List<MediaAdminHandler> convertMediaHandlerList(List<Media> items) throws SQLException, IOException {
        List<MediaAdminHandler> mediaHandlerList = new ArrayList<>();
        for (Object item : items) {
            Media media = (Media) item;
            MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this);
            mediaHandlerList.add(m1);
        }
        return mediaHandlerList;
    }

    public Stage getStage() {
        return this.stage;
    }
}
