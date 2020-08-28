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

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Add Part Controller class
 *
 * @author matt
 */
public class AddPartController implements Initializable {

    // Initialize the inventory object
    private final Inventory inventory;

    // JavaFX XML objects used to represent the GUI
    @FXML
    private RadioButton addPartInhouseRadioButton;

    @FXML
    private RadioButton addPartOutsourcedRadioButton;

    @FXML
    private Label companyNameMachineIdLabel;

    @FXML
    private TextField addPartIdAutoGenTextBox;

    @FXML
    private TextField addPartNameTextBox;

    @FXML
    private TextField addPartInvTextBox;

    @FXML
    private TextField addPartPriceTextBox;

    @FXML
    private TextField addPartMaxTextBox;

    @FXML
    private TextField companyNameMachineIdTextBox;

    @FXML
    private TextField addPartMinTextBox;

    @FXML
    private Button addPartSaveButton;

    @FXML
    private Button addPartCancelButton;

    // Creates the counter integer to be used in generating an ID
    private int counter;
    // Alert Dialog Box objects used for confirmation and error handling
    private final Alert validationErrorAlert = new Alert(Alert.AlertType.ERROR);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    public AddPartController(Inventory inventory) {
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
     * This function creates a new ID for a newly created Part. It also sets the radio buttons to Inhouse as the
     * default.
     */
    private void initializeData(){
        // set the id to the next available id, can be random
        // Get list of part id's, determine highest. Add one and set to id variable.
        // Set id to addPartIdAutoGenTextBox
        // Auto selects the inHouse radio button, and deselects the Outsourced radio button
        ArrayList countArray = new ArrayList(Inventory.getAllParts());
        counter = countArray.size();
        counter += 1;

        for (Part part: Inventory.getAllParts()) {
            if (counter == part.getId()) {
                counter += 1;
            }
        }
        addPartIdAutoGenTextBox.setText(Integer.toString(counter));
        addPartInhouseRadioButton.setSelected(true);
        addPartOutsourcedRadioButton.setSelected(false);

    }

    /**
     * This function is the onclick event for the button to add the new part to list of parts. The text box data is set
     * to temporary variables, and the variables are then tested to see if the data is valid or invalid. If the data is
     * all valid data entered, the part is tested to see if its Outsourced or In-House, and the data is added to the
     * respective type of Part. The part is then added to the allParts list in the Inventory.
     *
     * @param event
     */
    @FXML
    void addPart(MouseEvent event)  {
        // Add user input to temp variables
        int tempId = Integer.parseInt(addPartIdAutoGenTextBox.getText());
        String tempName = addPartNameTextBox.getText().trim();
        String tempMin = addPartMinTextBox.getText();
        String tempMax = addPartMaxTextBox.getText();
        String tempInv = addPartInvTextBox.getText();
        String tempPrice = addPartPriceTextBox.getText();
        String tempVarIdName = companyNameMachineIdTextBox.getText();

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
        } else if (tempVarIdName.isEmpty() && addPartInhouseRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Machine ID field is empty.");
            validationErrorAlert.show();
        } else if (tempVarIdName.isEmpty() && addPartOutsourcedRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Company Name field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringAnInt(tempVarIdName)) && addPartInhouseRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Machine ID must be an integer.");
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

            // If validation succeeds
            if(addPartInhouseRadioButton.isSelected()){
                // Add objects holding user data to the Part() constructor
                Part inHousePart = new InHouse(tempId,
                        tempName, Double.parseDouble(tempPrice),
                        Integer.parseInt(tempInv),
                        Integer.parseInt(tempMin),Integer.parseInt(tempMax),
                        Integer.parseInt(tempVarIdName));

                // Add Part to Inventory
                Inventory.addPart(inHousePart);

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

            } else if(addPartOutsourcedRadioButton.isSelected()){
                // Add objects holding user data to the Part() constructor
                Part outsourcedPart = new Outsourced(tempVarIdName, tempId,
                        tempName, Double.parseDouble(tempPrice),
                        Integer.parseInt(tempInv), Integer.parseInt(tempMin),
                        Integer.parseInt(tempMax));

                // Add Part to Inventory
                Inventory.addPart(outsourcedPart);

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

    }


    /**
     * This function cancels adding a new part to the inventory. A confirmation dialog box is pops up, and verifies that
     * the user does indeed want to NOT add a new part to the inventory. The function then returns the user to the
     * Main Screen.
     * @param event
     */
    @FXML
    void cancelAddPart(MouseEvent event) {
        confirmationAlert.setContentText("Are you sure you want to cancel adding a part? Your data will be lost.");
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
     * This function is an OnClick event for the In-House radio button. When it is selected, the other radio button is
     * deselected and the last label in the list of labels/text boxes is set to Machine ID.
     *
     * @param event
     */
    @FXML
    void selectInhouseRadioButton(MouseEvent event) {
        // Selects the InHouse radio button
        // Deselects the Outsourced Radio Button
        // Changes the MachineId label to Machine ID
        addPartInhouseRadioButton.setSelected(true);
        addPartOutsourcedRadioButton.setSelected(false);
        companyNameMachineIdLabel.setText("Machine ID");
    }

    /**
     * This function is an OnClick event for the Outsourced radio button. When it is selected, the other radio button is
     * deselected and the last label in the list of labels/text boxes is set to Company Name.
     * @param event
     */
    @FXML
    void selectOutsourcedRadioButton(MouseEvent event) {
        // Selects the Outsourced radio button
        // Deselects the InHouse radio button
        // Changed MachineId label to Company Name
        addPartInhouseRadioButton.setSelected(false);
        addPartOutsourcedRadioButton.setSelected(true);
        companyNameMachineIdLabel.setText("Company Name");
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
