/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * This Class represents a In-house created part used in the creation of a product object, both used within the
 * Inventory System application.
 *
 * @author matt
 */
public class InHouse extends Part {
    private int machineId;
    
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id,
                name,
                price,
                stock,
                min,
                max);
        this.machineId = machineId;
        
    }


    /**
     * This function returns the machineID of the machine that created said in-house part.
     * @return integer
     */
    public int getMachineId() {
        return machineId;
    }


    /**
     * This function sets the Machine ID of the machine that created the respective part.
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
 }
