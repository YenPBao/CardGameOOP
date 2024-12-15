package view;

public interface GameTable {
	public void setActivePlayer(int activePlayer);

	public int[] getSelected();

	public void resetSelected();

	public void repaint();
	
	public void printMsg(String msg);

	public void clearMsgArea();

	public void reset();

	public void enable();

	public void disable();
}
