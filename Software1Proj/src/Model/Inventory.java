/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author matt
 */
public class Inventory {
    // Create private variables to hold the lists of parts and products
    private static final ArrayList<Part> allParts = new ArrayList<>();
    private static final ArrayList<Product> allProducts = new ArrayList<>();

    /**
     * This function adds a part into the allParts Observable List.
     *
     * @param newPart
     *
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * This function adds a product into the allProducts Observable List
     *
     * @param newProduct
     *
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    


    /**
     * This function iterates through the allParts list and determines if a
     * partId that the user searched matches a part id in the list. It then
     * returns an part object if it matches
     *
     * @param partId
     * @return Part object
     *
     *      .
     */
    public static Part lookupPart(int partId){
        for(Part part : allParts){
            if(part.getId() == partId){
                return part;
            } 
        }
        return null;
    }
    


    /**
     * This function iterates through the allProducts list and determines if a
     * productId that the user searched matches a product id in the list. It then
     * returns an product object if it matches.
     *
     * @param productId
     * @return Product object
     *
     *
     */
    public static Product lookupProduct(int productId){
        for(Product product : allProducts){
            if(product.getId() == productId){
                return product;
            } 
        }
        return null;
    }


    /**
     *      This function created an ArrayList called foundParts, iterates through a list of all current
     *      parts called allParts, and for each part, it compares the name string of it to the part name
     *      that was searched for. If the resulting comparison is true, the part is added to the foundParts
     *      list. After the iteration, if the foundParts is not empty, the ArrayList is returned, else
     *      if it is empty, null is returned.
     *
     * @param partName
     * @return ArrayList
     *
     */
    public static ArrayList<Part> lookupPart(String partName){
        ArrayList<Part> foundParts = new ArrayList<>();
        for(Part part : allParts){
            if(part.getName().toLowerCase().trim().contains(partName.toLowerCase().trim())){
                foundParts.add(part);
            }
        }
        if (!(foundParts.isEmpty())){
            return foundParts;
        } else {
            return null;
        }

    }


    /**
     *   This function created an ArrayList called foundProducts, iterates through a list of all current
     *   products called allProducts, and for each product, it compares the name string of it to the product name
     *   that was searched for. If the resulting comparison is true, the product is added to the foundProduct
     *   list. After the iteration, if the foundProducts is not empty, the ArrayList is returned, else
     *   if it is empty, null is returned.
     * @param productName
     * @return ArrayList
     */
    public static ArrayList<Product> lookupProduct(String productName){
        ArrayList<Product> foundProducts = new ArrayList<>();
        for(Product product : allProducts){
            if(product.getName().toLowerCase().trim().contains(productName.toLowerCase().trim())){
                foundProducts.add(product);
            }
        }
        if (!(foundProducts.isEmpty())){
            return foundProducts;
        } else {
            return null;
        }
    }

    //

    /**
     *
     * Instead of including the integer parameter "index," I chose to use a for each loop. This function searches each
     * part in the allParts list for one with an ID that is the same as the one passed in as a parameter. When/if it
     * finds it, it replaces the element found in that position in the list with one that is the selectedPart object.
     *
     * @param selectedPart
     *
     */
    public static void updatePart(Part selectedPart){
        int countPartsInList = -1;
        for(Part part : allParts){
            countPartsInList += 1;
            if(part.getId() == selectedPart.getId()){
                allParts.set(countPartsInList, selectedPart);
            }
        }
    }


    /**
     *
     * This function works the same as the updatedPart() function, it finds a product (in the allProducts array) with
     * the same ID as the one taken in as a parameter, and then replaced said part object with the one taken in as a
     * parameter.
     *
     * @param selectedProduct
     */
    public static void updateProduct(Product selectedProduct){
        int counter = -1;
        for(Product product : allProducts){
            counter += 1;
            if(product.getId() == selectedProduct.getId()){
                allProducts.set(counter, selectedProduct);
            }
        }
    }

    /**
     *
     * This function deletes the part that the user selected in the GUI.
     *
     * @param selectedPart
     * @return boolean
     */
    public static boolean deletePart(Part selectedPart){
        for(Part part : allParts){
            if(part.getId() == selectedPart.getId()){
                return allParts.remove(part);
            }
        }
        return false;
    }

    /**
     *
     * This function deletes the product that the user selected in the GUI.
     *
     * @param selectedProduct
     * @return boolean
     *
     */
    public static boolean deleteProduct(Product selectedProduct){
        for(Product product : allProducts){
            if(product.getId() == selectedProduct.getId()){
                return allProducts.remove(product);
            }
        }
        return false;
    }

    /**
     *
     * This function returns an array list of all the parts.
     * @return ArrayList
     *
     */
    public static ArrayList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     *
     * This function returns an array list of all the products.
     *
     * @return ArrayList
     */
    public static ArrayList<Product> getAllProducts(){
        return allProducts;
    }
    
}
