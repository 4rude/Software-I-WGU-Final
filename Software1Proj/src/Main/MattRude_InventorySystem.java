/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.Inventory;
import Model.Product;
import Model.Part;
import Model.InHouse;
import Model.Outsourced;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This class sets the stage (literally Application class creates the Stage object) for the main screen page
 * that houses the Tables that are used to display Product/Part data in the application, and the buttons clicked to
 * move from "page to page." The class also is used to create test data for the controllers/models
 * (also so QA doesn't get mad).
 *
 * @author matt
 */
public class MattRude_InventorySystem extends Application {

    /**
     * This function launches the Application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function starts the Inventory System Application. It creates a new inventory object and loads it with test
     * data. Then it creates a loader object with the MainScreen.fxml doc loaded in, which in turn creates a controller
     * with the inventory object passed in. The root node is created from the Parent class, and it is passed in to an
     * instantiated scene object. The stage object (taken in as a parameter), is then loaded with the scene
     *
     * One runtime error I had with this function was getting it to load in the first place. The compiler/debugger kept
     * giving me a NullPointerException error saying that there was an issue with adding the controller to the loader
     * using setController. So after hours of research just to get the application loaded for the first time, the issue
     * turned out to be that I accidentally typed .xml instead of .fxml in the
     * .getResource("/View_Controller/MainScreen.fxml") function.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inventory = new Inventory();
        addTestData(inventory);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inventory);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * This class creates the test part and product objects which are instantiated with data and added to the Inventory
     * object.
     *
     * @param inventory
     */
    void addTestData(Inventory inventory) {
        // Add inHouse parts
        Part a1 = new InHouse(1, "Part A1", 2.99, 10, 5, 100, 101);
        Part a2 = new InHouse(3, "Part A2", 4.99, 11, 5, 100, 103);
        Part b = new InHouse(2, "Part B", 3.99, 9, 5, 100, 102);
        inventory.addPart(a1);
        inventory.addPart(a2);
        inventory.addPart(b);
        inventory.addPart(new InHouse(4, "Part A3", 5.99, 15, 5, 100, 104));
        inventory.addPart(new InHouse(5, "Part A4", 6.99, 5, 5, 100, 105));

        // Add OutSourced Parts
        Part o1 = new Outsourced("Acme Co", 6, "Part O1", 2.99, 10, 5, 100);
        Part p = new Outsourced("E Corp", 7, "Part P", 3.99, 5, 5, 100);
        Part q = new Outsourced("CHOAM", 8, "Part Q", 2.99, 5, 5, 100);
        inventory.addPart(o1);
        inventory.addPart(p);
        inventory.addPart(q);
        inventory.addPart(new Outsourced("Spacing Guild", 9, "Part R", 2.99, 5, 5, 100));
        inventory.addPart(new Outsourced("Delos Inc", 10, "Part O2", 2.99, 5, 5, 100));

        // Add AllProducts
        Product prod1 = new Product(100, "Product 1", 9.99, 20, 5, 100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(a1);
        inventory.addProduct(prod1);
        Product prod2 = new Product(200, "Product 2", 9.99, 29, 5, 100);
        prod1.addAssociatedPart(a2);
        prod1.addAssociatedPart(p);
        inventory.addProduct(prod2);
        Product prod3 = new Product(300, "Product 3", 9.99, 30, 5, 100);
        prod1.addAssociatedPart(b);
        prod1.addAssociatedPart(q);
        inventory.addProduct(prod3);
        inventory.addProduct(new Product(400, "Product 4", 29.99, 20, 5, 100));
        inventory.addProduct(new Product(500, "Product 5", 29.99, 9, 5, 100)) ;
    }
}
