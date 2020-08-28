/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * This Class represents a Outsourced part used in the creation of a product object, both used within the Inventory
 * System application.
 *
 * @author matt
 */
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(String companyName, int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    /**
     * This function returns the company name that the part created by.
     *
     * @return String
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * This function sets the company name member variable within the Outsourced part object.
     *
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
