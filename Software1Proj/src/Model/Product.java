/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * This class represents a generic product object used by the Inventory System application.
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

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * This function adds the given part to the associated parts list.
     * @param part
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * This function deletes a part with the ID matching the one given in the parameter.
     *
     * @param partToRemove
     * @return boolean
     */
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


    /**
     * This function returns an associated part from the associated parts list by the ID that matches the part ID given
     * as a parameter.
     *
     * @param partToGet
     * @return
     */
    public Part getAssociatedPart(int partToGet){
        for(Part part : associatedParts){
            if(part.getId() == partToGet){
                return part;
            }
        }
        return null;
    }


    /**
     * This function returns the size of the associated parts list.
     *
     * @return integer
     */
    public int getPartsListSize(){
        return associatedParts.size();
    }

    /**
     * This function returns the list of associated parts.
     *
     * @return ObservableList
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
