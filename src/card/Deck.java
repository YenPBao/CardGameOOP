package card;


public class Deck extends CardList {
	private static final long serialVersionUID = -3886066435694112173L;
	
	public Deck() {
		initialize();
	}

	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				Card card = new Card(i, j);
				addCard(card);
			}
		}
	}

	public void shuffle() {
		for (int i = 0; i < this.size(); i++) {
			int j = (int) (Math.random() * this.size());
			if (i != j) {
				Card card = setCard(i, getCard(j));
				setCard(j, card);
			}
		}
	}
}