package samloc;

import card.Deck;
import game.Client;
import card.Card;

public class SamLocClient extends Client{
	public SamLocClient(int n)
	{
		super(n,false);
	}
	@Override
	public void start(Deck deck)
	{
		this.deck = deck;
 		
 		for (int i = 0; i < numOfPlayers; i++)
 		{
 			playerList.get(i).removeAllCards();
 		}
 		
 		for (int i = 0; i < numOfPlayers; i++)
 		{
 			for (int j = 0; j < 10; j++)
 			{
 				getPlayerList().get(i).addCard(this.deck.getCard(i*10+j));
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
 		System.out.println(playerList.get(playerID).getCardsInHand());
 		
 		System.out.println(currentIdx  + " " + minCard );
 		table.repaint();
 		table.setActivePlayer(playerID);
 		table.enable();
 		
 		if(isBot) {
 			if(playerID == currentIdx) {
 				makeMoveAutomatically();
 			}
 		}
	}

}
