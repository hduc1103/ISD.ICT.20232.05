package views.screen.home;

import common.exception.ViewCartException;
import controller.HomeController;
import controller.InvoiceListController;
import controller.ViewCartController;
import entity.cart.Cart;
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
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.invoicelist.InvoiceListHandler;
import views.screen.media.MediaDetailHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HomeScreenHandler extends BaseScreenHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private Label currentPageLabel;

    @FXML
    private ImageView cartImage;

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
    private ImageView invoiceList;

    @FXML
    private Button sortPriceButton; // Button for sorting by price

    private List<MediaHandler> homeItems; // All items fetched from the database
    private List<MediaHandler> displayedItems; // Items currently displayed on the screen

    private int currentPage = 0;
    private final int itemsPerPage = 12;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public Label getNumMediaCartLabel() {
        return this.numMediaInCart;
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        int itemCount = Cart.getCart().getListMedia().size();
        String itemLabel = (itemCount == 1 || itemCount == 0) ? " item" : " items";
        numMediaInCart.setText(itemCount + itemLabel);
        super.show();
    }

    @FXML
    private void showNextMedia(MouseEvent event) {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            List<MediaHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    @FXML
    private void showPreviousMedia(MouseEvent event) {
        if (currentPage > 0) {
            currentPage--;
            List<MediaHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    private List<MediaHandler> updateMediaDisplay(List<MediaHandler> items) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());

        List<MediaHandler> displayedItems = items.subList(startIndex, endIndex);

        int totalPages = getTotalPages();
        int currentDisplayPage = currentPage + 1;
        currentPageLabel.setText("Page " + currentDisplayPage + " of " + totalPages);

        return displayedItems;
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) displayedItems.size() / itemsPerPage);
    }

    public void addMediaHome(List<MediaHandler> items) {
        // Clear existing media items
        hboxMedia.getChildren().forEach(node -> ((VBox) node).getChildren().clear());

        // Add sorted items to the VBox containers
        for (MediaHandler mediaHandler : items) {
            // Cast each node to VBox and find the one with the least number of children
            VBox targetVBox = (VBox) hboxMedia.getChildren().stream()
                    .map(node -> (VBox) node) // Cast to VBox
                    .min(Comparator.comparingInt(v -> v.getChildren().size()))
                    .orElse((VBox) hboxMedia.getChildren().get(0));

            // Add the media content to the target VBox
            targetVBox.getChildren().add(mediaHandler.getContent());
        }
    }

    private void sortMediaByPrice(MouseEvent event) {
        List<MediaHandler> sortedItems = homeItems.stream()
                .sorted(Comparator.comparingDouble(mh -> mh.getMedia().getPrice()))
                .collect(Collectors.toList());

        this.displayedItems = sortedItems;

        // Display the sorted items
        currentPage = 0; // Reset to first page after sorting
        addMediaHome(updateMediaDisplay(this.displayedItems));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBController(new HomeController());

        try {
            List<Media> mediaList = getBController().getAllMedia(); // This might throw SQLException
            this.homeItems = mediaList.stream()
                    .map(media -> {
                        try {
                            return new MediaHandler(Configs.HOME_MEDIA_PATH, media, this); // This might throw SQLException and IOException
                        } catch (SQLException | IOException e) {
                            e.printStackTrace(); // Log the exception and continue
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out any null MediaHandlers
                    .collect(Collectors.toList());

            this.displayedItems = new ArrayList<>(this.homeItems);
            addMediaHome(updateMediaDisplay(this.displayedItems));
        } catch (SQLException e) {
            e.printStackTrace(); // Log SQLException
        }

        // Add event listener for the sort button
        sortPriceButton.setOnMouseClicked(this::sortMediaByPrice);

        // Home button
        aimsImage.setOnMouseClicked(e -> {
            List<MediaHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        });

        // Cart button
        cartImage.setOnMouseClicked(e -> {
            try {
                LOGGER.info("User clicked to view cart");
                CartScreenHandler cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException ex) {
                throw new ViewCartException(Arrays.toString(ex.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        // Invoice button
        invoiceList.setOnMouseClicked(e -> {
            try {
                InvoiceListHandler invoiceListHandler = new InvoiceListHandler(this.stage, Configs.INVOICE_LIST_PATH);
                invoiceListHandler.setHomeScreenHandler(this);
                invoiceListHandler.setBController(new InvoiceListController());
                invoiceListHandler.requestToInvoiceList(this);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setImage() {
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file2 = new File(Configs.IMAGE_PATH + "/" + "cart.png");
        Image img2 = new Image(file2.toURI().toString());
        cartImage.setImage(img2);

        File file3 = new File(Configs.IMAGE_PATH + "/" + "invoice.png");
        Image img3 = new Image(file3.toURI().toString());
        invoiceList.setImage(img3);
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
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the chosen category
            List<MediaHandler> filteredItems = new ArrayList<>();
            homeItems.forEach(me -> {
                MediaHandler media = (MediaHandler) me;
                if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())) {
                    filteredItems.add(media);
                } else {
                    if (text.equals("<20đ")) {
                        if (media.getMedia().getPrice() < 20) {
                            filteredItems.add(media);
                        }

                    } else if (text.equals("20đ-50đ")) {
                        if (media.getMedia().getPrice() >= 20 && media.getMedia().getPrice() < 50) {
                            filteredItems.add(media);
                        }
                    } else if (text.equals("50đ-100đ")) {
                        if (media.getMedia().getPrice() >= 50 && media.getMedia().getPrice() <= 100) {
                            filteredItems.add(media);
                        }
                    } else if (text.equals(">100đ")) {
                        if (media.getMedia().getPrice() > 100) {
                            filteredItems.add(media);
                        }
                    }

                    Collections.sort(filteredItems, Comparator.comparingDouble(
                            mediax -> ((MediaHandler) mediax).getMedia().getPrice()));
                }
            });
            checkEmpty(filteredItems);
        });

        // Add to button
        menuButton.getItems().add(position, menuItem);
    }

    @FXML
    void searchButtonClicked(MouseEvent event) throws SQLException, IOException {
        String searchText = searchField.getText().toLowerCase().trim();
        List<Media> medium = getBController().getAllMedia();
        List<Media> filteredMedia = getBController().filterMediaByKeyWord(searchText, medium);
        List<MediaHandler> filteredItems = convertMediaHandlerList(filteredMedia);
        checkEmpty(filteredItems);
    }

    private void checkEmpty(List<MediaHandler> filteredItems) {
        if (filteredItems.isEmpty()) {
            try {
                PopupScreen.error("No matching products.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            currentPage = 0;
            this.displayedItems = filteredItems;
            List<MediaHandler> displayedItems = updateMediaDisplay(filteredItems);
            addMediaHome(displayedItems);
        }
    }

    public List<MediaHandler> convertMediaHandlerList(List<Media> items) throws SQLException, IOException {
        List<MediaHandler> mediaHandlerList = new ArrayList<>();
        for (Media media : items) {
            MediaHandler m1 = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
            mediaHandlerList.add(m1);
        }
        return mediaHandlerList;
    }

    public void handleClickDetail(Media media) {
        MediaDetailHandler mediaDetailHandler;
        try {
            mediaDetailHandler = new MediaDetailHandler(this.stage, media, Configs.MEDIA_DETAIL_PATH);
            mediaDetailHandler.requestToDetail(this);
            mediaDetailHandler.setHomeScreenHandler(this);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
