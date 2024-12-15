package handmodel;

import card.Card;
import card.CardList;
import player.Player;

public class Hand extends CardList implements HandType {
	private static final long serialVersionUID = 1L;
	private Player player;
	
	public Hand(Player player, CardList cards)
	{
		this.player = player;
		
		for(int i = 0; i < cards.size();i++)
		{
			this.addCard(cards.getCard(i));
		}
	}
	
	public Player getPlayer()
	{
		return this.player;
		
	}
	
	public Card getTopCard()
	{
		return null;
		
	}
	
	public boolean beats(Hand hand)
	{
		if (hand.size() == 1)
		{
			if (hand.getCard(0).getRank() == 12 && this.size() == 4 && this instanceof Quad && this.isValid())
			{
				return true;
			}if (hand.getCard(0).getRank() == 12 && this.size() == 4 && this instanceof PairConsecutive && this.isValid())
			{
				return true;
			}
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard())==1  )
			{
				return true;
			}
		}
		if (hand.size()==2)
		{
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard())==1  )
			{
				return true;
			}
		}
		if (hand.size() >= 3)
		{
			if (this instanceof Triple)
			{
				if (this.size() == hand.size() && this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
				{
					return true;
				}
				else
				{
				return false;
				}
			}
			if (this instanceof Straight)
			{
				if (this.size() == hand.size() && this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard())==1 )
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
			if (this instanceof Quad)
			{
				if (this.size() == hand.size() && this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard())==1 )
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
			if (this instanceof PairConsecutive)
			{
				if (this.size() == hand.size() && this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard())==1 )
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean isValid()
	{
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}