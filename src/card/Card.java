package card;

import java.io.Serializable;
public class Card implements Comparable<Card>, Serializable {
	private static final long serialVersionUID = -713898713776577970L;
	static boolean SUPPORT_COLOR = false;
	private static final char[] SUITS = {'\u2660' ,'\u2663' ,'\u2666',  '\u2665',}; // {Chuồn, Tép, Rô, Cơ}
	private static final char[] RANKS = { '3', '4', '5', '6', '7','8', '9', '0', 'J', 'Q', 'K','A', '2'};

	public final int suit; // 0 - 3
	public final int rank; // 0 - 12
	
	public Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public int getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}

	public String toString() {
		if (SUPPORT_COLOR && (this.suit  == 3|| this.suit == 2)) {
			return "\u001B[31m" + SUITS[this.suit] + RANKS[this.rank] + "\u001B[0m";
		} else {
			return "" + SUITS[this.suit] + RANKS[this.rank];
		}
	}

	public int compareTo(Card card) {
		if (this.rank > card.rank) {
			return 1;
		} else if (this.rank < card.rank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}

	public boolean equals(Object card) {
		return (this.rank == ((Card) card).getRank() && suit == ((Card) card)
				.getSuit());
	}

	public int hashCode() {
		return rank;
	}
}
