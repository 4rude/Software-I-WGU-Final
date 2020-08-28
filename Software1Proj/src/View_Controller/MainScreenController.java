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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Main Screen Controller class
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
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

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

    /**
     * This function fills the partInventory with all parts from the inventory, and then creates the id, name, stock,
     * and price columns. The mainScreenPartsTable is loaded with items from the partInventory ObservableList. The
     * table is then updated to show any changes in data to the user.
     */
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

    /**
     * This function fills the productInventory with all products from the inventory object, and then
     * creates the id, name, stock, and price columns. The mainScreenProductsTable is loaded with items from the
     * productInventory ObservableList. The table is then updated to show any changes in data to the user.
     */
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

    /**
     * This function happens on click and routes the user to the Add Part Controller, where they are able to create and add a part to the
     * inventory object.
     *
     * @param event
     */
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

    /**
     * This function, when clicked, routes the user to the Add Product controller, where they are able to create and add
     * a new product to the allProducts list in the "globally" used inventory object.
     *
     * @param event
     */
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

    /**
     * This function deletes a Part that is selected from the Part table. The user is asked to confirm that they do
     * indeed want to delete the selected Part.
     *
     * @param event
     */
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
            partInventory.setAll(Inventory.getAllParts());
            mainScreenPartsTable.setItems(partInventory);
            mainScreenPartsTable.refresh();

        }
    }


    /**
     * This function deletes a part from the inventory object. It also confirms that the user wants to do so.
     *
     * One piece of functionality I would add in an updated version of this application would be for the option to
     * delete all parts associated with this (selected) product. The button could be added to a Dialog box, and the user
     * could select that they want all parts deleted, instead of having to click modify product, then individually
     * select each part to delete and press "OK" to confirm each part they want to delete.
     *
     * @param event
     */
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

            }
        } else { // If a product does have parts associated with
            errorAlert.setContentText("You cannot delete a product that has a part associated with it.");
            errorAlert.show();
        }

    }

    /**
     * This function closes the application and makes sure any code still running is shut down.
     *
     * @param event
     */
    @FXML
    void exitMainScreen(MouseEvent event) {
        // Ends the JavaFX App. Preferred way to shut down a JavaFX application per the Oracle Docs.
        Platform.exit();
        // Shuts down the JVM.
        System.exit(0);
    }

    /**
     * This function, when the button is associated with it is clicked, routes the user to a new page which then allows
     * the user to modify the part selected from the Part table on the Main Screen.
     *
     * @param event
     */
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

    /**
     * This function, when the button is associated with it is clicked, routes the user to a new page which then allows
     * the user to modify the Product selected from the Product table on the Main Screen.
     *
     * @param event
     */
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

    /**
     * This function searches all the Part names (in the inventory) for a name matching what was typed in to the search
     * bar.
     *
     * @param event
     */
    @FXML
    void searchInvParts(MouseEvent event) {
        // If the search bar is empty, return all the parts
        if (invPartSearchBar.getText() == "") {
            partInventory.setAll(Inventory.getAllParts());

            invPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Part, Double> costColumn = formatPrice();
            mainScreenPartsTable.getColumns().addAll(costColumn);

            mainScreenPartsTable.setItems(partInventory);
        // If the search bar is not empty, lookup the part name by using the text provided
        } else {
            try {
                partInventorySearch.setAll(Inventory.lookupPart(invPartSearchBar.getText()));

                invPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                invPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                TableColumn<Part, Double> costColumn = formatPrice();
                mainScreenPartsTable.getColumns().addAll(costColumn);

                mainScreenPartsTable.setItems(partInventorySearch);
            } catch (NullPointerException e) {
            System.out.println(e + " is thrown because there is no part with that name.");
            }

        }
        // Either way refresh the Table to reflect the current state of data
        mainScreenPartsTable.refresh();
    }

    @FXML
    void searchInvProducts(MouseEvent event) {
        // If the search bar is empty, return all the products
        if (invProductsSearchBar.getText() == "") {
            productInventory.setAll(Inventory.getAllProducts());

            invProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            invProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            TableColumn<Product, Double> costColumn = formatPrice();
            mainScreenProductsTable.getColumns().addAll(costColumn);

            mainScreenProductsTable.setItems(productInventory);
        // If the search bar is not empty, lookup the product name by using the text provided
        } else {
            try {
                productInventorySearch.setAll(Inventory.lookupProduct(invProductsSearchBar.getText()));

                invProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                invProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                invProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                TableColumn<Product, Double> costColumn = formatPrice();
                mainScreenProductsTable.getColumns().addAll(costColumn);

                mainScreenProductsTable.setItems(productInventorySearch);
            } catch (NullPointerException e) {
                System.out.println(e + " is thrown because there is no product with that name.");
            }

        }
        // Either way refresh the Table to reflect the current state of data
        mainScreenProductsTable.refresh();

    }

    /**
     * This function formats the price column to put a dollar sign before each double type number.
     *
     * @param <T>
     * @return TableColumn
     */
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

    /**
     * This function clears the Part search bar, and it empties all results in the Main Screen Parts table.
     * @param event
     */
    @FXML
    void selectSearchPartsTextBox(MouseEvent event) {
        // When selected, set text box to ""
        // Remove all TableView results
        invPartSearchBar.setText("");
        mainScreenPartsTable.getItems().clear();
    }

    /**
     * This function clears the Products search bar, and empties the results in the Main Screen Products table.
     * @param event
     */
    @FXML
    void selectSearchProductsTextBox(MouseEvent event) {
        // When selected, set text box to ""
        // Remove all products TableView results
        invProductsSearchBar.setText("");
        mainScreenProductsTable.getItems().clear();
    }
    
}
