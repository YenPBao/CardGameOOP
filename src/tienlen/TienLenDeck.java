package tienlen;

import tienlen.TienLenCard;
import card.Deck;

public class TienLenDeck extends Deck {
	
	private static final long serialVersionUID = 1L;

	public void initialize() 
	{
		removeAllCards();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 13; j++) 
			{
				TienLenCard bigtwocard = new TienLenCard(i, j);
				this.addCard(bigtwocard);
			}
		}
		
	}

}
