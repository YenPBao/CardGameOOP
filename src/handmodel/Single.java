package handmodel;

import card.Card;
import card.CardList;
import player.Player;

public class Single extends Hand implements HandType{
	private static final long serialVersionUID = 1L;
	
	public Single(Player player, CardList card)
	{
		super(player,card);
	}
	
	public Card getTopCard()
	{
		return this.getCard(0); 
	}
	
	public boolean isValid()
	{
		if(this.size() == 1)
			{ return true; }
		
		return false;
	}
	
	public String getType()
	{
		return new String("Single"); 
	}
	
}