/**
 * 
 */
package sma.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import sma.layout.Analyse;

import java.io.IOException;
import java.util.Date;

/**
 *
 */
public class AgentBehviours extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	private java.util.logging.Logger logger;
	private SmaAgent myAgentCommercial;
	
	private Date last_update; 
	
	
	public AgentBehviours(Agent a, long period) {
		super(a, period);
		// Fix tick to "period" value
		setFixedPeriod(true);
		
		
		myAgentCommercial = (SmaAgent) myAgent;
		
		logger = Logger.getMyLogger(this.getClass().getName());
	}

	@Override
	public void onStart() {
		super.onStart();
		//Log msg test
		logger.log(Logger.INFO, "Entrée dans onStart.", this);
		
		last_update = new Date();
	}
	
	@Override
	protected void onTick() {
		double delta = (((new Date()).getTime() - last_update.getTime()) / 1000.0);
		
		//Log msg test
		logger.log(Logger.FINE, myAgent.getName()+" : Entrée dans onTick. delta="+delta, this); 
		
		myAgentCommercial.produce(delta);
		myAgentCommercial.consomme(delta);
		myAgentCommercial.check_lifeState();
		myAgentCommercial.check_satisfaction(delta);
		myAgentCommercial.update_price();
	
		myAgentCommercial.compute_stats(delta);
		
		last_update = new Date();
		
		//Update simulation info
		myAgentCommercial.sendInfoToAnalyser("UPDATE");
	}
	
	@Override
	public int onEnd() {
		//Log msg test
		logger.log(Logger.INFO, "Entrée dans onEnd.", this); 	
		return super.onEnd();
	}
	
	@Override
	public String toString() {
		return myAgent.getLocalName();
	}
	
}
