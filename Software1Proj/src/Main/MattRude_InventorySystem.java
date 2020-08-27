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
 *
 * @author matt
 */
public class MattRude_InventorySystem extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

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

    void addTestData(Inventory inventory) {
        // Add inHouse parts
        Part a1 = new InHouse(1, "Part A1", 2.99, 10, 5, 100, 101);
        Part a2 = new InHouse(3, "Part A2", 4.99, 11, 5, 100, 103);
        Part b = new InHouse(2, "Part B", 3.99, 9, 5, 100, 102);
        Inventory.addPart(a1);
        Inventory.addPart(a2);
        Inventory.addPart(b);
        Inventory.addPart(new InHouse(4, "Part A3", 5.99, 15, 5, 100, 104));
        Inventory.addPart(new InHouse(5, "Part A4", 6.99, 5, 5, 100, 105));

        // Add OutSourced Parts
        Part o1 = new Outsourced("Acme Co", 6, "Part O1", 2.99, 10, 5, 100);
        Part p = new Outsourced("E Corp", 7, "Part P", 3.99, 5, 5, 100);
        Part q = new Outsourced("CHOAM", 8, "Part Q", 2.99, 5, 5, 100);
        Inventory.addPart(o1);
        Inventory.addPart(p);
        Inventory.addPart(q);
        Inventory.addPart(new Outsourced("Spacing Guild", 9, "Part R", 2.99, 5, 5, 100));
        Inventory.addPart(new Outsourced("Delos Inc", 10, "Part O2", 2.99, 5, 5, 100));

        // Add AllProducts
        Product prod1 = new Product(100, "Product 1", 9.99, 20, 5, 100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(a1);
        Inventory.addProduct(prod1);
        Product prod2 = new Product(200, "Product 2", 9.99, 29, 5, 100);
        prod1.addAssociatedPart(a2);
        prod1.addAssociatedPart(p);
        Inventory.addProduct(prod2);
        Product prod3 = new Product(300, "Product 3", 9.99, 30, 5, 100);
        prod1.addAssociatedPart(b);
        prod1.addAssociatedPart(q);
        Inventory.addProduct(prod3);
        Inventory.addProduct(new Product(400, "Product 4", 29.99, 20, 5, 100));
        Inventory.addProduct(new Product(500, "Product 5", 29.99, 9, 5, 100)) ;
    }
}
