package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import card.Deck;
import message.GameMessage;
import message.Message;
import card.Card;
import card.CardList;
import handmodel.Hand;
import handmodel.Pair;
import handmodel.PairConsecutive;
import handmodel.Quad;
import handmodel.Single;
import handmodel.Straight;
import handmodel.Triple;
import player.Player;
import tienlen.TienLenCard;
import view.Table;
public class Client implements CardGameClient , Network
{
	
	protected int numOfPlayers;
	protected Card minCard;
	public Deck deck;
	protected ArrayList<Player> playerList;
	private ArrayList<Hand> handsOnTable;
	protected int playerID; 
	private String playerName; 
	private String serverIP; 
	private int serverPort; 
	private Socket sock;
	private ObjectOutputStream oos;
	protected int currentIdx; 
	protected boolean isBot;
	public Socket getSock() {
		return sock;
	}

	public void setSock(Socket sock) {
		this.sock = sock;
	}

	protected Table table; 
	private ObjectInputStream object_input;

	

	class ServerHandler implements Runnable
	{	
		public void run() 
		{
			Message message = null;
			
			try
			{
				while ((message = (Message) object_input.readObject()) != null)
				{
					parseMessage(message);
					System.out.println("Accepting messages Now");
				}
			} 
			
			catch (Exception exception) 
			{
				exception.printStackTrace();
			}
			
			table.repaint();
		}
	}

	public Client(int n, boolean isBot)
	{
		
		this.numOfPlayers = n;
		playerList = new ArrayList<Player>();
		
		for (int i = 0; i < 4; i++)
		{
			playerList.add(new Player());
		}
		handsOnTable = new ArrayList<Hand>();
		table = new Table(this);
		table.disable();
		if(!isBot)
		{
			this.isBot = false;
		playerName = (String) JOptionPane.showInputDialog("Nhập tên của bạn: " );
		if (playerName == null)
		{
			playerName = "Người chơi";
		}
		}
		else
		{
			this.isBot = true;
			playerName = "Bot";
		}
		makeConnection();
		table.repaint();
	}

