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
 * FXML Modify Part Controller class
 *
 * @author matt
 */
public class ModifyPartController implements Initializable {

    Inventory inventory;
    Part part;
    private final Alert validationErrorAlert = new Alert(Alert.AlertType.ERROR);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    @FXML
    private RadioButton modifyPartInhouseRadioButton;

    @FXML
    private RadioButton modifyPartOutsourcedRadioButton;

    @FXML
    private Label machineIdCompanyNameLabel;

    @FXML
    private TextField modifyPartIdAutoGen;

    @FXML
    private TextField modifyPartNameTextBox;

    @FXML
    private TextField modifyPartInvTextBox;

    @FXML
    private TextField modifyPartPriceTextBox;

    @FXML
    private TextField modifyPartMaxTextBox;

    @FXML
    private TextField machineIdCompanyNameTextBox;

    @FXML
    private TextField ModifyPartMinTextBox;

    @FXML
    private Button modifyPartSaveButton;

    @FXML
    private Button modifyPartCancelButton;



    public ModifyPartController(Inventory inventory, Part part){
        this.inventory = inventory;
        this.part = part;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the part data in the text boxes using one function
        setPartData();
    }

    /**
     * This function cancels the modification of a part in the inventory. The user is prompted if they are SURE they
     * want to cancel modifying the part and potentially lose data. If they are sure they want to cancel they are
     * redirected to the Main Screen page.
     *
     * @param event
     */
    @FXML
    void cancelModifyPart(MouseEvent event) {
        confirmationAlert.setContentText("Are you sure you want to cancel modifying this part? Your data will be lost.");
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
     * This function is the onclick event for the button to modify a part in the list of parts. The text box data is set
     * to temporary variables, and the variables are then tested to see if the data is valid or invalid. If the data is
     * all valid data entered, the part is tested to see if its Outsourced or In-House, and the data is added to the
     * respective type of Part. The part is also check if it needs to be re-cast to a different type (ie. in-house to
     * outsourced), and then that is done if needed. The part is then added to the allParts list in the Inventory.
     *
     * @param event
     */
    @FXML
    void saveModifyPart(MouseEvent event) {
        // Add user input to temp variables
        int tempId = Integer.parseInt(modifyPartIdAutoGen.getText());
        String tempName = modifyPartNameTextBox.getText().trim();
        String tempMin = ModifyPartMinTextBox.getText();
        String tempMax = modifyPartMaxTextBox.getText();
        String tempInv = modifyPartInvTextBox.getText();
        String tempPrice = modifyPartPriceTextBox.getText();
        String tempVarIdName = machineIdCompanyNameTextBox.getText();

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
        } else if (tempVarIdName.isEmpty() && modifyPartInhouseRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Machine ID field is empty.");
            validationErrorAlert.show();
        } else if (tempVarIdName.isEmpty() && modifyPartOutsourcedRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Company Name field is empty.");
            validationErrorAlert.show();
        } else if (!(isAStringAnInt(tempVarIdName)) && modifyPartInhouseRadioButton.isSelected()) {
            validationErrorAlert.setContentText("The Machine ID must be an integer.");
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
            // Check what type Part is currently, if type is changing, make a new Part object w/ that subclass. Else
            // just update the class using setProperty() function
            if (modifyPartInhouseRadioButton.isSelected() && (part instanceof InHouse)) {
                // Add objects holding user data to the Part() constructor
                part.setName(tempName);
                part.setMax(Integer.parseInt(tempMax));
                part.setMin(Integer.parseInt(tempMin));
                part.setStock(Integer.parseInt(tempInv));
                part.setPrice(Double.parseDouble(tempPrice));
                ((InHouse) part).setMachineId(Integer.parseInt(tempVarIdName));

                // Add Part to Inventory
                Inventory.updatePart(part);

            } else if (modifyPartInhouseRadioButton.isSelected() && (part instanceof Outsourced)) {
                // Create a new Inhouse object to hold Outsourced part data and then update the partInventory with it
                Part outsourcedToInhouse = new InHouse(tempId, tempName,
                        Double.parseDouble(tempPrice), Integer.parseInt(tempInv),
                        Integer.parseInt(tempMin), Integer.parseInt(tempMax), Integer.parseInt(tempVarIdName));

                // Add Part to Inventory
                Inventory.updatePart(outsourcedToInhouse);

            } else if (modifyPartOutsourcedRadioButton.isSelected() && (part instanceof InHouse)) {
                // Create a new Outsourced object to hold Inhouse part data and then update the partInventory with it
                Part inhouseToOutsourced = new Outsourced(tempVarIdName, tempId, tempName,
                        Double.parseDouble(tempPrice), Integer.parseInt(tempInv), Integer.parseInt(tempMin),
                        Integer.parseInt(tempMax));

                // Add Part to Inventory
                Inventory.updatePart(inhouseToOutsourced);

            } else if (modifyPartOutsourcedRadioButton.isSelected() && (part instanceof Outsourced)) {
                // Add objects holding user data to the Part() constructor
                part.setName(tempName);
                part.setMax(Integer.parseInt(tempMax));
                part.setMin(Integer.parseInt(tempMin));
                part.setStock(Integer.parseInt(tempInv));
                part.setPrice(Double.parseDouble(tempPrice));
                ((Outsourced) part).setCompanyName(tempVarIdName);

                // Add Part to Inventory
                Inventory.updatePart(part);
            }

            // Pass new updated inventory object to the MainScreenController
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

        } // Else end
    } // Function End

