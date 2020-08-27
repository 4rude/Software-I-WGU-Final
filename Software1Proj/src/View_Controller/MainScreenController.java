/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author matt
 */
public class MainScreenController implements Initializable {

    Inventory inventory;

    @FXML
    private TextField invProductsSearchBar;

    @FXML
    private TableView<Product> mainScreenProductsTable;

    @FXML
    private TableColumn<Product, Integer> invPartIdColumn;

    @FXML
    private TableColumn<Product, String> invPartNameCol;

    @FXML
    private TableColumn<Product, Integer> invPartInventoryLevelColumn;

    @FXML
    private Button invProductAddButton;

    @FXML
    private Button invProductModifyButton;

    @FXML
    private Button invProductDeleteButton;

    @FXML
    private Button invProductSearchButton;

    @FXML
    private TextField invPartSearchBar;

    @FXML
    private TableView<Part> mainScreenPartsTable;

    @FXML
    private TableColumn<Product, Integer> invProductIdColumn;

    @FXML
    private TableColumn<Product, String> invProductNameColumn;

    @FXML
    private TableColumn<Product, Integer> invProductInventoryLevelColumn;

    @FXML
    private Button invPartAddButton;

    @FXML
    private Button invPartModifyButton;

    @FXML
    private Button invPartDeleteButton;

    @FXML
    private Button invPartSearchButton;

    @FXML
    private Button mainScreenExitButton;

