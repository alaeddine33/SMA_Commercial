/**
 * 
 */
package sma.agent;

import java.util.HashMap;
import java.util.logging.Level;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * Gere la vente 
 */

public class AgentBehvioursListener extends Behaviour {

	private static final long serialVersionUID = 1L;
	
	private HashMap<String, AgentCommercialBehvioursListenerModule> listener;
	private java.util.logging.Logger logger;
	private boolean stop = false;
	
	public AgentBehvioursListener() {
		listener = new HashMap<String, AgentCommercialBehvioursListenerModule>();
		logger = Logger.getMyLogger(this.getClass().getName());
	}
	
	@Override
	public void action() {
		//Verify msgs & create behiaviourTransaction
		MessageTemplate mt = MessageTemplate.MatchPerformative( ACLMessage.CFP );
		ACLMessage msg = myAgent.receive(mt);
		if(msg != null) {
			if(listener.containsKey(msg.getSender().toString()) == false){
				logger.log(Logger.INFO, "Create ListerModule for :"+msg.getSender(), this);
				
				AgentCommercialBehvioursListenerModule listener_agent = new AgentCommercialBehvioursListenerModule(msg.getSender());
				myAgent.addBehaviour(listener_agent);
				listener_agent.setAgent(myAgent);
				listener_agent.sendPropose(msg);
				listener.put(msg.getSender().toString(), listener_agent);
			}else{
				AgentCommercialBehvioursListenerModule listener_agent = listener.get(msg.getSender().toString());
				listener_agent.sendPropose(msg);
			}
		}
	}
	
	@Override
	public boolean done() {
		return stop ;
	}
}


class AgentCommercialBehvioursListenerModule extends Behaviour {

	private static final long serialVersionUID = 1L;
	
	private java.util.logging.Logger logger;
	private SmaAgent myAgentCommercial;
	private AID sender;
	private boolean stop = false;
	//
	private double priceSend;
	private int quantitySend;

	public AgentCommercialBehvioursListenerModule(AID sender) {
		super();
		this.sender = sender;
		this.logger = Logger.getMyLogger(this.getClass().getName());
		
		logger.log(Logger.CONFIG, "Create AgentCommercialBehvioursListener for : "+sender, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		//Message test de log
		logger.log(Logger.INFO, "Entrée dans onStart.", this);
		this.myAgentCommercial = (SmaAgent) this.myAgent;
	}

	@Override
	public void action() {
		if(myAgentCommercial != null){
			MessageTemplate mt = MessageTemplate.and( 
					MessageTemplate.or( 
							MessageTemplate.MatchPerformative( ACLMessage.CFP ), 
							MessageTemplate.or(
									MessageTemplate.MatchPerformative( ACLMessage.ACCEPT_PROPOSAL ),
									MessageTemplate.MatchPerformative( ACLMessage.REJECT_PROPOSAL ))), 
					MessageTemplate.MatchSender(sender) );
			ACLMessage msg = myAgent.receive(mt);
			if(msg != null) {
				
				logger.log(Logger.INFO, "Receive("+msg.getContent()+"): from :"+msg.getSender().getLocalName(), this);
				
				switch(msg.getPerformative()){
					case ACLMessage.CFP:
						sendPropose(msg);
						break;
					case ACLMessage.ACCEPT_PROPOSAL:
						sendConfirm(msg);
						break;
					case ACLMessage.REJECT_PROPOSAL:
						break;
					default:
						break;
				}
			}
		}else{
			if(myAgent != null){
				myAgentCommercial = (SmaAgent) myAgent;
			}
		}
	}
	
	@Override
	public int onEnd() {
		//Test log msg
		logger.log(Logger.INFO, "Entrée dans onEnd.", this); 	
		return super.onEnd();
	}

	@Override
	public boolean done() {
		return stop;
	}

	@Override
	public void setAgent(Agent a) {
		super.setAgent(a);
		this.myAgentCommercial = (SmaAgent) this.myAgent;
	}
	
	//-----------Private Methode----------------

	public void sendPropose(ACLMessage msg){
		ACLMessage reply = msg.createReply();
		
		quantitySend = (int)Math.max(myAgentCommercial.getStock_production()-1,0);
		priceSend = myAgentCommercial.getPrice();
		
		reply.setPerformative(ACLMessage.PROPOSE);
		reply.setContent("PROPOSE "+quantitySend+" "+priceSend);
		myAgent.send(reply);
	}
	
	public synchronized void sendConfirm(ACLMessage msg) {
			int quantityR;
			try {
				quantityR = Integer.parseInt(msg.getContent().split(" ")[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				
				return;
			}
			//Check Quantity
			if(quantityR <= myAgentCommercial.getStock_production()){
				//Send Confirm
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.CONFIRM);
				reply.setContent("CONFIRM "+quantityR+" "+priceSend);
				myAgent.send(reply);
				
				logger.log(Logger.INFO, "Receive(Transaction accepted! Send Confirm!): from :"+msg.getSender().getLocalName(), this);
				
				//Execute transaction
				myAgentCommercial.sell(quantityR, priceSend);
			}else{
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.CANCEL);
				myAgent.send(reply);
				logger.log(Logger.INFO, myAgent.getLocalName() +"Receive(Transaction refused! Not enough product): from :"+msg.getSender().getLocalName(), this);
			}

	}
	
	@Override
	public String toString() {
		return myAgent.getLocalName();
	}
}
