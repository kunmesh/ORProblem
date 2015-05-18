package com.org.scm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.org.scm.DTO.InventoryDetails;
import com.org.scm.DTO.OrderDetails;
import com.org.scm.DTO.ProcessDetails;

public class MainClass {

	public static void main(String[] args) {

		// Queue for products
		Queue<String> demandqueue = new LinkedList<String>();

		// read Product composition data
		HashMap<String, ProcessDetails> productcompositionmap;
		ProductComposition productcomposition = new ProductComposition();
		productcomposition.readProductComposition("productcomposition.txt", "\t");
		productcompositionmap = productcomposition.getProductcompositionmap();
		
		// check cyclic dependency
		if (productcomposition.isCyclicDependentDefiniton()) {
			System.out.println("The product definition has cyclic dependency. Cannot proceed further. Good Bye ...");
			return;
		}
		
		// get raw material for product
		productcomposition.getRawmaterialset().removeAll(
				productcomposition.getOrderableproductset());

		System.out.println("+++ Product composition : " + productcomposition);

		// read inventory data
		HashMap<String, InventoryDetails> inventorycompositionmap;
		InventoryManager inventorymanager = new InventoryManager();
		inventorymanager.readInventory("inventory.txt", "\t");
		inventorycompositionmap = inventorymanager.getInventorymap();
		System.out.println("+++ Inventory : " + inventorymanager);

		// read order data
		HashMap<String, OrderDetails> demandcompositionmap;
		HashMap<String, OrderDetails> customerdemandcompositionmap;
		OrderManager ordermanager = new OrderManager();
		ordermanager.readOrderData("order.txt", "\t");
		customerdemandcompositionmap = ordermanager.getOrdermap();
		demandcompositionmap = ordermanager.getOrdermap();

		System.out.println("+++ Demand : " + ordermanager);
		
		// initialize queue; populate queue with customer demand
		if (demandcompositionmap != null) {
			for (Map.Entry<String, OrderDetails> entry : demandcompositionmap.entrySet()) {
				String ordqty = entry.getKey();
				// OrderDetails value = entry.getValue();
				demandqueue.add(ordqty);
				// System.out.println("Ordered product in order map  " + key +
				// " Value is " + value.getQuantity());
			}
		}

		// process orders
		System.out.println("**********  Processing Orders *************");
		if (demandcompositionmap != null) {
			while (!demandqueue.isEmpty()) {
				// String product = (String) demandqueue.remove();
				String product = (String) demandqueue.element();
				System.out.println("Processing order for " + product);
				OrderDetails demanddetails = demandcompositionmap.get(product);
				int demandquantity = demanddetails.getQuantity();
				System.out
						.println(" 	Demand is " + demanddetails.getQuantity());
				InventoryDetails inventorydetails = inventorycompositionmap
						.get(product);
				if (inventorydetails == null) {
					inventorydetails = new InventoryDetails();
					inventorydetails.setQuantity(0);
				}

				int inventoryquantity = inventorydetails.getQuantity();
				System.out.println("	Inventory is "
						+ inventorydetails.getQuantity());
				if (demandquantity <= inventoryquantity) {
					inventoryquantity = inventoryquantity - demandquantity;
					demandquantity = 0;
					demandcompositionmap.remove(product);
					InventoryDetails modifiedinventorydetails = new InventoryDetails();
					modifiedinventorydetails.setQuantity(inventoryquantity);
					inventorycompositionmap.put(product,
							modifiedinventorydetails);
					System.out.println("Demand for product  " + product
							+ " fulfilled from inventory. Reduced inventory : "
							+ modifiedinventorydetails.getQuantity());

				} else {
					demandquantity = demandquantity - inventoryquantity;
					inventoryquantity = 0;
					inventorycompositionmap.remove(product);
					OrderDetails modifieddemanddetails = new OrderDetails();
					modifieddemanddetails.setQuantity(demandquantity);
					demandcompositionmap.put(product, modifieddemanddetails);
					ProcessDetails prodcomposition = productcompositionmap.get(product);
					if (prodcomposition != null) {
						HashMap<String, String> intermidatelevels = prodcomposition.getSubComponent();
						for (String subkey : intermidatelevels.keySet()) {
							String subcomponentname = subkey;
							int subcomponentquantity = Integer.parseInt(intermidatelevels.get(subkey));
							System.out.println(" Product " + product + " is composed of " + subcomponentquantity + " Unit of " + subcomponentname);
							subcomponentquantity = subcomponentquantity	* demandquantity;
							if (demandcompositionmap.containsKey(subcomponentname)) {
								int existingdemand = demandcompositionmap.get(subcomponentname).getQuantity();
								int ehnanceddemand = existingdemand	+ subcomponentquantity;
								OrderDetails enhanceddemanddetails = new OrderDetails();
								enhanceddemanddetails.setQuantity(ehnanceddemand);
								demandcompositionmap.put(subcomponentname, enhanceddemanddetails);
								System.out.println(" 	Updated demand for " + subcomponentname	+ " is " + demandcompositionmap.get(subcomponentname).getQuantity());
							} else {
								System.out.println(" 	Existing demand for " + subcomponentname + " is 0");
								OrderDetails subcomponentdemand = new OrderDetails();
								subcomponentdemand.setQuantity(subcomponentquantity);
								demandcompositionmap.put(subcomponentname, subcomponentdemand);
								demandqueue.add(subcomponentname);
							}
						} // end of subcomponent product for loop
						demandcompositionmap.remove(product);

					} else {

						System.out.println("Product composition data for " + product + " not available");
					}
				}
				demandqueue.remove();
				demanddetails = null;
				inventorydetails = null;
				System.out.println("********** Order processing ends *********");
			}
		}
		// Print inventory.
		System.out.println("\n Available  " + inventorymanager);

		// Print raw material to be ordered
		System.out.print(" Buy raw materials : \n " + ordermanager);

	}
}