	public int getPlayerID()
	{
		return playerID;
	}

	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		playerList.get(playerID).setName(playerName);
		System.out.println("Player name set to: " + this.playerName);
		this.playerName = playerName;
	}

	public String getServerIP()
	{
		return serverIP;
	}

	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}

	public int getServerPort()
	{
		return serverPort;
	}
	
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public void makeConnection()
	{
		serverIP = "127.0.0.1";
		serverPort = 1234;
		
		try 
		{
			sock = new Socket(this.serverIP, this.serverPort);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		try
		{
			oos = new ObjectOutputStream(sock.getOutputStream());
			object_input = new ObjectInputStream(sock.getInputStream());
		}
		
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		
		Runnable new_job = new ServerHandler();
		Thread new_thread = new Thread(new_job);
		new_thread.start();
		
		sendMessage(new Message(1, -1, this.getPlayerName()));
		sendMessage(new Message(4, -1, null));
		table.repaint();
	}

	public void parseMessage(GameMessage message)
	{
		
		if(message.getType() == Message.PLAYER_LIST)
		{
			playerID = message.getPlayerID();
			table.setActivePlayer(playerID);
			
			for (int i = 0; i < numOfPlayers; i++)
			{
				if (((String[])message.getData())[i] != null)
				{
					this.playerList.get(i).setName(((String[])message.getData())[i]);
					table.setExistence(i);
				}
			}
			
			table.repaint();
		}
		
		else if(message.getType() == Message.JOIN)
		{
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.setExistence(message.getPlayerID());
			table.repaint();
			table.printMsg("Player " + playerList.get(message.getPlayerID()).getName() + " has joined the game!\n");
		}
		
		else if(message.getType() == Message.FULL)
		{
			playerID = -1;
			table.printMsg("The game is full!\n");
			table.repaint();
		}
		
		else if(message.getType() == Message.QUIT)
		{
			table.printMsg("Player " + message.getPlayerID() + " " + playerList.get(message.getPlayerID()).getName() + " left the game.\n");
			playerList.get(message.getPlayerID()).setName("");
			table.setNotExistence(message.getPlayerID());
			if (this.endOfGame() == false)
			{
				table.disable(); 
				this.sendMessage(new Message(4, -1, null));
				for (int i = 0; i < numOfPlayers; i++)
				{
					playerList.get(i).removeAllCards();
				}
					
				table.repaint();
			}
			
			table.repaint();
		}
		
		else if(message.getType() == Message.READY)
		{
			table.printMsg("Player " + message.getPlayerID() + " is ready now!\n");
			handsOnTable = new ArrayList<Hand>();
			table.repaint();
		}
		
		else if(message.getType() == Message.START)
		{
			start((Deck)message.getData());
			table.printMsg("Game has started!\n\n");
			table.enable();
			table.repaint();
		}
		
		else if(message.getType() == Message.MOVE)
		{
			checkMove(message.getPlayerID(), (int[])message.getData());
			table.repaint();
		}
		
		else if(message.getType() == Message.MSG)
		{
		table.printChatMsg((String)message.getData());
		}
		else
		{
			table.printMsg("Wrong message type: " + message.getType());
			table.repaint();
		}
		
	}


	public void sendMessage(GameMessage message)
	{
		try 
		{
			oos.writeObject(message);
		}
		
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}


	public int getNumOfPlayers() 
	{
		return numOfPlayers;
	}

	
	public Deck getDeck()
	{
		return this.deck;
	}

	
	public ArrayList<Player> getPlayerList() 
	{
		return playerList;
	}

	public ArrayList<Hand> getHandsOnTable() 
	{
		return handsOnTable;
	}

	public int getCurrentIdx()
	{
		return currentIdx;
	}
	public int getPrIdx ()
	{
		if (currentIdx == 0)
		{
			return 3;
		}
		else
		{
			return currentIdx-1;
		}
	}
	public boolean getIsBot()
	{
		return isBot;
	}

	public void start(Deck deck) {
		
		this.deck = deck;
		
		for (int i = 0; i <numOfPlayers; i++)
		{
			playerList.get(i).removeAllCards();
		}
		
		for (int i = 0; i < numOfPlayers; i++)
		{
			for (int j = 0; j < 13; j++)
			{
				getPlayerList().get(i).addCard(this.deck.getCard(i*13+j));
			}
		}
		
		for (int i = 0; i < numOfPlayers; i++)
		{
			getPlayerList().get(i).getCardsInHand().sort();
		}
		
		minCard = null;
 		
 		for (int i = 0; i < numOfPlayers; i++)
 		{
 			for(int j = 0; j < playerList.get(i).getCardsInHand().size(); j++) {
 				Card card = playerList.get(i).getCardsInHand().getCard(j);
 				if(minCard == null) {
 					setMinCard(card);
 					currentIdx = i;
 				} 
 				else if(card.compareTo(getMinCard()) == -1) {
 					currentIdx = i;
 					setMinCard(card);
 				}
 			}
 		}		
		this.sendMessage(new Message(Message.FIRST, this.playerID, currentIdx));
		table.repaint();
		table.setActivePlayer(playerID);
		if(isBot) {
 			if(playerID == currentIdx) {
 				makeMoveAutomatically();
 			}
		}
	}

	
	public Card getMinCard() {
		return minCard;
	}

	public void setMinCard(Card minCard) {
		this.minCard = minCard;
	}

	public void makeMove(int playerID, int[] cardIdx) 
	{
		
		Message message = new Message(6, playerID, cardIdx);
		sendMessage(message);
	}
	public void makeMoveAutomatically() {
	    	
	    	int numOfHandsPlayed=handsOnTable.size();
	    	if(numOfHandsPlayed == 0) {
	    		 int[] cardIdx = {0};
	    		 makeMove(playerID, cardIdx);
	    	} 
	    	 else {
	    		 
	    		 CardList playerSelectedCards =new CardList();
				 Hand playerHand;
				 
	    		 if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName()) {
	  				
	    			int size = playerList.get(currentIdx).getCardsInHand().size(); 	
	    			for(int i=0; i<size ; i++) {
	    				
	    				// straight
	    				if(i + 2 < size) {
	    					playerSelectedCards.removeAllCards();
	    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
	    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
	    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+2) );
	    					
	    					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	    					if(playerHand != null) {
	    						int[] cardIdx = {i,i+1,i+2};
	    						makeMove(playerID, cardIdx );
	    						return;
	    					}
	    				}
	    				
	    				// pair 
	    				
	    				if(i + 1 < size) {
	    					playerSelectedCards.removeAllCards();
	    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
	    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
	    					
	    					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	    					if(playerHand != null) {
	    						int[] cardIdx = {i,i+1};
	    						makeMove(playerID, cardIdx );
	    						return;
	    					}
	    				}
	    			}
	    			
	    			playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(0) );
	  				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	  				int[] cardIdx = {0};
					makeMove(playerID, cardIdx );
					return;
	    		 } else {
	    			 
	    			int size = playerList.get(currentIdx).getCardsInHand().size(); 	
	     			for(int i=0; i<size ; i++) {
	     				
	     				// straight
	     				if(i + 2 < size) {
	     					playerSelectedCards.removeAllCards();
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+2) );
	     					
	     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	     					if(playerHand != null ) {
	     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
	     						int[] cardIdx = {i,i+1,i+2};
	     						makeMove(playerID, cardIdx );
	     						return;
	     						}
	     					}
	     				}
	     				
	     				// pair 
	     				
	     				if(i + 1 < size) {
	     					playerSelectedCards.removeAllCards();
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
	     					
	     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	     					if(playerHand != null ) {
	     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
	     						int[] cardIdx = {i,i+1};
	     						makeMove(playerID, cardIdx );
	     						return;
	     						}
	     					}
	     				}
	     				if(i < size) {
	     					playerSelectedCards.removeAllCards();
	     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
	     					
	     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
	     					if(playerHand != null ) {
	     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
	     						int[] cardIdx = {i};
	     						makeMove(playerID, cardIdx );
	     						return;
	     						}
	     					}
	     				}
	     				
	     			}
	     			
	     			makeMove(playerID, null);
	    		 }
	    	}
	    }

	public void checkMove(int playerID, int[] cardIdx)
	{
		int numOfHandsPlayed=handsOnTable.size();
		

	    
		if(cardIdx==null)
		{
			if(numOfHandsPlayed==0)
			{
				table.printMsg("Not a legal move!\n");
			}
			
			else if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName())
			{
				table.printMsg("Not a legal move!\n");
			}
			
			else
			{
				table.printMsg(playerList.get(currentIdx).getName()+": "+"{Pass}\n");
				
				if (currentIdx!=numOfPlayers-1)
				{
					++currentIdx;
				}
				
				else
				{
					currentIdx=0;
				}
				table.printMsg(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
			}
		}
		
		else 
		{
			if(numOfHandsPlayed==0)
			{
				CardList playerSelectedCards =new CardList();
				Hand playerHand;
				
				for(int i=0; i<cardIdx.length; ++i)
				{
					playerSelectedCards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));	
				}
				
				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
				
				if(playerHand==null)
				{
					table.printMsg("Not a legal move!\n");
				}
				
				else
				{
					playerHand.sort();
					
					if(minCard.compareTo(playerHand.getCard(0)) != 0)
					{
						table.printMsg("Not a legal move!\n");
					}
					
					else
					{
						table.printMsg(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
						
						for (int j=0; j<playerHand.size(); ++j)
						{
							table.printMsg(" ["+playerHand.getCard(j).toString()+"]");
						}
						
						table.printMsg("\n");
						playerList.get(currentIdx).removeCards(playerHand);   
						
						if (currentIdx!=numOfPlayers-1)
						{
							++currentIdx;
						}
						
						else
						{
							currentIdx=0;
						}
						
						handsOnTable.add(playerHand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
					}
				}
			}
			
			else
			{
				CardList playerSelectedCards =new CardList();
				Hand playerHand;
				
				for(int i=0; i<cardIdx.length; ++i)
				{
					playerSelectedCards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
					
				}
				
				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
				
				if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName())
				{
					if (playerHand==null)
					{
						table.printMsg("Not a legal move!\n");
						table.playSound("Not a legal move!");
					}
					
					else
					{
						playerHand.sort();
						table.printMsg(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
						
						for (int j=0; j<playerHand.size(); ++j)
						{
							table.printMsg(" ["+playerHand.getCard(j).toString()+"]");
						}
						
						table.printMsg("\n");
						playerList.get(currentIdx).removeCards(playerHand);
						
						if (currentIdx!=numOfPlayers -1)
						{
							++currentIdx;
						}
						
						else
						{
							currentIdx=0;
						}
						
						handsOnTable.add(playerHand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
					}
				}
				
				else
				{
					if(playerHand!=null)
					{
						if (playerHand.size()==handsOnTable.get(handsOnTable.size()-1).size())
						{
							if (handsOnTable.get(handsOnTable.size()-1).beats(playerHand)==true)
							{
								table.printMsg("Not a legal move!\n");
							}
							
							else if(playerHand!=null)
							{
								playerHand.sort();
								table.printMsg(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
								
								for (int j=0; j<playerHand.size(); ++j)
								{
									table.printMsg(" ["+playerHand.getCard(j).toString()+"]");
								}
								
								table.printMsg("\n");
								playerList.get(currentIdx).removeCards(playerHand);
								
								if (currentIdx!=numOfPlayers -1)
								{
									++currentIdx;
								}
								
								else
								{
									currentIdx=0;
								}
								
								handsOnTable.add(playerHand);
								table.printMsg(this.getPlayerList().get(currentIdx).getName()+" Please make a move!\n");
							}
						}
						
						else
						{
							table.printMsg("Not a legal move!\n");
						}
					}
					
					else
					{
						table.printMsg("Not a legal move!\n");
				
					}
				}
			}
		} 
		
		if(!endOfGame())
		{
			playerList.get(playerID).getCardsInHand().sort();
			table.resetSelected();
			
			if(this.playerID==currentIdx)
			{
				if(this.isBot)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// + new java.util.Random().nextInt(1001));
 					makeMoveAutomatically();
				}
				table.enable();
			}
			
			else
			{
				table.disable();
			}
			
			table.repaint();
		}
		
		else
		{
			
			table.repaint();
			table.printEndGameMsg();
			handsOnTable.clear();
			
			for (int i=0; i<4; ++i)
			{
				playerList.get(i).removeAllCards();
			}
			
			sendMessage(new Message(Message.READY, -1, null));
		} 
	}

	public boolean endOfGame() 
	{
		for (int i = 0; i < numOfPlayers; i++)
		{
			if (this.getPlayerList().get(i).getNumOfCards() == 0)
			{
				return true;
			}
				
		}
			
		return false;
	}

	public static Hand composeHand(Player player, CardList cards)
	{
		Hand test;
		test = new Single(player, cards);
		
		if (test.isValid())
		{
			return test;
		}
			
		test = new Pair(player, cards);
		
		if (test.isValid())
		{
			return test;
		}
		
		test = new Triple(player, cards);
		
		if (test.isValid())
		{
			return test;
		}
		
		test = new Straight(player, cards);
		
		if (test.isValid())
		{
			return test;
		}
		
		test = new Quad(player, cards);
		if (test.isValid())
		{
			return test;
		}
		test = new PairConsecutive(player, cards);
		
		if (test.isValid())
		{
			return test;
		}
		return null;
	}

}