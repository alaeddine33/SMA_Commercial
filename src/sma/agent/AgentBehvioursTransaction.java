/**
 * 
 */
package sma.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import sma.parameters.Config;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

/**
 * Gere l'achat 
 */
public class AgentBehvioursTransaction extends TickerBehaviour {

	private static final long serialVersionUID = 1L;

	private java.util.logging.Logger logger;
	private SmaAgent myAgentCommercial;

	//Regroupe les prix de chaque agent
	private HashMap<AID, Double[]> price_table;
	private int init_quantity;
	private AID min_seller;
	private int min_quantity;
	private double min_price;
	private double quantity_for_duplication;

	public AgentBehvioursTransaction(Agent a, long period) {
		super(a, period);
		this.logger = Logger.getMyLogger(this.getClass().getName());

		price_table = new HashMap<AID, Double[]>();

		this.init_quantity = 1;

		logger.log(Logger.CONFIG, "Create AgentBehvioursTransaction", this);

	}

	@Override
	public void onStart() {
		super.onStart();
		logger.log(Logger.INFO, "Entrée dans onStart.", this);
		this.myAgentCommercial = (SmaAgent) this.myAgent;

		myAgentCommercial.setRunningState(0);
	}

	@Override
	protected void onTick() {
		//Buying condition
		if(myAgentCommercial.getStock_consumption() == myAgentCommercial.getStock_max_consumption()){
			return;
		}

		if(myAgentCommercial.getRunningState() == 4){
			myAgentCommercial.setRunningState(0);
		}

		//Buy
		pricesResearch();
		buyProduct();

	}

	@Override
	public int onEnd() {
		logger.log(Logger.INFO, "End of transaction!", this); 	
		return super.onEnd();
	}


	public void setQuantity(int quantity) {	
		this.init_quantity = quantity;
	}

	//-----------Private Methode----------------
	private int moreSuitableQuantity(double price,int quantity){
		int test = (int) Math.min(myAgentCommercial.getStock_max_consumption()-myAgentCommercial.getStock_consumption(), Math.min(myAgentCommercial.getMoney()/price,quantity));
		return test;

	}

