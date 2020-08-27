/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.Inventory;
import Model.Part;
import Model.Product;
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
 * FXML Controller class
 *
 * @author matt
 */
public class AddProductController implements Initializable {

    // Constructor used to get Inventory data from MainScreenController
    public AddProductController(Inventory inventory) {
        this.inventory = inventory;
    }

    // Instance variables
    Inventory tempInventory;
    Inventory inventory;
    Product product = new Product(000, "Name", 0.00, 0, 0, 0);

    // Java FXML Objects used to communicate with the .fxml file
    // TableView with allParts
    @FXML
    private TableView<Part> addProductNewPartTable;

    @FXML
    private TableColumn<Part, Integer> addProductPartIdColumn;

    @FXML
    private TableColumn<Part, String> addProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addProductInventoryLevelColumn;

    @FXML
    private TextField addProductIdAutoGen;

    @FXML
    private TextField addProductNameTextBox;

    @FXML
    private TextField addProductInvTextBox;

    @FXML
    private TextField addProductPriceTextBox;

    @FXML
    private TextField addProductMaxTextBox;

    @FXML
    private TextField addProductMinTextBox;

    // All Parts Associated with the Product
    @FXML
    private TableView<Part> addProductRemovePartTable;

    @FXML
    private TableColumn<Part, Integer> addProductAssociatedPartId;

    @FXML
    private TableColumn<Part, String> addProductAssociatedPartName;

    @FXML
    private TableColumn<Part, Integer> addProductAssociatedInventoryLevel;

    @FXML
    private TextField addProductPartSearchBar;

    @FXML
    private Button addProductAddPartButton;

    @FXML
    private Button saveAssociatedPartButton;

    @FXML
    private Button cancelAddProductButton;

    @FXML
    private Button removeAssociatedPartButton;

    @FXML
    private Button addProductPartSearchButton;

