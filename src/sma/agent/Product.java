/**
 * 
 */
package sma.agent;

import java.util.Random;

/**
 *
 */
public enum Product {

	//Products List
	A(),
	B(),
	C();
	
	private Product() {
	}
	
	public static Product getRandom(){
		Random r = new Random();
		int index = r.nextInt(Product.values().length);
		return Product.values()[index];
	}
	
}

