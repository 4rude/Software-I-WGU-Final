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
public class ModifyProductController implements Initializable {

    // Initialize the inventory and product objects
    Inventory inventory;
    Product product;
    // Create Alert instances
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert validationErrorAlert = new Alert(Alert.AlertType.ERROR);
    // Create ObservableLists to be filled with parts and added to a TableView
    private final ObservableList<Part> nonAssociatedPartsList = FXCollections.observableArrayList();
    private final ObservableList<Part> modifyProductAssociatedArray = FXCollections.observableArrayList();

    // FXML variables created with Scene Builder
    @FXML
    private TextField modifyProductIdTextBox;

    @FXML
    private TextField modifyProductNameTextBox;

    @FXML
    private TextField modifyProductInvTextBox;

    @FXML
    private TextField modifyProductPriceTextBox;

    @FXML
    private TextField modifyProductMaxTextBox;

    @FXML
    private TextField modifyProductMinTextBox;

    @FXML
    private TableView<Part> modifyProductAddPartTable;

    @FXML
    private TableColumn<Part, Integer> modifyProductPartIdColumn;

    @FXML
    private TableColumn<Part, String> modifyProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductInventoryLevelColumn;

    @FXML
    private TableView<Part> modifyProductRemovePartTable;

    @FXML
    private TableColumn<Part, Integer> modifyProductAssociatedPartIdColumn;

    @FXML
    private TableColumn<Part, String> modifyProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductAssociatedInventoryLevelColumn;
    
    @FXML
    private TextField modifyProductPartSearchBar;

    @FXML
    private Button modifyProductAddPartButton;

    @FXML
    private Button saveModifyProductButton;

    @FXML
    private Button cancelModifyProductButton;

    @FXML
    private Button removeAssociatedPartButton;

    @FXML
    private Button modifyProductPartSearchButton;

    public ModifyProductController(Inventory inventory, Product product) {
        this.product = product;
        this.inventory = inventory;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeData();
    }

    /**
     * This function adds a part selected from the table with all parts in it, and adds it to the table with the parts
     * associated with the current product instance.
     *
     * @param event
     */
    @FXML
    void addPart(MouseEvent event) {
        // If no part is selected to become an associated part....
        if (modifyProductAddPartTable.getSelectionModel().isEmpty()) {
            validationErrorAlert.setContentText("You must select a part.");
            validationErrorAlert.show();
        } else {

            // Get the selected part/row in the TableView
            Part selectedPart = modifyProductAddPartTable.getSelectionModel().getSelectedItem();

            // Add the associated part to the product instance
            product.addAssociatedPart(selectedPart);

            // Refresh the TableView to show new associatedParts list
            modifyProductAssociatedArray.setAll(product.getAllAssociatedParts());
            modifyProductRemovePartTable.setItems(modifyProductAssociatedArray);
            modifyProductRemovePartTable.refresh();

        }
    }

