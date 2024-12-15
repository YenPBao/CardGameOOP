package game;

import message.GameMessage;

public interface Network {
	// Lấy ID của người chơi
	public int getPlayerID();
	// Thiết lập ID người chơi
	public void setPlayerID(int playerID);
	// Lấy tên người chơi
	public String getPlayerName();
	// Thiết lập tên người chơi
	public void setPlayerName(String playerName);
	public String getServerIP();

	public void setServerIP(String serverIP);

	public int getServerPort();

	public void setServerPort(int serverPort);
	// Kết nối
	public void makeConnection();
	// Dịch thông điệp
	public void parseMessage(GameMessage message);
	// Gửi thông điệp
	public void sendMessage(GameMessage message);
}
