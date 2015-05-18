package com.org.scm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.org.scm.DTO.OrderDetails;

/**
 * 
 * @author unmesh_kulkarni
 *
 *         This class stores all the orders from various customers.
 *
 *         Sample data:
 * 
 *         20 A C1 100 B C2
 * 
 *         It may be noted that we do not store the customer as its not required
 *         in the output.
 * 
 */

public class OrderManager {

	private HashMap<String, OrderDetails> ordermap = new HashMap<String, OrderDetails>();

		public HashMap<String, OrderDetails> getOrdermap() {
			return ordermap;
		}
		
		@Override
		public String toString() {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("	Order details : \n");
				
				for (Map.Entry<String, OrderDetails> entry : ordermap.entrySet()) {
					String key = entry.getKey();
					OrderDetails value = entry.getValue();
					stringBuffer.append("		 " + value.getQuantity() + " unit/s of " + key + "\n");
				}
				return stringBuffer.toString();
			}
		
	// read order file
	public void readOrderData(String filepath, String delimiter) {
		BufferedReader bufferedReader = null;
		OrderDetails orderdetails = null;

		try {

			String sCurrentLine;
			bufferedReader = new BufferedReader(new FileReader(filepath));
			System.out.println("Reading order data file");
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				/**
				 * Splitting the line will get us three tokens, which are
				 * quantity, product and customer
				 */

				String[] arr = sCurrentLine.split(delimiter);
				String product = arr[1].trim();
				int quantity = Integer.parseInt(arr[0].trim());
				//String customer = arr[2].trim();

				orderdetails = ordermap.get(product);

				if (orderdetails == null) {
					orderdetails = new OrderDetails();
					orderdetails.setQuantity(quantity);
					ordermap.put(product, orderdetails);
				} else {
					int orderedQuantity = ordermap.get(product).getQuantity();
					int totalDemandQuantity = orderedQuantity + quantity;
					orderdetails.setQuantity(totalDemandQuantity);
				}
			}
			System.out.println("Finished reading order data file");
		} 
		catch (FileNotFoundException fileNotFound) {
			System.out.println("File not found : " + filepath);
			fileNotFound.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