    /**
     * This function cancels modifying a product from the inventory. The user is prompted if they are SURE they
     * want to cancel modifying the product and potentially lose their data. If they are sure they want to cancel they
     * are redirected to the Main Screen page.
     *
     * @param event
     */
    @FXML
    void cancelModifyProduct(MouseEvent event) {
        // Verify that a user wants to cancel modifying the selected product
        confirmationAlert.setContentText("Are you sure you want to cancel modifying this product? Your data will be lost.");
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

    /**
     * This function removes a part from the table with the parts associated with the current product. It also verifies
     * whether the user actually wants to delete the selected part. If they do the part is removed and the table is
     * refreshed to display the current state of data.
     *
     * @param event
     */
    @FXML
    void removeAssociatedPart(MouseEvent event) {
        // If a part is not selected, let the user know
        if (modifyProductRemovePartTable.getSelectionModel().isEmpty()) {
            validationErrorAlert.setContentText("You must select a part.");
            validationErrorAlert.show();
        } else { // If a part is selected, verify that a user wants to remove the selected part
            confirmationAlert.setContentText("Are you sure you want to remove this associated part?.");
            Optional<ButtonType> alertResult = confirmationAlert.showAndWait();

            // If the user does not want to delete the part
            if(alertResult.get() == ButtonType.CANCEL){
                confirmationAlert.close();

            } else if(alertResult.get() == ButtonType.OK){ // If the user wants to delete the selected part
                // Delete the part from the associated array list using product.deleteAssociatedPart()
                product.deleteAssociatedPart(modifyProductRemovePartTable.getSelectionModel().getSelectedItem().getId());

                // Refresh the list of associated parts in the TableView
                modifyProductAssociatedArray.setAll(product.getAllAssociatedParts());
                modifyProductRemovePartTable.setItems(modifyProductAssociatedArray);
                modifyProductRemovePartTable.refresh();
            }
        }
    }

    /**
     * This function saves the modified product to the inventory object. The values in the text boxes are added to
     * temporary variables, and those variables are tested to determine if the data entered is valid or not. If it is all valid,
     * the data is set to the member variables of the product object, and then is passed to the allProducts list in
     * the inventory object. The user is then redirected to the Main Screen.
     *
     * @param event
     */
    @FXML
    void saveModifyProduct(MouseEvent event) {
        // Create temp variables for validation
        int tempId = Integer.parseInt(modifyProductIdTextBox.getText());
        String tempName = modifyProductNameTextBox.getText().trim();
        String tempInv = modifyProductInvTextBox.getText();
        String tempPrice = modifyProductPriceTextBox.getText();
        String tempMax = modifyProductMaxTextBox.getText();
        String tempMin = modifyProductMinTextBox.getText();
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
        } else if (Integer.parseInt(tempInv) < 0 || Integer.parseInt(tempMax) < 0 || Integer.parseInt(tempMin) < 0 ||
                Double.parseDouble(tempPrice) < 0.00) {
            validationErrorAlert.setContentText("None of the values entered can be less than 0.");
            validationErrorAlert.show();
        } else if (Integer.parseInt(tempMax) < Integer.parseInt(tempMin)) {
            validationErrorAlert.setContentText("The max cannot be less than the min.");
            validationErrorAlert.show();
        } else {

            // Set temp variables to the product properties using product setters
            product.setId(tempId);
            product.setName(tempName);
            product.setMax(Integer.parseInt(tempMax));
            product.setMin(Integer.parseInt(tempMin));
            product.setStock(Integer.parseInt(tempInv));
            product.setPrice(Double.parseDouble(tempPrice));

            // Use static method updateProduct to update the current product with the new property values
            Inventory.updateProduct(product);

            // Pass the inventory object to the MainScreenController to load the main screen properly
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


    /**
     * This function displays all the parts if the search button is pressed and the text box is empty. If the text box
     * has letters in it, the string is searched and the parts table is loaded with the results of the search.
     * @param event
     */
    @FXML
    void searchParts(MouseEvent event) {
        // If nothing is entered and search button is pressed, generate the parts table
        if(modifyProductPartSearchBar.getText() == "") { generatePartsTable(); }

        // Search partInventory for part by name
        else {
            try {
                nonAssociatedPartsList.setAll(inventory.lookupPart(modifyProductPartSearchBar.getText()));

                // Load the tableview with data from the tempInventory
                modifyProductAddPartTable.setItems(nonAssociatedPartsList);
                modifyProductAddPartTable.refresh();
            } catch (NullPointerException e) {
                System.out.println(e + " is thrown because there is no part with that name.");
            }

        }
    }

    /**
     * This function initializes the text boxes with the data from the product that was passed to the Modify Product
     * Controller. It also generates the Parts and Associated Parts tables.
     */
    void initializeData() {

        // Set the product text boxes to their respective data
        modifyProductIdTextBox.setText(String.valueOf(product.getId()));
        modifyProductNameTextBox.setText(product.getName());
        modifyProductInvTextBox.setText(Integer.toString(product.getStock()));
        modifyProductMinTextBox.setText(Integer.toString(product.getMin()));
        modifyProductMaxTextBox.setText(Integer.toString(product.getMax()));
        modifyProductPriceTextBox.setText(Double.toString(product.getPrice()));

        // Initialize the TableView for Parts
        generatePartsTable();

        // Initialize the TableView for Associative Parts
        generateAssociatedPartsTable();
    }

    /**
     * This function fills the nonAssociatedPartsList with all parts from the inventory, and then creates the id, name,
     * stock, and price columns. The modifyProductAddPartTable is loaded with items from the nonAssociatedPartsList
     * ObservableList. The table is then updated to show any changes in data to the user.
     */
    private void generatePartsTable(){ ;
        nonAssociatedPartsList.setAll(inventory.getAllParts());

        modifyProductPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> costColumn = formatPrice();
        modifyProductAddPartTable.getColumns().addAll(costColumn);

        modifyProductAddPartTable.setItems(nonAssociatedPartsList);
        modifyProductAddPartTable.refresh();
    }

    /**
     * This function fills the modifyProductAssociatedArray with all parts associated with the respective product, and
     * then creates the id, name, stock, and price columns. The modifyProductRemovePartTable is loaded with items from
     * the modifyProductAssociatedArray ObservableList. The table is then updated to show any changes in data to the
     * user.
     */
    private void generateAssociatedPartsTable(){
        modifyProductAssociatedArray.setAll(product.getAllAssociatedParts());

        modifyProductAssociatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAssociatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAssociatedInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> costColumn = formatPrice();
        modifyProductRemovePartTable.getColumns().addAll(costColumn);

        modifyProductRemovePartTable.setItems(modifyProductAssociatedArray);
        modifyProductRemovePartTable.refresh();
    }

    /**
     * This function runs when the search parts text box is clicked. It empties all text in the search bar, and empties
     * all results in the table with all parts.
     *
     * @param event
     */
    @FXML
    void selectSearchPartTextBox(MouseEvent event) {
        // Set text in searchpartTextBox to ""
        modifyProductPartSearchBar.setText("");
        // Clears the TableView results
        modifyProductAddPartTable.getItems().clear();

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
     * This function tests if String input can be converted to an Integer or not.
     * @param inputString
     * @return boolean
     */
    public boolean isAStringAnInt(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * This function tests if String input can be converted to an Double.
     *
     * @param inputString
     * @return boolean
     */
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
