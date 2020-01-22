/**
 * 
 */package sma.layout;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;

import sma.agent.SmaAgent;
/**
 * 
 */
public class AgentRepresentation extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JLabel satifaction;
	private JLabel production;
	private JLabel production_stock;
	private JLabel consumption;
	private JLabel consumption_stock;
	private JLabel money;
	private JLabel name;
	
	private JToolTip tip;

	public AgentRepresentation() {

		setLayout(new CircleLayout(true));
		
		satifaction = new JLabel();
		satifaction.setText("100");
		
		production = new JLabel();
		production.setText("???");
		
		production_stock = new JLabel();
		production_stock.setText("???");
		
		consumption = new JLabel();
		consumption.setText("???");
		
		consumption_stock = new JLabel();
		consumption_stock.setText("???");
		
		money = new JLabel();
		money.setText("???$");
		
		name = new JLabel();
		name.setText("<Name>");
		
		add(production);
		add(consumption);
		add(consumption_stock);
		add(production_stock);
		add(satifaction);
		add(money);
	}
	
	public void update(SmaAgent smaAgent){
		
		satifaction.setText(String.format("%.2f", smaAgent.getSatisfaction()));
		production.setText(""+smaAgent.getProduction());
		production_stock.setText((int)smaAgent.getStock_production()+"/"+(int)smaAgent.getStock_max_production()+" ");
		consumption.setText(""+smaAgent.getConsumption());
		consumption_stock.setText((int)smaAgent.getStock_consumption()+"/"+(int)smaAgent.getStock_max_consumption()+" ");
		money.setText(smaAgent.getMoney()+"$");
		name.setText(smaAgent.getLocalName());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawOval(0, 0, getPreferredSize().width-1, getPreferredSize().height-1);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(135, 135);
	}
	
}
