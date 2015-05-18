package com.org.scm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import com.org.scm.DTO.ProcessDetails;

/**
 * 
 * @author unmesh_kulkarni
 * 
 *         This class reads the product composition data file.
 * 
 *         Sample data A = 1B + 2C
 */
public class ProductComposition {

	private HashMap<String, ProcessDetails> productcompositionmap = new HashMap<String, ProcessDetails>();
	private LinkedHashSet<String> orderableproductset = new LinkedHashSet<String>();
	private LinkedHashSet<String> rawmaterialset = new LinkedHashSet<String>();

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Product Composition : \n");

		for (String prod : productcompositionmap.keySet()) {
			ProcessDetails details = productcompositionmap.get(prod);
			stringBuffer.append("Product -> " + prod + "\n");
			HashMap<String, String> subcomponent = details.getSubComponent();
			for (String subkey : subcomponent.keySet()) {
				stringBuffer.append("	Subcomponent -> " + subkey
						+ " unit/s -> " + subcomponent.get(subkey) + "\n");
			}
		}
		return stringBuffer.toString();
	}

	public HashMap<String, ProcessDetails> getProductcompositionmap() {
		return productcompositionmap;
	}

	public LinkedHashSet<String> getOrderableproductset() {
		return orderableproductset;
	}

	public LinkedHashSet<String> getRawmaterialset() {
		return rawmaterialset;
	}

	// read product composition data
	public void readProductComposition(String filepath, String delimiter) {
		BufferedReader bufferedReader = null;
		ProcessDetails processdetails = null;
		try {
			String sCurrentLine;
			bufferedReader = new BufferedReader(new FileReader(filepath));
			System.out.println("Reading product composition data file");
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				/**
				 * Splitting the line will get us four tokens, which are
				 * product, qty, sub component and process
				 */
				String[] arr = sCurrentLine.split(delimiter);

				String explodableprod = arr[0].trim();
				String subcompqty = arr[1].trim();
				String subcomp = arr[2].trim();
				String process = arr[3].trim();
				orderableproductset.add(explodableprod);
				rawmaterialset.add(subcomp);

				processdetails = productcompositionmap.get(explodableprod);

				if (processdetails == null) {
					processdetails = new ProcessDetails();
					processdetails.setProduct(explodableprod);
					processdetails.setProcess(process);
					HashMap<String, String> detailsmap = new HashMap<String, String>();
					detailsmap.put(subcomp, subcompqty);
					processdetails.setSubComponent(detailsmap);
					productcompositionmap.put(explodableprod, processdetails);
					processdetails = null;
				}

				else {
					HashMap<String, String> detailsmap = new HashMap<String, String>();
					detailsmap = processdetails.getSubComponent();
					if (detailsmap.containsKey(subcomp)) {
						int subcomponentquantity = Integer.parseInt(detailsmap
								.get(subcomp)) + Integer.parseInt(subcompqty);
						detailsmap.put(subcomp,
								String.valueOf(subcomponentquantity));
					} else {

						detailsmap.put(subcomp, subcompqty);
					}
					processdetails.setSubComponent(detailsmap);
				}
			}
			System.out
					.println("Finished reading product composition data file");

		}

		catch (FileNotFoundException fileNotFound) {
			System.out.println("File not found : " + filepath);
			fileNotFound.printStackTrace();
		}

		catch (IOException e) {
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

	private boolean isCyclicDependent(String product1, String product2) {
		// checks if product1 exists in the definition of product 2
		ProcessDetails details = productcompositionmap.get(product2);
		if (details != null){
		HashMap<String, String> subcomponent = details.getSubComponent();
		
		Iterator it = subcomponent.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (product1.equalsIgnoreCase(pair.getKey().toString()))
				return true;

			if (isCyclicDependent(product1, (String) pair.getKey().toString()))
				return true;
		}
	}
		return false;
		
	}

	public boolean isCyclicDependentDefiniton() {
		// check if each product defined by the user has cyclic dependency
		for (String product : this.orderableproductset) {
			if (isCyclicDependent(product, product))
				return true;
		}
		return false;
	}
}
