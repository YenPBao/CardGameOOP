package samloc;

import card.Deck;

public class SamLocDeck extends Deck {
	
	private static final long serialVersionUID = 1L;
	
	public void initialize() 
	{
		removeAllCards();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 13; j++) 
			{
				SamLocCard samloccard = new SamLocCard(i, j);
				this.addCard(samloccard);
			}
		}
		
	}

}

