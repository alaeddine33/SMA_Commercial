/**
 * 
 */
package sma.parameters;
/**
 *
 */
public class Config {
	
	public static final boolean DEBUG_LOG = true;
	
	public static final int TICKER_DELAY = 1000;
	public static final int STOCK_MAX_PRODUCTION = 50;
	public static final int STOCK_MAX_CONSUMPTION = 50;
	public static final float INIT_MONEY = 100;
	public static final float INIT_PRICE = 1;
	public static final double INFINI = 99999999;
	
	//Modify prices
	public static final double PRICE_MIN_SATISFACTION = 60;
	public static final double PRICE_MAX_SATISFACTION = 100;
	public static final double PRICE_MIN_MONEY = 50;
	public static final double PRICE_MAX_MONEY = 110;
	public static final double UP_PRICE_CONSUM = 20;
	
	public static final double INIT_CONSUMPTION = STOCK_MAX_CONSUMPTION*1/4;
	
	public static final double CONST_PROD = 1;
	public static final double CONST_CONSUM = 1;
	
	
	/**
	 * Constante de reduction de la satifaction
	 * Voir methode: private void reduceSatifaction(float delta)
	 */
	public static final double CONST_REDUCE_SATIFACTION = 1;
	
}
