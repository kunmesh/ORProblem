package com.org.scm.DTO;

import java.util.HashMap;

/**
 * 
 * @author unmesh_kulkarni
 *
 * This is used to store the product structure/composition data.
 *
 * Sample data:
 *	 A      	1      	B      	p1
 *	 A      	2      	C      	p1
 *
 */

public class ProcessDetails {

	String product;
	String process;
	HashMap<String, String> subComponent;

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public HashMap<String, String> getSubComponent() {
		return subComponent;
	}

	public void setSubComponent(HashMap<String, String> subComponent) {
		this.subComponent = subComponent;
	}


}
