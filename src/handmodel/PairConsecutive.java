package handmodel;

import card.Card;
import card.CardList;
import player.Player;

public class PairConsecutive extends Hand implements HandType {

    public PairConsecutive (Player player, CardList cards) {
        super(player, cards);
    }

    @Override
    public boolean isValid() {
        if (this.size() != 6) {
            return false; // Phải có đúng 6 lá bài
        }

        // Kiểm tra từng đôi có cùng rank và các đôi liên tiếp
        for (int i = 0; i < 6; i += 2) {
            if (this.getCard(i).rank != this.getCard(i + 1).rank) {
                return false; // Không phải đôi
            }
            if (i > 0 && this.getCard(i).rank != this.getCard(i - 2).rank + 1) {
                return false; // Không liên tiếp
            }
        }
        return true;
    }

    @Override
    public Card getTopCard() {
        // Top card là lá bài có rank cao nhất trong đôi cuối cùng
        return this.getCard(5);
    }

    @Override
    public String getType() {
        return new String("ConsecutivePair");
    }
}