    // Variables used only in the AddProductController class
    private final ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private final ObservableList<Part> newProductAssociatedArray = FXCollections.observableArrayList();
    private final Alert validationErrorAlert = new Alert(Alert.AlertType.ERROR);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private int counter;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeData();

    }

    @FXML
    void cancelAddProduct(MouseEvent event) {

        confirmationAlert.setContentText("Are you sure you want to cancel adding a product? Your data will be lost.");
        Optional<ButtonType> alertResult = confirmationAlert.showAndWait();

        if(alertResult.get() == ButtonType.CANCEL){
            confirmationAlert.close();

        } else if(alertResult.get() == ButtonType.OK){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inventory);
                loader.setController(controller);
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    void removeAssociatedPart(MouseEvent event) {

        if (addProductRemovePartTable.getSelectionModel().isEmpty()) {
            validationErrorAlert.setContentText("You must select a part.");
            validationErrorAlert.show();
        } else {
            confirmationAlert.setContentText("Are you sure you want to remove this associated part?.");
            Optional<ButtonType> alertResult = confirmationAlert.showAndWait();

            if(alertResult.get() == ButtonType.CANCEL){
                confirmationAlert.close();

            } else if(alertResult.get() == ButtonType.OK){

                // Delete the part from the associated array list
                // product.deleteAssociatedPart()
                product.deleteAssociatedPart(addProductRemovePartTable.getSelectionModel().getSelectedItem().getId());

                // Refresh original tempInventory parts list
                partInventory.setAll(tempInventory.getAllParts());
                addProductNewPartTable.setItems(partInventory);
                addProductNewPartTable.refresh();

                // Refresh the list of associated parts in the TableView
                newProductAssociatedArray.setAll(product.getAllAssociatedParts());
                addProductRemovePartTable.setItems(newProductAssociatedArray);
                addProductRemovePartTable.refresh();
            }
        }
    }

    @FXML
    void searchPart(MouseEvent event) {
        // If nothing is entered and search button is pressed, generate the parts table
        if(addProductPartSearchBar.getText() == "") { generatePartsTable(); }
        // Search partInventory for part by name
        else {
            // Check if the user inputted an Part ID or Name
            if (tempInventory.lookupPart(addProductPartSearchBar.getText()) == null) {
                partInventory.setAll(tempInventory.lookupPart(Integer.parseInt(addProductPartSearchBar.getText())));
            } else {
                partInventory.setAll(tempInventory.lookupPart(addProductPartSearchBar.getText()));
            }

            // Load the tableview with data from the tempInventory
            addProductNewPartTable.setItems(partInventory);
            addProductNewPartTable.refresh();
        }

    }

    @FXML
    void addPartToAssociatedParts(MouseEvent event) {

        if (addProductNewPartTable.getSelectionModel().isEmpty()) {
            validationErrorAlert.setContentText("You must select a part.");
            validationErrorAlert.show();
        } else {

            // Get the selected part/row in the TableView
            Part selectedPart = addProductNewPartTable.getSelectionModel().getSelectedItem();

            // Add the associated part to the product instance
            product.addAssociatedPart(selectedPart);

            // Refresh the TableView to show new associatedParts list
            newProductAssociatedArray.setAll(product.getAllAssociatedParts());
            addProductRemovePartTable.setItems(newProductAssociatedArray);
            addProductRemovePartTable.refresh();

            // Refresh the tempInventory based addProductNewPartTable TableView
//        generatePartsTable();
//            partInventory.setAll(tempInventory.getAllParts());
//            addProductNewPartTable.setItems(partInventory);
//            addProductNewPartTable.refresh();

        }

    }

    @FXML
    void saveProduct(MouseEvent event) {
        // Create temp variables for validation
        int tempId = Integer.parseInt(addProductIdAutoGen.getText());
        String tempName = addProductNameTextBox.getText().trim();
        String tempInv = addProductInvTextBox.getText();
        String tempPrice = addProductPriceTextBox.getText();
        String tempMax = addProductMaxTextBox.getText();
        String tempMin = addProductMinTextBox.getText();
        Double totalPriceOfParts = 0.00;

        /* For each part in the list of associated product parts, add the price to the totalPriceOfParts variable */
        for(Part part : product.getAllAssociatedParts()){
            totalPriceOfParts += part.getPrice();
        }

        // Validate user input
        if (tempName.isEmpty()) {
            validationErrorAlert.setContentText("The product Name field is empty.");
            validationErrorAlert.show();
        } else if (tempInv.isEmpty()) {
            validationErrorAlert.setContentText("The Inventory field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringAnInt(tempInv))) {
                validationErrorAlert.setContentText("The Inventory value entered is not an integer.");
                validationErrorAlert.show();
        } else if (tempMax.isEmpty()) {
            validationErrorAlert.setContentText("The Max field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringAnInt(tempMax))) {
                    validationErrorAlert.setContentText("The Max value entered is not an integer.");
                    validationErrorAlert.show();
        } else if (tempMin.isEmpty()) {
            validationErrorAlert.setContentText("The Min field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringAnInt(tempMin))) {
                    validationErrorAlert.setContentText("The Min value entered is not an integer.");
                    validationErrorAlert.show();
        } else if (tempPrice.isEmpty()) {
            validationErrorAlert.setContentText("The Price field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringADouble(tempPrice))) {
            validationErrorAlert.setContentText("The Price value entered is not a double.");
            validationErrorAlert.show();
        } else if (Double.parseDouble(tempPrice) < totalPriceOfParts) {
            validationErrorAlert.setContentText("The Price of the product must be greater than the total price of the parts.");
            validationErrorAlert.show();
        } else if (Integer.parseInt(tempInv) > Integer.parseInt(tempMax)) {
            validationErrorAlert.setContentText("The inventory cannot be greater than the max number of products.");
            validationErrorAlert.show();
        } else if (Integer.parseInt(tempInv) < Integer.parseInt(tempMin)) {
            validationErrorAlert.setContentText("The inventory cannot be less than the minimum number of products.");
            validationErrorAlert.show();
        } else if (Integer.parseInt(tempInv) < 0 || Integer.parseInt(tempMax) < 0 || Integer.parseInt(tempMin) < 0 || Double.parseDouble(tempPrice) < 0.00) {
            validationErrorAlert.setContentText("None of the values entered can be less than 0.");
            validationErrorAlert.show();
        } else if (Integer.parseInt(tempMax) < Integer.parseInt(tempMin)) {
            validationErrorAlert.setContentText("The max cannot be less than the min.");
            validationErrorAlert.show();
        } else {

            product.setId(tempId);
            product.setName(tempName);
            product.setMax(Integer.parseInt(tempMax));
            product.setMin(Integer.parseInt(tempMin));
            product.setStock(Integer.parseInt(tempInv));
            product.setPrice(Double.parseDouble(tempPrice));
            Inventory.addProduct(product);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inventory);
                loader.setController(controller);
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            } catch (IOException e) {
                System.out.println(e);
            }
        }




        // Set up constructor with user input and create product object
        // Add product object to allParts ArrayList
    }

    @FXML
    void selectSearchPartTextBox(MouseEvent event) {
        // When pressed, clear text box
        // Also clear list of associatedParts shown in TableView
        addProductPartSearchBar.setText("");
        addProductNewPartTable.getItems().clear();

    }

    void initializeData() {
        // Counts the number of products and creates a auto generated ID for the new product
        counter = tempInventory.getAllProducts().size();
        counter += 1;
        addProductIdAutoGen.setText(Integer.toString(counter) + "00");

        // Initialize the TableView for Parts
        generatePartsTable();

        // Initialize the TableView for Associative Parts
        generateAssociatedPartsTable();

    }

    private void generatePartsTable(){
        partInventory.setAll(tempInventory.getAllParts());

        addProductPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> costColumn = formatPrice();
        addProductNewPartTable.getColumns().addAll(costColumn);

        addProductNewPartTable.setItems(partInventory);
        addProductNewPartTable.refresh();
    }

    private void generateAssociatedPartsTable(){
        newProductAssociatedArray.setAll(product.getAllAssociatedParts());

        addProductAssociatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAssociatedInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> costColumn = formatPrice();
        addProductRemovePartTable.getColumns().addAll(costColumn);

        addProductRemovePartTable.setItems(newProductAssociatedArray);
        addProductRemovePartTable.refresh();
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

    // Tests if String input can be converted to an Integer
    public boolean isAStringAnInt(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    // Tests if String input can be converted to an Double
    public boolean isAStringADouble(String inputString) {
        try {
            Double.parseDouble(inputString);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }
}
