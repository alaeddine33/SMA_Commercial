/**
 * 
 */
package sma.layout;

import sma.agent.SmaAgent;
import sma.agent.Product;
/**
 * 
 */
public class Stats {

	private String name;
	
	private String status;
	private double life_time;
	
	private Product production;
	private double stock_production;
	private double stock_max_production;
	private double price;
	
	private Product consumption;
	private double stock_consumption;
	private double stock_max_consumption;
	
	private double money;
	private double satisfaction;
	
	private double average_price;
	private double average_satifaction;
	private double average_money;
	
	private double lifeState;
	private int runningState;
	
	private int transactionConfirm;
	private int transactionCancel;
	private int transactionInit;
	
	public Stats(SmaAgent agent) {
		update(agent);
	}

	public void update(SmaAgent agent){
		name = agent.getName();
		
		life_time = agent.getLife_time();
		
		lifeState = agent.getLifeState();
		runningState = agent.getRunningState();
		
		transactionCancel = agent.getTransactionCancel();
		transactionConfirm = agent.getTransactionConfirm();
		transactionInit = agent.getTransactionInit();
		
		production = agent.getProduction();
		stock_production = agent.getStock_production();
		stock_max_production = agent.getStock_max_production();
		price = agent.getPrice();
		
		consumption = agent.getConsumption();
		stock_consumption = agent.getStock_consumption();
		stock_max_consumption = agent.getStock_max_consumption();
		
		money = agent.getMoney();
		satisfaction = agent.getSatisfaction();
		
		average_price = agent.getAverage_price();
		average_money = agent.getAverage_money();
		average_satifaction = agent.getAverage_satifaction();
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getName() {
		return name;
	}

	public Product getProduction() {
		return production;
	}

	public double getStock_production() {
		return stock_production;
	}

	public double getStock_max_production() {
		return stock_max_production;
	}

	public double getPrice() {
		return price;
	}

	public Product getConsumption() {
		return consumption;
	}

	public double getStock_consumption() {
		return stock_consumption;
	}

	public double getStock_max_consumption() {
		return stock_max_consumption;
	}

	public double getMoney() {
		return money;
	}

	public double getSatisfaction() {
		return satisfaction;
	}
	
	public String getStatus() {
		if(status == null){
			return "";
		}
		return status;
	}
	
	public double getAverage_money() {
		return average_money;
	}
	
	public double getAverage_price() {
		return average_price;
	}
	
	public double getAverage_satifaction() {
		return average_satifaction;
	}
	
	public double getLife_time() {
		return life_time;
	}
	
	public double getLifeState() {
		return lifeState;
	}
	
	public int getRunningState() {
		return runningState;
	}
	
	public int getTransactionCancel() {
		return transactionCancel;
	}
	
	public int getTransactionConfirm() {
		return transactionConfirm;
	}
	
	public int getTransactionInit() {
		return transactionInit;
	}
	
}
