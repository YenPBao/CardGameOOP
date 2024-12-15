package handmodel;

import card.Card;
import card.CardList;
import player.Player;

public class Triple extends Hand implements HandType {


	private static final long serialVersionUID = 1L;
	
	public Triple(Player player, CardList card)
	{
		super(player,card);
	}
	

	
	public Card getTopCard()
	{
		if(this.getCard(0).suit > this.getCard(1).suit) 
		{
			if(this.getCard(0).suit > this.getCard(2).suit)
			{
				return this.getCard(0); 
			}
			
			else
			{
				return this.getCard(2); 
			}
		}
		
		else
		{
			if(this.getCard(1).suit > this.getCard(2).suit) 
			{
				return this.getCard(1);
			}
			
			else
			{
				return this.getCard(2);
			}
		}
		
	}
	

	
	public boolean isValid()
	{
		if(this.size()  != 3) 
		{
			return false;
		}
		
		if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(0).rank == this.getCard(2).rank)  
		{
			return true;
		}
		
		return false;
	}
	
	
	
	
	

	
	public String getType()
	{
		return new String("Triple"); //Tell its a triple
	}

}
