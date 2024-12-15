package tienlen;


import card.Card;

public class TienLenCard extends Card {


	private static final long serialVersionUID = 1L;

	public TienLenCard(int suit, int rank) {
		super(suit, rank);
		
	}
	
	public int compareTo(Card card)
	{
		
		int rank = this.rank;
		int card_rank = card.rank;
		
		if (rank > card_rank) {
			return 1;
		} 
		else if (rank < card_rank) {
			return -1;
		} 
		
		else if (this.suit > card.suit) {
			return 1;
		} 
		
		else if (this.suit < card.suit) {
			return -1;
		} 
		
		else {
			return 0;
		}
	}
}