    /**
     * This function is used to select the in-house radio button, and deselect the outsourced radio button. It also
     * changes the last label text to "Machine ID."
     *
     * @param event
     */
    @FXML
    void selectInhouse(MouseEvent event) {
        // Change machineIdCompanyNameLabel text to Machine ID
        machineIdCompanyNameLabel.setText("Machine ID");
        // On click, set inHouse radio button to selected, and Outsourced to not selected
        modifyPartOutsourcedRadioButton.setSelected(false);
    }

    /**
     * This function selects the outsourced radio button, deselects the in-house radio button, and sets the last label
     * text to "Company Name."
     *
     * @param event
     */
    @FXML
    void selectOutsourced(MouseEvent event) {
        // Change machineIdCompanyNameLabel text to CompanyName
        machineIdCompanyNameLabel.setText("Company Name");
        // On click, set outSourced radio button to selected, and inHouse to not selected
        modifyPartInhouseRadioButton.setSelected(false);
    }

    /**
     *  This function checks if the controller was given a Part with the type in-house or outsourced. Once that is
     *  determined, The radio buttons and labels are set to their proper type, and the text-boxes are filled with the
     *  data of the given Part.
     *
     */
    void setPartData() {
        // Check if part selected to modify is InHouse class or Outsourced class, and set radio buttons, text
        // boxes, and labels accordingly
        if(part instanceof InHouse) {
            modifyPartInhouseRadioButton.setSelected(true);
            modifyPartOutsourcedRadioButton.setSelected(false);
            machineIdCompanyNameTextBox.setText(Integer.toString(((InHouse) part).getMachineId()));
        } else if(part instanceof Outsourced){
            modifyPartInhouseRadioButton.setSelected(false);
            modifyPartOutsourcedRadioButton.setSelected(true);
            machineIdCompanyNameLabel.setText("Company Name");
            machineIdCompanyNameTextBox.setText(((Outsourced) part).getCompanyName());
        }

        // Set each text box with its respective part property
        modifyPartIdAutoGen.setText(Integer.toString(part.getId()));
        modifyPartNameTextBox.setText(part.getName());
        modifyPartInvTextBox.setText(Integer.toString(part.getStock()));
        modifyPartPriceTextBox.setText(Double.toString(part.getPrice()));
        modifyPartMaxTextBox.setText(Integer.toString(part.getMax()));
        ModifyPartMinTextBox.setText(Integer.toString(part.getMin()));

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
