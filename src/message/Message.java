package message;

public class Message extends GameMessage {
	private static final long serialVersionUID = -4847411748052026276L;
	
	public static final int PLAYER_LIST = 0;
	
	public static final int JOIN = 1;
	
	public static final int FULL = 2;
	
	public static final int QUIT = 3;
	
	public static final int READY = 4;
	
	public static final int START = 5;
	
	public static final int MOVE = 6;
	
	public static final int MSG = 7;
	
	public static final int YOUR_TURN = 8;
	
	public static final int FIRST = 9;

	public Message(int type, int playerID, Object data) {
		super(type, playerID, data);
	}
}
