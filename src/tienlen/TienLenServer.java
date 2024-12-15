package tienlen;

import tienlen.TienLenDeck;
import card.Deck;
import game.Server;

public class TienLenServer extends Server{
	public TienLenServer(int n) {
		super("Tien Len Server", n);
	}

	public Deck createDeck() {
		return new TienLenDeck(); 
	}
}
