package handmodel;

import java.util.Arrays;

import card.Card;
import card.CardList;
import player.Player;

public class Straight extends Hand implements HandType{


	private static final long serialVersionUID = 1L;
	
	public Straight(Player player, CardList card)
	{
		super(player,card);
	}
	
	public Card getTopCard ()
	{
		int[] num_ranks = new int[this.size()] ;
		for (int i = 0; i < this.size(); i++)
		{
			num_ranks[i] = this.getCard(i).rank;
		}
		Arrays.sort(num_ranks);
		int index = num_ranks[this.size()-1];
		for (int i = 0 ; i < this.size(); i++)
		{
			if (this.getCard(i).rank == index)
			{
				index = i;
				break;
			}
		}
		return this.getCard(index);
	}
	
	public boolean isValid ()
	{
		if(this.size() <=2)
		{
			return false;
		}
		int[] num_ranks = new int[this.size()] ;
		for (int i = 0; i < this.size(); i++)
		{
			num_ranks[i] = this.getCard(i).rank;
		}
		Arrays.sort(num_ranks);
		for (int i = 0; i < this.size()-1;i++)
		{
			if (num_ranks[i]+1!= num_ranks[i+1])
			{
				return false;
			}
		}
		return true;
	}

	public String getType()
	{
		return "Straight"; 
	}
}
