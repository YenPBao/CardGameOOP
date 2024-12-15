package card;

import java.io.Serializable;
import java.util.ArrayList;


public class CardList implements Serializable {
	private static final long serialVersionUID = -3711761437629470849L;
	private ArrayList<Card> cards = new ArrayList<Card>();

// thêm lá bài vào list bài
	public void addCard(Card card) {
		if (card != null) {
			cards.add(card);
		}
	}

// lấy lá bài thứ i ra
	public Card getCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.get(i);
		} else {
			return null;
		}
	}

// xóa lá bài thứ i did
	public Card removeCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.remove(i);
		} else {
			return null;
		}
	}

// xóa đối tượng bài đi
	public boolean removeCard(Card card) {
		return cards.remove(card);
	}

// xóa toàn bộ các lá bài 
	public void removeAllCards() {
		cards = new ArrayList<Card>();
	}

// thiết lập đối tượng lá bài ở vị trí i
	public Card setCard(int i, Card card) {
		if (i >= 0 && i < cards.size()) {
			return cards.set(i, card);
		} else {
			return null;
		}
	}

// kiểm tra xem list bài có chứa lá bài không
	public boolean contains(Card card) {
		return cards.contains(card);
	}

// kiểm tra xem list bài có trống không 
	public boolean isEmpty() {
		return cards.isEmpty();
	}

// sắp xếp lại các lá bài
	public void sort() {
		cards.sort(null);
	}

// kích thược list bài 
	public int size() {
		return cards.size();
	}

	public void print() {
		print(true, false);
	}

	
	public void print(boolean printFront, boolean printIndex) {
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				String string = "";
				if (printIndex) {
					string = i + " ";
				}
				if (printFront) {
					string = string + "[" + cards.get(i) + "]";
				} else {
					string = string + "[  ]";
				}
				if (i % 13 != 0) {
					string = " " + string;
				}
				System.out.print(string);
				if (i % 13 == 12 || i == cards.size() - 1) {
					System.out.println("");
				}
			}
		} else {
			System.out.println("[Empty]");
		}
	}

	
	public String toString() {
		String string = "";
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				string = string + "[" + cards.get(i) + "]";
				if (i != cards.size() - 1) {
					string = string + " ";
				}
			}
		} else {
			string = "[Empty]";
		}

		return string;
	}
}
