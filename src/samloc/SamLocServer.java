package samloc;

import card.Deck;
import game.Server;
import tienlen.TienLenDeck;

public class SamLocServer extends Server{
	public SamLocServer(int n) {
		super("Sam Loc Server", n);
	}

	public Deck createDeck() {
		return new SamLocDeck(); 
	}
}
