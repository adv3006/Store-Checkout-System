package outside;

/*
Program #4
Nhan Phan
cssc0928
*/

import data_structures.*;
import java.util.Iterator;

public class ProductLookup {
    private DictionaryADT<String, StockItem> dictionary;
   
    // Constructor.  There is no argument-less constructor, or default size
    public ProductLookup(int maxSize) {
        dictionary = new Hashtable<String, StockItem>(maxSize);
                    // new BinarySearchTree<String, StockItem> ();
                    // new BinarySearchTree<String, StockItem> ();
        
    }
       
    // Adds a new StockItem to the dictionary
    public void addItem(String SKU, StockItem item) {
        dictionary.add(SKU, item);
    }
           
    // Returns the StockItem associated with the given SKU, if it is
    // in the ProductLookup, null if it is not.
    public StockItem getItem(String SKU) {
        return dictionary.getValue(SKU);
    }
       
    // Returns the retail price associated with the given SKU value.
    // -.01 if the item is not in the dictionary
    public float getRetail(String SKU) {
        if (getItem(SKU) != null)
            return getItem(SKU).getRetail();
        return (float)(-.01);
    }
    
    // Returns the cost price associated with the given SKU value.
    // -.01 if the item is not in the dictionary
    public float getCost(String SKU) {
        if (getItem(SKU) != null)
            return getItem(SKU).getCost();
        return (float)(-.01);
    }
    
    // Returns the description of the item, null if not in the dictionary.
    public String getDescription(String SKU) {
        if (getItem(SKU) != null)
            return getItem(SKU).getDescription();
        return null;       
    }       
       
    // Deletes the StockItem associated with the SKU if it is
    // in the ProductLookup.  Returns true if it was found and
    // deleted, otherwise false.  
    public boolean deleteItem(String SKU) {
        return dictionary.delete(SKU);
    }
       
    // Prints a directory of all StockItems with their associated
    // price, in sorted order (ordered by SKU).
    public void printAll() {
        Iterator<String> itr = keys();
        while (itr.hasNext()) {
            String name = itr.next();
            System.out.println("\"" + name + "\"" +  " - " + "\"" 
                                + dictionary.getValue(name).getCost() + "\"");
        }
    }
    
    // Prints a directory of all StockItems from the given vendor, 
    // in sorted order (ordered by SKU).
    public void print(String vendor) {
        Iterator<String> itr = keys();
        while (itr.hasNext()) {
            String name = itr.next();
            if (vendor.compareTo(name) == 0)
                System.out.println(dictionary.getValue(name).toString());                
        }        
    }
    
    // An iterator of the SKU keys.
    public Iterator<String> keys() {
        return dictionary.keys();
    }
    
    // An iterator of the StockItem values.    
    public Iterator<StockItem> values() {
        return dictionary.values();
    }
}
