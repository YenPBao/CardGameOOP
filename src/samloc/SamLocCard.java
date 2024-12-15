package samloc;

import card.Card;

public class SamLocCard extends Card {


	private static final long serialVersionUID = 1L;


	public SamLocCard(int suit, int rank) {
		super(suit, rank);
		
	}


	public int compareTo(Card card)
	{
		
		int rank = this.rank;
		int card_rank = card.getRank();
		
		if (rank > card_rank) {
			return 1;
		} 
		else if (rank < card_rank) {
			return -1;
		} 
		else {
			return 0;
		}
	}
	
}