    private final ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private final ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private final ObservableList<Part> partInventorySearch = FXCollections.observableArrayList();
    private final ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();
    private final Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);

    public MainScreenController(Inventory inventory){
        this.inventory = inventory;
    }

    /**
     * Initializes the controller class. This also generates the parts and products tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProductsTable();

    }

    private void generatePartsTable(){
        partInventory.setAll(Inventory.getAllParts());

        invPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> costColumn = formatPrice();
        mainScreenPartsTable.getColumns().addAll(costColumn);

        mainScreenPartsTable.setItems(partInventory);
        mainScreenPartsTable.refresh();
    }

    private void generateProductsTable() {
        productInventory.setAll(Inventory.getAllProducts());

        invProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        invProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        invProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product, Double> costColumn = formatPrice();
        mainScreenProductsTable.getColumns().addAll(costColumn);

        mainScreenProductsTable.setItems(productInventory);
        mainScreenProductsTable.refresh();
    }

    @FXML
    void addPartToInv(MouseEvent event) {
     try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
         AddPartController controller = new AddPartController(inventory);
         loader.setController(controller);
         Parent root = loader.load();
         Scene scene = new Scene(root);
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();

     } catch (IOException e) {
        System.out.println(e);
     }
    }

    @FXML
    void addProductToInv(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            AddProductController controller = new AddProductController(inventory);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    void deletePartInInv(MouseEvent event) {
        // Get selected part from TableView
        Part selectedPart = mainScreenPartsTable.getSelectionModel().getSelectedItem();
        deleteAlert.setContentText("Are you sure you wish to delete this part?");
        // Display alert message
        Optional<ButtonType> alertResult = deleteAlert.showAndWait();

        if(alertResult.get() == ButtonType.CANCEL){
            deleteAlert.close();

        } else if(alertResult.get() == ButtonType.OK) {
            // Remove the selected part from the allParts ArrayList
            Inventory.deletePart(selectedPart);
            // Refresh the list of parts in the TableView
            generatePartsTable();
        }
    }

    @FXML
    void deleteProductInInv(MouseEvent event) {
        // Get selected product from TableView
        Product selectedProduct = mainScreenProductsTable.getSelectionModel().getSelectedItem();
        deleteAlert.setContentText("Are you sure you wish to delete this product?");

        // If product does not have parts associated with it
        if (selectedProduct.getPartsListSize() == 0) {
            // Display alert message
            Optional<ButtonType> alertResult = deleteAlert.showAndWait();

            if(alertResult.get() == ButtonType.CANCEL){
                deleteAlert.close();

            } else if(alertResult.get() == ButtonType.OK) {
                // Remove the selected product from the allProducts ArrayList
                Inventory.deleteProduct(selectedProduct);
                // Refresh the list of products in the TableView
                productInventory.setAll(Inventory.getAllProducts());
                mainScreenProductsTable.setItems(productInventory);
                mainScreenProductsTable.refresh();

                //generateProductsTable();
            }
        } else { // If a product does have parts associated with
            errorAlert.setContentText("You cannot delete a product that has a part associated with it.");
            errorAlert.show();
        }

    }

    @FXML
    void exitMainScreen(MouseEvent event) {
        // Ends the JavaFX App
        Platform.exit();
        // Ends the JVM
        System.exit(0);
    }

    @FXML
    void modifyPartInInv(MouseEvent event) {
        try {
            // If part is selected...
            Part selectedPart = mainScreenPartsTable.getSelectionModel().getSelectedItem();

            // If the part is an instance of the Inhouse class
            if (selectedPart instanceof InHouse) {
                InHouse selectedPartAsInhouse = (InHouse) selectedPart;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inventory, selectedPartAsInhouse);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            // If the part is an instance of the Outsourced class
            } else if (selectedPart instanceof Outsourced) {
                Outsourced selectedPartAsOutsourced = (Outsourced) selectedPart;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inventory, selectedPartAsOutsourced);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    void modifyProductInInv(MouseEvent event) {
        try {
            // If product is selected
            Product selectedProduct = mainScreenProductsTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
            ModifyProductController controller = new ModifyProductController(inventory, selectedProduct);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println(e);

        }
    }

    @FXML
    void searchInvParts(MouseEvent event) {
        // Get selected part from TableView
        // Use inventory methods to search both id and name for result
        // If result isn't empty, update TableView to show only that part
        // Else, do nothing
        if (invPartSearchBar.getText() == "") {
            partInventory.setAll(Inventory.getAllParts());

            invPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Part, Double> costColumn = formatPrice();
            mainScreenPartsTable.getColumns().addAll(costColumn);

            mainScreenPartsTable.setItems(partInventory);
        } else {
            partInventorySearch.setAll(Inventory.lookupPart(invPartSearchBar.getText()));

            invPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Part, Double> costColumn = formatPrice();
            mainScreenPartsTable.getColumns().addAll(costColumn);

            mainScreenPartsTable.setItems(partInventorySearch);
        }
        mainScreenPartsTable.refresh();
    }

    @FXML
    void searchInvProducts(MouseEvent event) {
        // Get selected products from TableView
        // Use inventory methods to search both id and name for result
        // If result isn't empty, update TableView to show only that product
        // Else, do nothing
        if (invProductsSearchBar.getText() == "") {
            productInventory.setAll(Inventory.getAllProducts());

            invProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Product, Double> costColumn = formatPrice();
            mainScreenProductsTable.getColumns().addAll(costColumn);

            mainScreenProductsTable.setItems(productInventory);
        } else {
            productInventorySearch.setAll(Inventory.lookupProduct(invProductsSearchBar.getText()));

            invProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Product, Double> costColumn = formatPrice();
            mainScreenProductsTable.getColumns().addAll(costColumn);

            mainScreenProductsTable.setItems(productInventorySearch);
        }
        mainScreenProductsTable.refresh();

    }

    private <T> TableColumn<T, Double> formatPrice(){
        TableColumn<T, Double> costColumn = new TableColumn("Price");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format column values as US Currency symbol
        costColumn.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if(!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                }


            };
        });
        return costColumn;
    }

    @FXML
    void selectSearchPartsTextBox(MouseEvent event) {
        // When selected, set text box to ""
        // Remove all TableView results
        invPartSearchBar.setText("");
        mainScreenPartsTable.getItems().clear();
    }

    @FXML
    void selectSearchProductsTextBox(MouseEvent event) {
        // When selected, set text box to ""
        // Remove all products TableView results
        invProductsSearchBar.setText("");
        mainScreenProductsTable.getItems().clear();
    }
    
}
