/**
 * 
 */
package sma.simulation;

import java.util.Date;
import java.util.logging.Level;

import jade.core.Agent;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 *
 */
public class Simulation extends Agent{

	private static final long serialVersionUID = 1L;

	protected void setup() {
		AgentContainer c = getContainerController();
		try {
			Logger logger = Logger.getMyLogger(this.getClass().getName());
			logger.log(Level.SEVERE, "---- Start Simulation "+new Date().toString()+" ----", this);
			
			AgentController Analyser = c.createNewAgent("Analyser", "sma.layout.AgentAnalyser", null);
			
			String[] argsA = {"A", "B"};
			String[] argsB = {"B", "C"};
			String[] argsC = {"C", "A"};
			
			
			AgentController AgentA1 = c.createNewAgent("AgentA1", "sma.agent.SmaAgent", argsA);
			AgentController AgentB1 = c.createNewAgent("AgentB1", "sma.agent.SmaAgent", argsB);
			AgentController AgentC1 = c.createNewAgent("AgentC1", "sma.agent.SmaAgent", argsC);
			
			AgentController AgentA2 = c.createNewAgent("AgentA2", "sma.agent.SmaAgent", argsA);
			AgentController AgentB2 = c.createNewAgent("AgentB2", "sma.agent.SmaAgent", argsB);
			AgentController AgentC2 = c.createNewAgent("AgentC2", "sma.agent.SmaAgent", argsC);
			
			Analyser.start();
			AgentA1.start();
			AgentB1.start();
			AgentC1.start();
			AgentA2.start();
			AgentB2.start();	
			AgentC2.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public String toString() {
		return getLocalName();
	}

}
