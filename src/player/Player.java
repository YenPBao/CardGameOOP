package player;

import java.util.ArrayList;

import card.Card;
import card.CardList;

public class Player {
	private static int playerId = 0;
	private String name = "";
	private CardList cardsInHand = new CardList();

	public Player() {
		this.name = "Player " + playerId;
		playerId++;
	}

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addCard(Card card) {
		if (card != null) {
			cardsInHand.addCard(card);
		}
	}

	public void removeCards(CardList cards) {
		for (int i = 0; i < cards.size(); i++) {
			cardsInHand.removeCard(cards.getCard(i));
		}
	}

	public void removeAllCards() {
		cardsInHand = new CardList();
	}

	public int getNumOfCards() {
		return cardsInHand.size();
	}

	public void sortCardsInHand() {
		cardsInHand.sort();
	}

	public CardList getCardsInHand() {
		return cardsInHand;
	}

	public CardList play(int[] cardIdx) {
		if (cardIdx == null) {
			return null;
		}

		CardList cards = new CardList();
		for (int idx : cardIdx) {
			if (idx >= 0 && idx < cardsInHand.size()) {
				cards.addCard(cardsInHand.getCard(idx));
			}
		}

		if (cards.isEmpty()) {
			return null;
		} else {
			return cards;
		}
	}
}