	/**
	 * Recherche le vendeur le moins chere
	 */
	private void pricesResearch(){
		//Get results
		DFAgentDescription[] sellers = myAgentCommercial.search();

		//send CFP
		if(myAgentCommercial.getRunningState() == 0){
			price_table.clear();
			for(DFAgentDescription seller : sellers){
				sendCFP(seller.getName());
			}
			myAgentCommercial.setRunningState(1);
			myAgentCommercial.addTransactionInit();
		}

		//wait for response
		if(price_table.size() < sellers.length && myAgentCommercial.getRunningState() == 1){
			MessageTemplate mt = MessageTemplate.MatchPerformative( ACLMessage.PROPOSE );
			ACLMessage msg = myAgent.receive(mt);
			if(msg != null) {
				logger.log(Logger.INFO, "Receive("+msg.getContent()+"): from :"+msg.getSender().getLocalName(), this);
				
				switch(msg.getPerformative()){
				case ACLMessage.PROPOSE:
					String[] propose = msg.getContent().split(" ");
					try {
						int quantity = Integer.parseInt(propose[1]);
						double price = Double.parseDouble(propose[2]);
						Double[] tmp = {price, (double) quantity};
						price_table.put(msg.getSender(), tmp);
						logger.log(Level.FINE, "Taille price_table : "+price_table.size(), this);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					break;
				default:
					break;
				}
			}
		}else if(myAgentCommercial.getRunningState() == 1){
			myAgentCommercial.setRunningState(2);
		}
	}

	/**
	 * Achete des produits a consommer
	 */
	private void buyProduct(){
		boolean one_is_accepted = false;
		if(price_table.size() > 0 && myAgentCommercial.getRunningState() >= 2){
			if(myAgentCommercial.getRunningState() == 2){

				min_price = Config.INFINI;
				min_seller = null;
				min_quantity = 0;
				

				if(myAgentCommercial.getLifeState() == 0 ){
					for(AID seller : price_table.keySet()){
						Double[] price_tmp = price_table.get(seller);
						if(moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue()) > min_quantity || min_seller == null){ //TODO
							min_price = price_tmp[0];
							min_seller = seller;
							min_quantity = moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue());
							if(min_quantity > 1){
								Random r = new Random();
								if(r.nextBoolean() == true){
									break;
								}
							}
						}
					}
				}else if(myAgentCommercial.getLifeState() == 2){
					double quantity;
					quantity = Config.INFINI;
					for(AID seller : price_table.keySet()){
						Double[] price_tmp = price_table.get(seller);
						quantity_for_duplication = (int) Config.INIT_CONSUMPTION*2 - myAgentCommercial.getStock_consumption();
						if(Math.abs(quantity_for_duplication - moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue())) < quantity || min_seller == null){ //TODO
							min_price = price_tmp[0];
							min_seller = seller;
							min_quantity = moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue());
							quantity = Math.abs(quantity_for_duplication - moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue()));
						}
					}
				}else if(myAgentCommercial.getLifeState() == 1){
					//Find cheapest seller
					for(AID seller : price_table.keySet()){
						Double[] price_tmp = price_table.get(seller);

						if((price_tmp[0] < min_price && price_tmp[1].intValue() > 0) || min_seller == null){ //TODO
							min_price = price_tmp[0];
							min_seller = seller;
							min_quantity = moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue());
						}
					}
				}else{
					for(AID seller : price_table.keySet()){
						min_price = -1;
						Double[] price_tmp = price_table.get(seller);
						if((price_tmp[0] > min_price && price_tmp[1].intValue() > 0) || min_seller == null){ //TODO
							min_price = price_tmp[0];
							min_seller = seller;
							min_quantity = moreSuitableQuantity(price_tmp[0],price_tmp[1].intValue());
						}
					}
				}
					

				//Send approuval

				for(AID seller : price_table.keySet()){
					if(seller.equals(min_seller) == false){
						sendReject_Proposal(seller);
					}else{
						if(min_quantity == 0){
							sendReject_Proposal(seller);
							logger.log(Level.INFO, "QUANTITY = 0 | money : "+myAgentCommercial.getMoney(), this);
							myAgentCommercial.setRunningState(4);
						}else{
							sendAccept_Proposal(min_seller, min_quantity, min_price);
							one_is_accepted = true;
							myAgentCommercial.setRunningState(3);
						}
					}
				}
			}

			if(one_is_accepted == true || myAgentCommercial.getRunningState() == 3){
				//Wait for confirm
				int nb_reponce = 0;
				int nb_try = 0;
				
				if(nb_reponce < 1 ){					
					nb_try++;
					MessageTemplate mt = MessageTemplate.and(
							MessageTemplate.or(MessageTemplate.MatchPerformative( ACLMessage.CONFIRM ), MessageTemplate.MatchPerformative( ACLMessage.CANCEL ))
					,MessageTemplate.MatchSender(min_seller));
					ACLMessage msg = myAgent.receive(mt);
					if(msg != null) {
						
						logger.log(Logger.INFO, "Receive("+msg.getContent()+"): from :"+msg.getSender().getLocalName(), this);
						
						nb_reponce++;
						switch(msg.getPerformative()){	
						case ACLMessage.CONFIRM:
							executeTransaction(min_quantity, min_price);
							myAgentCommercial.setRunningState(4);
							myAgentCommercial.addTransactionConfirm();
							break;
						case ACLMessage.CANCEL:
							myAgentCommercial.setRunningState(4);
							myAgentCommercial.addTransactionCancel();
							break;
						default:
							logger.log(Logger.INFO, "DEFAULT : to : "+myAgent.getLocalName() +"Receive("+msg.getContent()+"): from :"+msg.getSender().getLocalName(), this);
							myAgentCommercial.setMoney();
							break;
						}
					}else{
					}
				}
			}
		}
	}

	private void sendCFP(AID aid){
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		msg.addReceiver(aid);
		myAgent.send(msg);
	}

	private void sendAccept_Proposal(AID aid, int quantity, double price){
		ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.setContent("ACCEPT_PROPOSAL "+quantity);
		msg.addReceiver(aid);
		myAgent.send(msg);

	}

	private void sendReject_Proposal(AID aid){
		ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		msg.setContent("REJECT_PROPOSAL");
		msg.addReceiver(aid);
		myAgent.send(msg);

	}

	private void executeTransaction(int quantity, double price) {
		myAgentCommercial.buy(quantity, price);
	}


	//--------------------Private Behaviours----------------------

	private class PriceResearch extends TickerBehaviour{
		private static final long serialVersionUID = 1L;
		public PriceResearch(Agent a, long period) {
			super(a, period);
		}
		@Override
		protected void onTick() {
			pricesResearch();
		}
	}

	@Override
	public String toString() {
		return myAgent.getLocalName();
	}
}
