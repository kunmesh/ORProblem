package com.org.scm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.org.scm.DTO.InventoryDetails;
/**
 * 
 * @author unmesh_kulkarni
 * This class reads the inventory data file.
 * 
 *          Sample data: 
 * 
 *         100 A  20 B 	5 D
 */

public class InventoryManager {
	
	
	private  HashMap<String, InventoryDetails> inventorymap = new  HashMap<String, InventoryDetails>();
	
		public HashMap<String, InventoryDetails> getInventorymap() {
			return inventorymap;
		}
		
		@Override
		public String toString() {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("Inventory : \n");
				List<String> keys = new ArrayList<String>(inventorymap.keySet());
				for (String key : keys) {
					// System.out.println("Key is " + key);
					if (inventorymap.get(key).getQuantity() != 0) {
						stringBuffer.append( "	" + key + " -> " + inventorymap.get(key).getQuantity() + "\n");
					}
				}
				return stringBuffer.toString();
			}

		
	// Read inventory data file 
	public void readInventory(String filepath, String delimiter){
		BufferedReader br = null;
		InventoryDetails inventorydetails = null;
		
		try {

	        String sCurrentLine;
	        br = new BufferedReader(new FileReader(filepath));
	        System.out.println("Reading inventory data file");
	        while ((sCurrentLine = br.readLine()) != null) {
	        	
	            String[] arr = sCurrentLine.split(delimiter);
				String product = arr[1].trim();
  	            int quantity = Integer.parseInt(arr[0].trim());
  	            inventorydetails = inventorymap.get(product);
  	            
  	            if (inventorydetails == null){
  	            	inventorydetails = new InventoryDetails();
  	            	inventorydetails.setQuantity(quantity);
  	            	inventorymap.put(product, inventorydetails);
  	            }
  	            else
  	            {
  	            	int incrementedQuantity = inventorymap.get(product).getQuantity();
  	            	incrementedQuantity = incrementedQuantity + quantity;
  	            	inventorydetails.setQuantity(incrementedQuantity);
  	            	//System.out.println("IncrementedQuantity " + incrementedQuantity);
  	               inventorymap.get(product).setQuantity(incrementedQuantity);
  	             	
  	            }  	            
	        }
	        System.out.println("Finished reading inventory data file");
		
	    }catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (br != null)br.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }	 
	}
	
}
	



