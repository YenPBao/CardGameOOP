package handmodel;

import card.Card;
import card.CardList;
import player.Player;

public class Pair extends Hand implements HandType {

	private static final long serialVersionUID = 1L;
		
	public Pair(Player player, CardList card)
	{
		super(player,card);
	}
	

	
	public Card getTopCard()
	{
		
		if(this.getCard(0).suit > this.getCard(1).suit) 
		{
			return this.getCard(0);
		}
		
		return this.getCard(1); 
		
	}
	

	
	public boolean isValid()
	{
		if(this.size() != 2) 
		{
			return false;
		}
		
		if(this.getCard(0).rank == this.getCard(1).rank) 
		{
			return true;
			
		}
		
		return false;	
		
	}
	

	
	public String getType()
	{
		
		
		return new String("Pair"); //Tell its a pair
	}
}
