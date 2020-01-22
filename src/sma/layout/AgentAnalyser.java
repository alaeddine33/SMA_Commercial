/**
 * 
 */
package sma.layout;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sma.agent.SmaAgent;
/**
 * 
 */
public class AgentAnalyser extends Agent{

	private static final long serialVersionUID = 1L;

	public AgentAnalyser() {
		
		addBehaviour(new listenerBehaviours());
		
	}
	
	
	private class listenerBehaviours extends Behaviour{

		private static final long serialVersionUID = 1L;
		
		private boolean stop = false;
		
		@Override
		public void action() {
			SmaAgent smaAgent;
			ACLMessage msg = myAgent.receive();
			if(msg != null) {
				try {
					if(msg.getPerformative() == ACLMessage.INFORM){
						smaAgent = (SmaAgent) msg.getContentObject();
						Analyse.getInstance().agent_setup(smaAgent);
					}else if(msg.getPerformative() == ACLMessage.PROPAGATE){
						smaAgent = (SmaAgent) msg.getContentObject();
						Analyse.getInstance().agent_update(smaAgent);
					}else{
						smaAgent = (SmaAgent) msg.getContentObject();
						Analyse.getInstance().agent_dead(smaAgent);
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public boolean done() {
			return stop;
		}
		@Override
		public void onStart() {
			super.onStart();
			register();
		}
		@Override
		public int onEnd() {
			deregister();
			return super.onEnd();
		}
		
		/**
		 * Enregistre l'agent 		 */
		public void register(){
			// Register 
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("SYSLOG");
			sd.setName(getName());
			dfd.addServices(sd);
			try {
				DFService.register(myAgent, dfd);
			}catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
		
		/**
		 * Retire l'agent
		 */
		public void deregister(){
			// Deregister 
			try {
				DFService.deregister(myAgent);
			}catch(FIPAException fe) {
				fe.printStackTrace();
			}
		}
	}
}
