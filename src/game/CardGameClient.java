package game;

import java.util.ArrayList;

import card.Deck;
import handmodel.Hand;
import player.Player;

public interface CardGameClient {
	public int getNumOfPlayers();

	public Deck getDeck();

	public ArrayList<Player> getPlayerList();

	public ArrayList<Hand> getHandsOnTable();

	public int getCurrentIdx();

	public void start(Deck deck);
	
	public void makeMove(int playerID, int[] cardIdx);

	public void checkMove(int playerID, int[] cardIdx);

	public boolean endOfGame();
}
