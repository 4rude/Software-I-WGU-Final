/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 *
 * @author matt
 */
public class Product {
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    
    public boolean deleteAssociatedPart(int partToRemove){
        for(Part part : associatedParts){
            if(part.getId() == partToRemove){
                // The .remove() method returns true if the part is found in the ArrayList
                return associatedParts.remove(part);
            }
        }
        // Else return false (no matching part found)
        return false;
    }
    
    public Part getAssociatedPart(int partToGet){
        for(Part part : associatedParts){
            if(part.getId() == partToGet){
                return part;
            }
        }
        return null;
    }

    public int getPartsListSize(){
        return associatedParts.size();
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
